package com.shop.logic;

import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.BuyCart;
import com.shop.domain.BuyItem;
import com.shop.domain.book.DeliverWay;
import com.shop.domain.book.GeneratedOrderid;
import com.shop.domain.book.Order;
import com.shop.domain.book.OrderItem;
import com.shop.domain.book.OrderState;
import com.shop.domain.book.PaymentWay;
import com.shop.domain.product.ProductStyle;
import com.shop.domain.user.Buyer;
import com.shop.domain.user.ContactInfo;
import com.shop.logic.bean.OrderBean;

@Service
@Transactional
public class OrderLogicImpl extends LogicImpl implements OrderLogic {

	@Override
	public Order addLock(String orderid, String username) {
		Order order = findById(Order.class, orderid);
		order.setLockuser(username);
		this.update(order);
		return order;
	}

	@Override
	public void cannelOrder(String orderid) {
		Order order = findById(Order.class, orderid);
		if (!OrderState.RECEIVED.equals(order.getState()))
			order.setState(OrderState.CANCEL);
		order.setLockuser(null);
	}

	@Override
	public void confirmOrder(String orderid) {
		Order order = findById(Order.class, orderid);
		if (OrderState.WAITCONFIRM.equals(order.getState()))
			if (!PaymentWay.COD.equals(order.getPaymentWay()) && !order.getPaymentstate())
				order.setState(OrderState.WAITPAYMENT);
			else
				order.setState(OrderState.ADMEASUREPRODUCT);
		order.setLockuser(null);
	}

	@Override
	public void confirmPayment(String orderid) {
		Order order = findById(Order.class, orderid);
		order.setPaymentstate(true);
		if (OrderState.WAITPAYMENT.equals(order.getState()))
			order.setState(OrderState.ADMEASUREPRODUCT);
		else if (OrderState.DELIVERED.equals(order.getState()) && PaymentWay.COD.equals(order.getPaymentWay()))
			order.setState(OrderState.RECEIVED);
	}

	@Override
	public Order createOrder(BuyCart buyCart, String username) {
		Order order = new Order();
		Buyer buyer = findById(Buyer.class, username);
		order.setBuyer(buyer);
		order.setDeliverFee(buyCart.getDeliveFee());
		order.setNote(buyCart.getNote());
		order.setOrderContactInfo(buyCart.getContactInfo());
		order.setOrderDeliverInfo(buyCart.getDeliverInfo());
		order.setState(OrderState.WAITCONFIRM);
		order.setPaymentWay(buyCart.getPaymentWay());
		order.setProductTotalPrice(buyCart.getTotalSellPrice());
		order.setTotalPrice(buyCart.getOrderTotalPrice());
		order.setPayablefee(buyCart.getOrderTotalPrice());
		for (BuyItem item : buyCart.getItems()) {
			ProductStyle style = item.getProduct().getStyles().iterator().next();
			OrderItem oitem = new OrderItem(item.getProduct().getName(), item.getProduct().getId(), item.getProduct()
					.getSellprice(), item.getAmount(), style.getName(), style.getId());
			order.addOrderItem(oitem);
		}
		if (buyer.getContactInfo() == null) {
			buyer.setContactInfo(new ContactInfo());
			buyer.getContactInfo().setAddress(order.getOrderContactInfo().getAddress());
			buyer.getContactInfo().setPostalcode(order.getOrderContactInfo().getPostalcode());
			buyer.getContactInfo().setPhone(order.getOrderContactInfo().getTel());
			buyer.getContactInfo().setMobile(order.getOrderContactInfo().getMobile());
			if (buyer.getRealname() == null)
				buyer.setRealname(order.getOrderContactInfo().getBuyerName());
			if (buyer.getGender() == null)
				buyer.setGender(order.getOrderContactInfo().getGender());
		}
		order.setOrderid(buildOrderid2(order.getCreateDate()));
		this.save(order);
		return order;
	}

	@Override
	public Page<Order> getQueryResult(OrderBean orderBean, PageBy bound) {
		List<QueryFilter> filters = LogicUtils.filterList();
		if (CheckUtil.isValid(orderBean.getOrderid()))
			filters.add(LogicUtils.filterby("orderid", PMLO.LIKE, "%" + orderBean.getOrderid().trim() + "%"));
		if (orderBean.getState() != null)
			filters.add(LogicUtils.filterby("state", orderBean.getState()));
		if (CheckUtil.isValid(orderBean.getUsername()))
			filters.add(LogicUtils.filterby("buyer.username", PMLO.LIKE, "%" + orderBean.getUsername().trim() + "%"));
		if (CheckUtil.isValid(orderBean.getRecipients()))
			filters.add(LogicUtils.filterby("orderDeliverInfo.recipients", PMLO.LIKE, orderBean.getRecipients().trim()));
		if (CheckUtil.isValid(orderBean.getBuyer()))
			filters.add(LogicUtils.filterby("orderContactInfo.buyerName", PMLO.LIKE, orderBean.getBuyer().trim()));
		return this.findPage(Order.class, LogicUtils.toArray(filters), bound);
	}

	@Override
	public void turnDelivered(String orderid) {
		Order order = findById(Order.class, orderid);
		if (OrderState.WAITDELIVER.equals(order.getState()))
			order.setState(OrderState.DELIVERED);
	}

	@Override
	public void turnReceived(String orderid) {
		Order order = findById(Order.class, orderid);
		if (OrderState.DELIVERED.equals(order.getState()))
			order.setState(OrderState.RECEIVED);
	}

	@Override
	public void turnWaitdeliver(String orderid) {
		Order order = findById(Order.class, orderid);
		if (OrderState.ADMEASUREPRODUCT.equals(order.getState()))
			order.setState(OrderState.WAITDELIVER);
	}

	@Override
	public void unlock(String... orderids) {
		if (CheckUtil.notValid(orderids))
			return;
		List<Order> list = findByIds(Order.class, orderids);
		for (Order order : list)
			order.setLockuser(null);
		update(list);
	}

	@Override
	public void updateDeliverFee(String orderid, float deliverFee) {
		Order order = findById(Order.class, orderid);
		order.setDeliverFee(deliverFee);
		order.setTotalPrice(order.getProductTotalPrice() + order.getDeliverFee());
		order.setPayablefee(order.getTotalPrice());
	}

	@Override
	public void updateDeliverWay(String orderid, DeliverWay deliverWay) {
		Order order = findById(Order.class, orderid);
		order.getOrderDeliverInfo().setDeliverWay(deliverWay);
	}

	@Override
	public void updatePaymentWay(String orderid, PaymentWay paymentWay) {
		Order order = findById(Order.class, orderid);
		order.setPaymentWay(paymentWay);
		update(order, "paymentWay");
	}

	private int buildOrderid() {
		this.update("update GeneratedOrderid o set o.orderid=orderid+1 where o.id=? ", new Object[] { "order" });
		GeneratedOrderid go = findById(GeneratedOrderid.class, "order");
		return go.getOrderid();
	}

	private String buildOrderid2(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
		StringBuilder sb = new StringBuilder(dateFormat.format(date));
		sb.append(fillZero(8, String.valueOf(buildOrderid())));
		return sb.toString();
	}

	private String fillZero(int length, String source) {
		StringBuilder result = new StringBuilder(source);
		for (int i = result.length(); i < length; i++)
			result.insert(0, '0');
		return result.toString();
	}
}
