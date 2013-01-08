package com.facetime.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Provide XML process utility<p>
 *
 * Used to convert xml node to <code>String</code>
 *
 * @author dzb
 */
public final class XMLUtils {

	public static final String INDENT = "  ";

	public static final DocumentBuilderFactory builderFactory;
	public static final TransformerFactory transformerFactory;
	public static final XPathFactory xpathFactory;

	// Initialize global document builder factory
	// Use apache xerces and xalan as default.
	static {

		builderFactory = DocumentBuilderFactory.newInstance();
		if (builderFactory == null)
			throw new RuntimeException(
					"Cannot find any DocumentBuilderFactory implementation in environments. go apache.org download xerces please.");

		transformerFactory = TransformerFactory.newInstance();
		if (transformerFactory == null)
			throw new RuntimeException(
					"Cannot find any TransformerFactory implementation in environments. go apache.org download xalan please.");

		xpathFactory = XPathFactory.newInstance();
		if (xpathFactory == null)
			throw new RuntimeException(
					"Cannot find any XPathFactory implementation in environments. go apache.org download xalan please.");

		// set default properties to document builder
		builderFactory.setNamespaceAware(true);
		builderFactory.setValidating(false);
		builderFactory.setIgnoringComments(true);

	}
	/**
	 * The cache size for the XSL transforms
	 */
	private static final int transCacheSize = 10;
	private static Hashtable<String, Transformer> transCache = new Hashtable<String, Transformer>();
	private static LinkedList<String> transKeyList = new LinkedList<String>();

	private static ThreadLocal<DocumentBuilder> local_builder = new ThreadLocal<DocumentBuilder>() {
		protected synchronized DocumentBuilder initialValue() {
			try {
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				return builder;
			} catch (Exception ex) {
				return null;
			}
		}
	};

	private static ThreadLocal<Transformer> local_transformer = new ThreadLocal<Transformer>() {
		protected synchronized Transformer initialValue() {
			try {
				return transformerFactory.newTransformer();
			} catch (Exception ex) {
				return null;
			}
		}
	};

	private static ThreadLocal<XPath> local_xpath = new ThreadLocal<XPath>() {
		protected synchronized XPath initialValue() {
			return xpathFactory.newXPath();
		}
	};

	/**
	 * Clone given Node into target Document. If targe is null, same Document will be used.
	 * If deep is specified, all children below will also be cloned.
	 */
	public static Node cloneNode(Node node, Document target, boolean deep) throws DOMException {
		if (target == null || node.getOwnerDocument() == target)
			// same Document
			return node.cloneNode(deep);
		else {
			//DOM level 2 provides this in Document, so once xalan switches to that,
			//we can take out all the below and just call target.importNode(node, deep);
			//For now, we implement based on the javadocs for importNode
			Node newNode;
			int nodeType = node.getNodeType();

			switch (nodeType) {
			case Node.ATTRIBUTE_NODE:
				newNode = target.createAttribute(node.getNodeName());

				break;

			case Node.DOCUMENT_FRAGMENT_NODE:
				newNode = target.createDocumentFragment();

				break;

			case Node.ELEMENT_NODE:

				Element newElement = target.createElement(node.getNodeName());
				NamedNodeMap nodeAttr = node.getAttributes();

				if (nodeAttr != null)
					for (int i = 0; i < nodeAttr.getLength(); i++) {
						Attr attr = (Attr) nodeAttr.item(i);

						if (attr.getSpecified()) {
							Attr newAttr = (Attr) cloneNode(attr, target, true);
							newElement.setAttributeNode(newAttr);
						}
					}

				newNode = newElement;

				break;

			case Node.ENTITY_REFERENCE_NODE:
				newNode = target.createEntityReference(node.getNodeName());

				break;

			case Node.PROCESSING_INSTRUCTION_NODE:
				newNode = target.createProcessingInstruction(node.getNodeName(), node.getNodeValue());

				break;

			case Node.TEXT_NODE:
				newNode = target.createTextNode(node.getNodeValue());

				break;

			case Node.CDATA_SECTION_NODE:
				newNode = target.createCDATASection(node.getNodeValue());

				break;

			case Node.COMMENT_NODE:
				newNode = target.createComment(node.getNodeValue());

				break;

			case Node.NOTATION_NODE:
			case Node.ENTITY_NODE:
			case Node.DOCUMENT_TYPE_NODE:
			case Node.DOCUMENT_NODE:
			default:
				throw new IllegalArgumentException("Importing of " + node + " not supported yet");
			}

			if (deep)
				for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
					newNode.appendChild(cloneNode(child, target, true));

			return newNode;
		}
	}

