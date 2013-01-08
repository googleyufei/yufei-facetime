package com.shop.logic.bean;

import com.shop.domain.book.PaymentWay;

public class ShoppingFinishBean extends BaseBean {
	private static final long serialVersionUID = -2700804121186900697L;

	private PaymentWay paymentway;
	private String orderid;
	private Float payablefee;

	public String getOrderid() {
		return orderid;
	}

	public Float getPayablefee() {
		return payablefee;
	}

	public PaymentWay getPaymentway() {
		return paymentway;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public void setPayablefee(Float payablefee) {
		this.payablefee = payablefee;
	}

	public void setPaymentway(PaymentWay paymentway) {
		this.paymentway = paymentway;
	}
}
