package com.shop.logic;

import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.Page;

import com.shop.domain.BuyCart;
import com.shop.domain.book.DeliverWay;
import com.shop.domain.book.Order;
import com.shop.domain.book.PaymentWay;
import com.shop.logic.bean.OrderBean;

/**
 * used to process order about logic.
 */
public interface OrderLogic extends Logic {

	Order addLock(String orderid, String username);

	void cannelOrder(String orderid);

	void confirmOrder(String orderid);

	void confirmPayment(String orderid);

	Order createOrder(BuyCart buyCart, String username);

	Page<Order> getQueryResult(OrderBean orderBean, PageBy bound);

	void turnDelivered(String orderid);

	void turnReceived(String orderid);

	void turnWaitdeliver(String orderid);

	void unlock(String... orderids);

	void updateDeliverFee(String orderid, float deliverFee);

	void updateDeliverWay(String orderid, DeliverWay deliverWay);

	void updatePaymentWay(String orderid, PaymentWay paymentWay);

}
