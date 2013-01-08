package com.facetime.communication.activemq;

import static com.facetime.core.conf.SysLogger.facetimeLogger;

import com.facetime.communication.bean.MessageDTO;
import com.facetime.communication.bean.MessageType;
import com.facetime.communication.bean.MessagesDTO;
import com.facetime.core.http.ErrorType;
import com.facetime.core.http.HttpSender;
import com.facetime.core.http.PojoMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.DefaultTransportListener;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * 发送MQ消息到MQ服务器
 * 
 * FIXME 等待优化, 其中的getInstance()方法存在重大性能问题
 */
public class AmqProducer {

	private static final String msgStart = "<message>";
	private static final String msgEnd = "</message>";

	/**
	 * 远程服务请求代理
	 */
	private static final HttpSender proxy = HttpSender.get();

	private boolean connected = false;

	private ActiveMQConnectionFactory factory;

	private ActiveMQConnection connection;
	private Session session;
	private MessageProducer producer;

	private static AmqProducer instance;

	public synchronized static AmqProducer getInstance() {
		if (instance == null) {
			instance = new AmqProducer();
		}
		return instance;
	}

	private AmqProducer() {
		factory = new ActiveMQConnectionFactory(AmqConsumer.BROKER_URL);
		try {
			connection = getConnection();
		} catch (JMSException ex) {
			facetimeLogger.error("", ex);
		}
	}

	/**
	 * send message, save to app server
	 * 
	 * @param token
	 * @param messageDTO
	 */
	public String sendMessage(String token, MessageDTO messageDTO) {
		return sendMessage(token, messageDTO, true);
	}

	/**
	 * send message, save to app server
	 * 
	 * @param token
	 * @param messageDTO
	 * @param trySave
	 *            try to save message to app server or database
	 * @return
	 */
	public String sendMessage(String token, MessageDTO messageDTO, boolean trySave) {
		String result = ErrorType.OK;
		try {
			if (connected) {
				messageDTO.setSendDate(new Date());
				String messageJson = PojoMapper.toJson(messageDTO);
				// send message to MQ
				sendMessageToMQ(messageDTO.getReceiver(), messageDTO.getMessageType(), messageJson);
				if (trySave && token != null) {
					// save message history
					saveMessage(token, messageDTO.getMessageType(), messageJson);
				}
			} else {
				result = ErrorType.ERR_MQ_DISCONNTECTED;
			}

		} catch (Exception ex) {
			facetimeLogger.error("", ex);
			result = ErrorType.OK;
		}
		return result;
	}

	/**
	 * send messages
	 * 
	 * @param token
	 * @param messageDTOs
	 * @param trySave
	 * @return
	 */
	public String sendMessages(String token, List<MessageDTO> messageDTOs, boolean trySave) {
		String result = ErrorType.OK;

		for (MessageDTO MessageDTO : messageDTOs) {
			result = sendMessage(token, MessageDTO, false);
		}

		if (trySave && ErrorType.OK.equals(result) && token != null) {
			// save message history
			List<MessageDTO> saveMsgs = new ArrayList<MessageDTO>();
			for (MessageDTO msg : messageDTOs) {
				if (isSaveMessage(msg.getMessageType())) {
					MessageDTO m = new MessageDTO();
					m.setSender(msg.getSender());
					m.setFromUser(msg.getFromUser());
					m.setReceiver(msg.getReceiver());
					m.setReceiverName(msg.getReceiverName());
					m.setMessageType(msg.getMessageType());
					m.setMessageBody(msg.getMessageBody());
					m.setStatus(msg.getStatus());
					m.setSendDate(msg.getSendDate());
					saveMsgs.add(m);
				}
			}
			if (saveMsgs.size() > 0) {
				MessagesDTO messagesDTO = new MessagesDTO();
				messagesDTO.setMessageDTOList(saveMsgs);
				String postData = PojoMapper.toJson(messagesDTO);
				proxy.postToUrl("", token, postData);
			}
		}

		return result;
	}

	/**
	 * send message, not save to app server
	 * 
	 * @param messageDTO
	 * @return
	 */
	public String sendMessage(MessageDTO messageDTO) {
		return sendMessage(null, messageDTO, false);
	}