	public static String encodeXML(String xml) {

		if (xml == null)
			return "";

		char[] chars = xml.toString().toCharArray();

		StringBuffer out = new StringBuffer();

		for (char c : chars)
			switch (c) {
			case '&':
				out.append("&amp;");

				break;

			case '<':
				out.append("&lt;");

				break;

			case '>':
				out.append("&gt;");

				break;

			case '\"':
				out.append("&quot;");

				break;

			default:
				out.append(c);
			}

		return out.toString();
	}

	public static NodeList evaluateList(Node node, String xpathExpression) {

		try {
			XPath xpath = local_xpath.get();
			XPathExpression expr = xpath.compile(xpathExpression);
			return (NodeList) expr.evaluate(node, XPathConstants.NODESET);
		} catch (Exception ex) {
			return null;
		}
	}

	public static Node evaluateNode(final Node node, String xpathExpression) {
		try {
			XPath xpath = local_xpath.get();
			XPathExpression expr = xpath.compile(xpathExpression);
			return (Node) expr.evaluate(node, XPathConstants.NODE);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String evaluateText(final Node node, String xpathExpression) {
		try {
			XPath xpath = local_xpath.get();
			XPathExpression expr = xpath.compile(xpathExpression);
			return (String) expr.evaluate(node, XPathConstants.STRING);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Utility method that returns the first child element
	 * identified by its getName.
	 * @param ele the DOM element to analyze
	 * @param childName the child element getName to look for
	 * @return the <code>org.w3c.dom.Element</code> creating,
	 * or <code>null</code> if none found
	 */
	public static Element getChildElement(Element ele, String childName) {
		NodeList nl = ele.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element && nodeNameEquals(node, childName))
				return (Element) node;
		}
		return null;
	}

	/**
	 * Retrieve all child elements of the given DOM element that match
	 * the given element getName. Only look at the direct child level of the
	 * given element; do not go into further depth (in contrast to the
	 * DOM API's <code>getElementsByTagName</code> method).
	 * @param ele the DOM element to analyze
	 * @param childEleName the child element getName to look for
	 * @return a List of child <code>org.w3c.dom.Element</code> instances
	 * @see org.w3c.dom.Element
	 * @see org.w3c.dom.Element#getElementsByTagName
	 */
	public static List<Node> getChildElements(Element ele, String childEleName) {
		NodeList nl = ele.getChildNodes();
		List<Node> childEles = new ArrayList<Node>();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element && nodeNameEquals(node, childEleName))
				childEles.add(node);
		}
		return childEles;
	}

