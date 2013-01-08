package com.shop.logic.bean;

import com.facetime.core.security.Base64;

import com.shop.domain.book.DeliverWay;
import com.shop.domain.book.PaymentWay;
import com.shop.domain.user.Gender;

public class DeliverBean extends BaseBean {
	private static final long serialVersionUID = 7555196853891533969L;
	private String recipients;
	private Gender gender;
	private String address;
	private String email;
	private String postalcode;
	private String tel;
	private String mobile;
	private Boolean buyerIsrecipients;

	private String buyer;
	private Gender buyer_gender;
	private String buyer_address;
	private String buyer_postalcode;
	private String buyer_mobile;
	private String buyer_tel;

	private DeliverWay deliverway;
	private PaymentWay paymentway;
	private String requirement;
	private String delivernote;

	private String directUrl;
	private String note;

	public String getAddress() {
		return address;
	}

	public String getBuyer() {
		return buyer;
	}

	public String getBuyer_address() {
		return buyer_address;
	}

	public Gender getBuyer_gender() {
		return buyer_gender;
	}

	public String getBuyer_mobile() {
		return buyer_mobile;
	}

	public String getBuyer_postalcode() {
		return buyer_postalcode;
	}

	public String getBuyer_tel() {
		return buyer_tel;
	}

	public Boolean getBuyerIsrecipients() {
		return buyerIsrecipients;
	}

	public String getDelivernote() {
		return delivernote;
	}

	public DeliverWay getDeliverway() {
		return deliverway;
	}

	public String getDirectUrl() {
		return directUrl;
	}

	public String getEmail() {
		return email;
	}

	public Gender getGender() {
		return gender;
	}

	public String getMobile() {
		return mobile;
	}

	public String getNote() {
		return note;
	}

	public PaymentWay getPaymentway() {
		return paymentway;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public String getRecipients() {
		return recipients;
	}

	public String getRequirement() {
		return requirement;
	}

	public String getTel() {
		return tel;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public void setBuyer_address(String buyer_address) {
		this.buyer_address = buyer_address;
	}

	public void setBuyer_gender(Gender buyer_gender) {
		this.buyer_gender = buyer_gender;
	}

	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}

	public void setBuyer_postalcode(String buyer_postalcode) {
		this.buyer_postalcode = buyer_postalcode;
	}

	public void setBuyer_tel(String buyer_tel) {
		this.buyer_tel = buyer_tel;
	}

	public void setBuyerIsrecipients(Boolean buyerIsrecipients) {
		this.buyerIsrecipients = buyerIsrecipients;
	}

	public void setDelivernote(String delivernote) {
		this.delivernote = delivernote;
	}

	public void setDeliverway(DeliverWay deliverway) {
		this.deliverway = deliverway;
	}

	public void setDirectUrl(String directUrl) {
		if (directUrl != null && !"".equals(directUrl.trim())) {
			this.directUrl = new String(Base64.decode(directUrl.trim()
					.getBytes()));// ��ȡ������url
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setPaymentway(PaymentWay paymentway) {
		this.paymentway = paymentway;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