	String DEFAULT_DOMAIN_NAME = "conlect.oatOS.";

	/**
	 * send message to MQ
	 * 
	 * @param receiver
	 * @param type
	 * @param messageJson
	 * @throws Exception
	 */
	private void sendMessageToMQ(long receiver, String type, String messageJson) throws Exception {
		Destination destination = null;
		String destinationName = DEFAULT_DOMAIN_NAME + String.valueOf(receiver);
		if (MessageType.ChatMessage.equals(type)) {
			// topic
			destination = getSession().createTopic(destinationName);
		} else {
			// TODO
			// queue
			// destination = getSession().createQueue(destinationName);
			destination = getSession().createTopic(destinationName);
		}
		// build message
		StringBuilder msg = new StringBuilder();
		msg.append(msgStart);
		msg.append(StringEscapeUtils.escapeXml(messageJson));
		msg.append(msgEnd);
		TextMessage txtMessage = getSession().createTextMessage(msg.toString());
		// send message to mq
		getProducer().send(destination, txtMessage);
	}

	/**
	 * save message history
	 * 
	 * @param token
	 * @param type
	 * @param messageJson
	 */
	public void saveMessage(String token, String type, String messageJson) {
		if (token != null) {
			if (isSaveMessage(type)) {
				proxy.postToUrl(null, token, messageJson);
			}
		}
	}

	/**
	 * get connection
	 * 
	 * @return
	 * @throws JMSException
	 */
	private ActiveMQConnection getConnection() throws JMSException {
		if (connection == null) {
			connection = (ActiveMQConnection) factory.createConnection();
			connection.start();
			connected = true;
			connection.addTransportListener(new DefaultTransportListener() {

				@Override
				public void onException(IOException error) {
					connected = false;
					facetimeLogger.error("", error);
					// destroy();
				}

				@Override
				public void transportInterupted() {
					connected = false;
					facetimeLogger.error("mq connection interupted");
					// destroy();
				}

				@Override
				public void transportResumed() {
					connected = true;
					facetimeLogger.info("mq connection resumed");
				}

			});
		}
		return connection;
	}

	/**
	 * get session
	 * 
	 * @return
	 * @throws JMSException
	 */
	private Session getSession() throws JMSException {
		if (session == null) {
			session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
		}
		return session;
	}

	/**
	 * get message producer
	 * 
	 * @return
	 * @throws JMSException
	 */
	private MessageProducer getProducer() throws JMSException {
		if (producer == null) {
			producer = getSession().createProducer(null);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}
		return producer;
	}

	/**
	 * close
	 */
	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
			if (session != null) {
				session.close();
			}
			if (producer != null) {
				producer.close();
			}
		} catch (Exception ex) {
			facetimeLogger.error("", ex);
		} finally {
			connected = false;
			connection = null;
			session = null;
			producer = null;
		}
	}

	/**
	 * is save message
	 * 
	 * @param messateType
	 * @return
	 */
	public static boolean isSaveMessage(String type) {
		boolean save = false;
		if (MessageType.ChatMessage.equals(type) || MessageType.InviteBuddy.equals(type)
				|| MessageType.VideoInvite.equals(type) || MessageType.AudioInvite.equals(type)
				|| MessageType.VoiceMail.equals(type) || MessageType.InstantFile.equals(type)
				|| MessageType.ShareFileInPrivacy.equals(type) || MessageType.OfflineFile.equals(type)
				|| MessageType.SystemMsg.equals(type)) {
			save = true;
		}
		return save;
	}

	/**
	 * test
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "{[]}:;,<Hello? sdfsd sdfsd sdfsdf > dsfdsf sdf;?sdf ds' \"";

		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setMessageType(MessageType.ChatMessage);
		messageDTO.setSender(1L);
		messageDTO.setReceiver(2L);
		messageDTO.setMessageBody(str);

		// XStream xStream = new XStream();
		//
		// System.out.println("xstream : " + xStream.toXML(messageDTO));

		System.out.print(StringEscapeUtils.escapeXml(str));

		// System.exit(-1);

		System.out.println(getInstance().sendMessage(messageDTO));
	}

}
