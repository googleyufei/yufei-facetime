package com.facetime.core.utils;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.facetime.core.logging.Log;
import com.facetime.core.logging.LogFactory;

/**
 * XML使用SAX处理的辅助功能类
 */
public class SAXUtils {

	private static final Log log = LogFactory.getLog(SAXUtils.class.getName());

	public static SAXParserFactory parserFactory;

	static {
		if (parserFactory == null)
			parserFactory = SAXParserFactory.newInstance();
	}

	public static void parse(File file, DefaultHandler handler) throws Exception {
		try {
			SAXParser parser = parserFactory.newSAXParser();
			parser.parse(file, handler);

		} catch (Exception ex) {
			log.error("parse error : " + ex.getMessage());
			throw ex;
		}
	}

	public static void parse(InputSource source, DefaultHandler handler) throws Exception {
		try {
			SAXParser parser = parserFactory.newSAXParser();
			parser.parse(source, handler);

		} catch (Exception ex) {
			log.error("parse error : " + ex.getMessage());
			throw ex;
		}
	}

	//public static

	public static void parse(InputStream stream, DefaultHandler handler) throws Exception {
		try {
			SAXParser parser = parserFactory.newSAXParser();
			parser.parse(stream, handler);
		} catch (Exception ex) {
			log.error("parse error : " + ex.getMessage());
			throw ex;
		}
	}
}
