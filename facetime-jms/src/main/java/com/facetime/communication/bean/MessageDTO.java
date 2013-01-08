package com.facetime.communication.bean;

import com.facetime.core.bean.BusinessBean;

import java.util.Date;

/**
 * 客服端信息
 * 
 * @author PeterGuo
 */
public class MessageDTO implements BusinessBean {
	private static final long serialVersionUID = 1L;

	/**
	 * 一次会话的ID
	 */
	public long chatId;
	/**
	 * 聊天记录ID
	 */
	public String messageId;
	/**
	 * 消息类型，决定消息的处理方式
	 */
	public String messageType;
	/**
	 * 发送者 ID
	 */
	public Long sender;
	/**
	 * 接收者 ID
	 */
	public long receiver;
	/**
	 * 发送时间
	 */
	public Date sendDate = new Date();
	/**
	 * 消息内容
	 */
	public String messageBody;
	/**
	 * 消息语言
	 */
	public String language;
	/**
	 * 当是群消息时，群ID
	 */
	public Long groupId;
	/**
	 * 发送者名称
	 */
	public String fromUser;
	/**
	 * 接收者名称
	 */
	public String receiverName;
	/**
	 * 消息状态
	 */
	public String status = MessageStatus.NEW.name();
	/**
	 * 消息是否已显示
	 */
	private boolean showed = false;
	/**
	 * 消息是否已确认
	 */
	private boolean confirmed = false;
	/**
	 * 消息是否正显示
	 */
	private boolean displaying = false;

	public MessageDTO() {
	}

	public MessageDTO(long chatId, String messageId, String messageType, Long sender, long receiver, Date sendDate,
			String messageBody, String language, Long groupId, String fromUser) {
		super();
		this.chatId = chatId;
		this.messageId = messageId;
		this.messageType = messageType;
		this.sender = sender;
		this.receiver = receiver;
		this.sendDate = sendDate;
		this.messageBody = messageBody;
		this.language = language;
		this.groupId = groupId;
		this.fromUser = fromUser;
	}

	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Long getSender() {
		return sender;
	}

	public void setSender(Long sender) {
		this.sender = sender;
	}

	public long getReceiver() {
		return receiver;
	}

	public void setReceiver(long receiver) {
		this.receiver = receiver;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public boolean isShowed() {
		return showed;
	}

	public void setShowed(boolean showed) {
		this.showed = showed;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	/**
	 * @param confirmed
	 *            the confirmed to set
	 */

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public boolean isDisplaying() {
		return displaying;
	}

	public void setDisplaying(boolean displaying) {
		this.displaying = displaying;
	}
}
