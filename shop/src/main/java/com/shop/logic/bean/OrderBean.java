package com.shop.logic.bean;

import com.shop.domain.book.DeliverWay;
import com.shop.domain.book.OrderState;
import com.shop.domain.book.PaymentWay;
import com.shop.domain.user.Gender;

public class OrderBean extends BaseBean {

	private String orderid;
	private String username;
	private OrderState state;
	private String recipients;
	private String buyer;
	private Integer contactid;

	private Gender buyer_gender;
	private String buyer_address;
	private String buyer_postalcode;
	private String buyer_tel;
	private String buyer_mobile;

	private Gender gender;
	private String address;
	private String postalcode;
	private String email;
	private String tel;
	private String mobile;

	private Integer deliverid;
	private PaymentWay paymentWay;
	private DeliverWay deliverWay;

	private Integer amount;
	private Integer orderitemid;
	private float fee;

	private String[] orderids;
	private String message;

	private static final long serialVersionUID = -5143612917098697434L;

	public String getAddress() {
		return address;
	}

	public Integer getAmount() {
		return amount;
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

	public Integer getContactid() {
		return contactid;
	}

	public Integer getDeliverid() {
		return deliverid;
	}

	public DeliverWay getDeliverWay() {
		return deliverWay;
	}

	public String getEmail() {
		return email;
	}

	public float getFee() {
		return fee;
	}

	public Gender getGender() {
		return gender;
	}

	public String getMessage() {
		return message;
	}

	public String getMobile() {
		return mobile;
	}

	public String getOrderid() {
		return orderid;
	}

	public String[] getOrderids() {
		return orderids;
	}

	public Integer getOrderitemid() {
		return orderitemid;
	}

	public PaymentWay getPaymentWay() {
		return paymentWay;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public String getRecipients() {
		return recipients;
	}

	public OrderState getState() {
		return state;
	}

	public String getTel() {
		return tel;
	}

	public String getUsername() {
		return username;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
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

	public void setContactid(Integer contactid) {
		this.contactid = contactid;
	}

	public void setDeliverid(Integer deliverid) {
		this.deliverid = deliverid;
	}

	public void setDeliverWay(DeliverWay deliverWay) {
		this.deliverWay = deliverWay;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFee(float fee) {
		this.fee = fee;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public void setOrderids(String[] orderids) {
		this.orderids = orderids;
	}

	public void setOrderitemid(Integer orderitemid) {
		this.orderitemid = orderitemid;
	}

	public void setPaymentWay(PaymentWay paymentWay) {
		this.paymentWay = paymentWay;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