	/**
	 * Return the contained text within an Element. Returns null if no text found.
	 */
	public static String getNodeText(final Node node) {
		NodeList nl = node.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node c = nl.item(i);
			if (c instanceof Text)
				return ((Text) c).getData();
		}
		return null;
	}

	/**
	 * Extract the text symbol from the given DOM element, ignoring XML comments.
	 * <p>Appends all CharacterData nodes and EntityReference nodes
	 * into a single String symbol, excluding Comment nodes.
	 * @see CharacterData
	 * @see EntityReference
	 * @see Comment
	 */
	public static String getTextValue(Element valueEle) {
		StringBuffer value = new StringBuffer();
		NodeList nl = valueEle.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node item = nl.item(i);
			if (item instanceof CharacterData && !(item instanceof Comment) || item instanceof EntityReference)
				value.append(item.getNodeValue());
		}
		return value.toString();
	}

	/**
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document newDocument() throws ParserConfigurationException {

		DocumentBuilder builder = local_builder.get();
		if (builder == null)
			throw new ParserConfigurationException("Init DocumentBuilder error.");
		return builder.newDocument();
	}

	/**
	 * Create blank Document, and insert root element with given getName.
	 */
	public static Document newDocument(String rootElementName) throws ParserConfigurationException {
		Document doc = newDocument();
		doc.appendChild(doc.createElement(rootElementName));

		return doc;
	}

	/**
	 * Namespace-aware equals comparison. Returns <code>true</code> if either
	 * {@link Node#getLocalName} or {@link Node#getNodeName} equals <code>desiredName</code>,
	 * otherwise returns <code>false</code>.
	 */
	public static boolean nodeNameEquals(Node node, String desiredName) {
		assert node != null : "Node must not be null";
		assert desiredName != null : "Desired getName must not be null";
		return desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName());
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static Document parse(File file) {

		return parse(file, null);
	}

	/**
	 * 
	 * @param file
	 * @param resovler
	 * @return
	 */
	public static Document parse(File file, EntityResolver resovler) {

		assert file != null && file.exists();

		try {

			InputSource is = new InputSource(new FileReader(file));

			return parse(is, resovler);

		} catch (FileNotFoundException ex) {

			return null;
		}
	}

	public static Document parse(InputSource source) {

		return parse(source, null);
	}

	public static Document parse(InputSource source, EntityResolver resolver) {

		return parse(source, resolver, null);
	}

	public static Document parse(InputSource source, EntityResolver resovler, ErrorHandler handler) {

		assert source != null;

		try {
			DocumentBuilder builder = local_builder.get();
			if (builder == null)
				throw new Exception("Init DocumentBuilder error.");

			if (resovler == null)
				builder.setEntityResolver(new EmptyEntityResolver());
			else
				builder.setEntityResolver(resovler);

			if (handler == null)
				builder.setErrorHandler(new SAXErrorHandler(null));
			else
				builder.setErrorHandler(handler);

			return builder.parse(source);
		} catch (Exception ex) {
			return null;
		}
	}

	public static Document parse(InputStream stream) {

		return parse(stream, null);
	}

	public static Document parse(InputStream stream, EntityResolver resovler) {

		assert stream != null;

		return parse(new InputSource(stream), null);
	}

	public static Document parse(String xml) {

		return parse(xml, null);
	}

	/**
	 * Parse a String containing XML data into a Document.
	 * Note that String contains XML itself and is not URI.
	 */
	public static Document parse(String xml, EntityResolver resovler) {

		if (xml == null || xml.length() < 3)
			return null;

		return parse(new InputSource(new StringReader(xml)), resovler, null);
	}

	public static Document parse(URL url) {

		return parse(new InputSource(url.toString()), null);
	}

	/**
	 * Parse the contents of a URL's XML into Document.
	 */
	public static Document parse(URL url, EntityResolver resovler) {

		return parse(new InputSource(url.toString()), resovler, null);
	}

	public static void printIndent(PrintWriter out, int indent) {
		for (int i = 0; i < indent; i++)
			out.print(INDENT);
	}

	/**
	 * 保存节点到文件
	 * 
	 * @param node
	 * @param file
	 * @throws Exception
	 */
	public static void saveToFile(final Node node, File file) throws Exception {
		assert file != null;
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			transform(node, writer, "", "");
			writer.close();
		} catch (Exception ex) {
			throw ex;
		}
	}

	/** Output a DOM Node (such as a Document) to String */
	public static String saveToString(final Node node) {

		StringWriter writer = new StringWriter();
		String result = null;
		try {
			transform(node, writer, "", "");
			result = writer.toString();
			writer.close();
			return result;
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * This method applies an XSL sheet to an XML document.
	 * @param xml the XML source
	 * @param xsl the XSL source
	 * @throws TransformerException
	 */
	public static Document transform(Document xml, Document xsl) throws TransformerException {
		try {
			Document result = builderFactory.newDocumentBuilder().newDocument();
			Transformer t = transformerFactory.newTransformer(new DOMSource(xsl));
			t.transform(new DOMSource(xml), new DOMResult(result));
			return result;
		} catch (ParserConfigurationException pce) {
			throw new TransformerException(pce);
		} catch (TransformerConfigurationException tce) {
			throw new TransformerException(tce);
		}
	}

	/**
	 * Perform XSL transformation.
	 */
	public static void transform(InputStream xml, InputStream xsl, OutputStream result) throws TransformerException {
		transform(new InputStreamReader(xml), new InputStreamReader(xsl), new OutputStreamWriter(result));
	}

	/**
	 * 
	 * @param node
	 * @param writer
	 * @param docID
	 * @param sysID
	 * @throws TransformerException
	 */
	public static void transform(Node node, Writer writer, String docID, String sysID) throws TransformerException {

		Transformer transformer = local_transformer.get();

		if (transformer == null)
			throw new TransformerException("Init Transformer error.");

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");

		if (StringUtils.isNotEmpty(docID))
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, docID);

		if (StringUtils.isNotEmpty(sysID))
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, sysID);

		Source source = new DOMSource(node);
		Result result = new StreamResult(writer);
		transformer.transform(source, result);
	}

	/**
	 * Perform XSL transformation.
	 */
	public static void transform(Reader xml, Reader xsl, Writer result) throws TransformerException {
		transform(xml, xsl, result, null);
	}

	/**
	 * Perform XSL transformation, with option.
	 */
	public static void transform(Reader xml, Reader xsl, Writer result, Map<String, String> parameters)
			throws TransformerException {
		transform(xml, xsl, result, parameters, xsl.toString());
	}

	/**
	 * 
	 * @param xml
	 * @param xsl
	 * @param result
	 * @param parameters
	 * @param xslkey
	 * @throws TransformerException
	 */
	public static void transform(Reader xml, Reader xsl, Writer result, Map<String, String> parameters, String xslkey)
			throws TransformerException {
		try {
			Transformer t;
			if (null != xslkey && transCache.containsKey(xslkey)) {
				t = transCache.get(xslkey);
				synchronized (transKeyList) {
					transKeyList.remove(xslkey);
					transKeyList.add(xslkey);
				}
			} else {
				t = transformerFactory.newTransformer(new StreamSource(xsl));

				if (null != xslkey) {
					transCache.put(xslkey, t);
					transKeyList.add(xslkey);

					synchronized (transKeyList) {
						int s = transKeyList.size();
						int cacheSize = transCacheSize;
						int iterations = 1;

						// if the cache size was adjusted after the cache is initialized,we don't want to shrink TOO fast.
						// just to be nice to runtime performance.
						if (s > cacheSize + 1)
							iterations = 2;
						while (iterations-- != 0) {
							String removalKey = transKeyList.get(0);
							transKeyList.remove(0);
							transCache.remove(removalKey);
						}
					}
				}
			}

			if (parameters != null) {
				Iterator<String> iter = parameters.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					String value = parameters.get(key);
					t.setParameter(key, value);
				}
			}
			t.transform(new StreamSource(xml), new StreamResult(result));

		} catch (TransformerConfigurationException tce) {
			throw new TransformerException(tce);
		}
	}

	/**
	 * Perform XSL transformation. XML and XSL supplied in Strings and result returned as String.
	 * Note that inputs are actual XML/XSL data, not URIs.
	 */
	public static String transform(String xml, String xsl) throws TransformerException {

		StringWriter result = new StringWriter();
		transform(new StringReader(xml), new StringReader(xsl), result);
		return result.toString();
	}

	/**
	 * 通用的SAXErrorHandler
	 * @author Zenberg
	 *
	 */
	public static class SAXErrorHandler implements ErrorHandler {

		private String url = null;

		public SAXErrorHandler(final String name) {
			url = name;
		}

		public void error(SAXParseException exception) throws SAXException {
			throw new SAXException(getMessage(exception));
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			throw new SAXException(getMessage(exception));
		}

		public void warning(SAXParseException exception) throws SAXException {
		}

		private String getMessage(SAXParseException exception) {
			return exception.getMessage() + " (" + (url != null ? " url=" + url + ' ' : "") + "\r\n line:"
					+ exception.getLineNumber()
					+ (exception.getColumnNumber() > -1 ? " col:" + exception.getColumnNumber() : "") + ')';
		}
	}

	/**
	 * 避免dtd校验的entityResolver
	 * @author dzb2k9
	 *
	 */
	private static class EmptyEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			return new InputSource(new StringReader(""));
		}
	}
}
