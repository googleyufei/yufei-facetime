package com.shop.action.order;

import com.facetime.spring.action.Action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.action.privilege.Permission;
import com.shop.domain.book.Order;
import com.shop.domain.book.OrderContactInfo;
import com.shop.domain.book.OrderDeliverInfo;
import com.shop.domain.book.OrderItem;
import com.shop.logic.OrderLogic;
import com.shop.logic.bean.OrderBean;
import com.shop.util.SiteUrl;

@Controller
public class OrderModifyAction extends Action {

	@RequestMapping("/control/order/deleteOrderItem")
	@Permission(module = "order", privilege = "deleteOrderItem")
	public String deleteOrderItem(OrderBean bean, ModelMap model) throws Exception {
		this.defaultLogic.deleteById(OrderItem.class, bean.getOrderitemid());
		model.addAttribute("message", "删除订单项成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.view") + "?orderid=" + bean.getOrderid());
		return "share/message";
	}

	@RequestMapping("/control/order/modifyContactInfo")
	@Permission(module = "order", privilege = "modifyContactInfo")
	public String modifyContactInfo(OrderBean bean, ModelMap model) throws Exception {
		OrderContactInfo contact = this.getDefaultLogic().findById(OrderContactInfo.class, bean.getContactid());
		contact.setBuyerName(bean.getBuyer());
		contact.setGender(bean.getBuyer_gender());
		contact.setAddress(bean.getBuyer_address());
		contact.setMobile(bean.getBuyer_mobile());
		contact.setPostalcode(bean.getBuyer_postalcode());
		contact.setTel(bean.getBuyer_tel());
		this.getDefaultLogic().update(contact);
		model.addAttribute("message", "更新订单合同信息成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.view") + "?orderid="
				+ contact.getOrder().getOrderid());
		return "share/message";
	}

	@RequestMapping("/control/order/modifyContactInfoUI")
	@Permission(module = "order", privilege = "modifyContactInfo")
	public String modifyContactInfoUI(OrderBean bean, ModelMap model) throws Exception {
		OrderContactInfo contact = this.getDefaultLogic().findById(OrderContactInfo.class, bean.getContactid());
		model.addAttribute("contactInfo", contact);
		return "order/modifyContactInfo";
	}

	@RequestMapping("/control/order/modifyDeliverFee")
	@Permission(module = "order", privilege = "modifyDeliverFee")
	public String modifyDeliverFee(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).updateDeliverFee(bean.getOrderid(), bean.getFee());
		model.addAttribute("message", "配送费修改成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.view") + "?orderid=" + bean.getOrderid());
		return "share/message";
	}

	@RequestMapping("/control/order/modifyDeliverFeeUI")
	@Permission(module = "order", privilege = "modifyDeliverFee")
	public String modifyDeliverFeeUI(OrderBean bean, ModelMap model) throws Exception {
		Order order = this.getDefaultLogic().findById(Order.class, bean.getOrderid());
		model.addAttribute("fee", order.getDeliverFee());
		return "order/modifydeliverFee";
	}

	@RequestMapping("/control/order/modifyDeliverInfo")
	@Permission(module = "order", privilege = "modifyDeliverInfo")
	public String modifyDeliverInfo(OrderBean bean, ModelMap model) throws Exception {
		OrderDeliverInfo deliverInfo = this.defaultLogic.findById(OrderDeliverInfo.class, bean.getDeliverid());
		deliverInfo.setEmail(bean.getEmail());
		deliverInfo.setRecipients(bean.getRecipients());
		deliverInfo.setGender(bean.getGender());
		deliverInfo.setAddress(bean.getAddress());
		deliverInfo.setPostalcode(bean.getPostalcode());
		deliverInfo.setTel(bean.getTel());
		deliverInfo.setMobile(bean.getMobile());
		this.getDefaultLogic().update(deliverInfo);
		model.addAttribute("message", "订单配送信息修改成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.view") + "?orderid="
				+ deliverInfo.getOrder().getOrderid());
		return "share/message";
	}

	@RequestMapping("/control/order/modifyDeliverInfoUI")
	@Permission(module = "order", privilege = "modifyDeliverInfo")
	public String modifyDeliverInfoUI(OrderBean bean, ModelMap model) throws Exception {
		OrderDeliverInfo deliverInfo = this.defaultLogic.findById(OrderDeliverInfo.class, bean.getDeliverid());
		model.addAttribute("deliverInfo", deliverInfo);
		return "order/modifyDeliverInfo";
	}

	@RequestMapping("/control/order/modifyDeliverWay")
	@Permission(module = "order", privilege = "modifyDeliverWay")
	public String modifyDeliverWay(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).updateDeliverWay(bean.getOrderid(), bean.getDeliverWay());
		model.addAttribute("message", "配送方式修改成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.view") + "?orderid=" + bean.getOrderid());
		return "share/message";
	}

	@RequestMapping("/control/order/modifyDeliverWayUI")
	@Permission(module = "order", privilege = "modifyDeliverWay")
	public String modifyDeliverWayUI(OrderBean bean) throws Exception {
		Order order = this.defaultLogic.findById(Order.class, bean.getOrderid());
		bean.setDeliverWay(order.getOrderDeliverInfo().getDeliverWay());
		bean.setPaymentWay(order.getPaymentWay());
		return "order/modifyDeliverWay";
	}

	@RequestMapping("/control/order/modifyPaymentWay")
	@Permission(module = "order", privilege = "modifyPaymentWay")
	public String modifyPaymentWay(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).updatePaymentWay(bean.getOrderid(), bean.getPaymentWay());
		model.addAttribute("message", "支付方式修改成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.view") + "?orderid=" + bean.getOrderid());
		return "share/message";
	}

	@RequestMapping("/control/order/modifyPaymentWayUI")
	@Permission(module = "order", privilege = "modifyPaymentWay")
	public String modifyPaymentWayUI(OrderBean bean, ModelMap model) throws Exception {
		Order order = this.defaultLogic.findById(Order.class, bean.getOrderid());
		bean.setPaymentWay(order.getPaymentWay());
		model.addAttribute("orderBean", bean);
		model.addAttribute("deliverWay", order.getOrderDeliverInfo().getDeliverWay());
		return "order/modifyPaymentWay";
	}

	@RequestMapping("/control/order/modifyProductAmount")
	@Permission(module = "order", privilege = "modifyProductAmount")
	public String modifyProductAmount(OrderBean bean, ModelMap model) throws Exception {
		OrderItem item = this.defaultLogic.findById(OrderItem.class, bean.getOrderitemid());
		item.setAmount(bean.getAmount());
		this.defaultLogic.update(item);
		model.addAttribute("message", "商品购买数量修改成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.view") + "?orderid=" + bean.getOrderid());
		return "share/message";
	}

	@RequestMapping("/control/order/modifyProductAmountUI")
	@Permission(module = "order", privilege = "modifyProductAmount")
	public String modifyProductAmountUI(OrderBean bean, ModelMap model) throws Exception {
		OrderItem item = this.defaultLogic.findById(OrderItem.class, bean.getOrderitemid());
		model.addAttribute("amount", item.getAmount());
		return "order/modifyProductAmount";
	}
}
