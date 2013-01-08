package com.facetime.communication.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * message list
 * 
 * @author yang
 */
public class MessagesDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long count;

	private List<MessageDTO> messageDTOList = new ArrayList<MessageDTO>();

	public MessagesDTO() {
		super();
	}

	public MessagesDTO(List<MessageDTO> messageDTOList) {
		super();
		this.messageDTOList = messageDTOList;
		count = messageDTOList.size();
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public void setMessageDTOList(List<MessageDTO> messagesDTOs) {
		this.messageDTOList = messagesDTOs;
	}

	public List<MessageDTO> getMessageDTOList() {
		return messageDTOList;
	}

}
