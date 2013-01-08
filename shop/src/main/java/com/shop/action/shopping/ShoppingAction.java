package com.shop.action.shopping;

import com.facetime.core.security.Base64;
import com.facetime.spring.action.Action;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.domain.BuyCart;
import com.shop.domain.book.DeliverWay;
import com.shop.domain.book.Order;
import com.shop.domain.book.OrderContactInfo;
import com.shop.domain.book.OrderDeliverInfo;
import com.shop.domain.book.PaymentWay;
import com.shop.logic.OrderLogic;
import com.shop.logic.bean.DeliverBean;
import com.shop.logic.bean.ShoppingFinishBean;
import com.shop.util.WebUtil;

@Controller
public class ShoppingAction extends Action {

	@RequestMapping("/customer/shopping/confirm")
	public String confirm(HttpServletRequest request) throws Exception {
		String url = "/customer/shopping/confirm.do";
		request.setAttribute("directUrl",
				new String(Base64.encodeToByte(url.getBytes(), false)));
		return "shopping/confirm";
	}

	@RequestMapping("/customer/shopping/paymentway")
	public String execute(DeliverBean bean, HttpServletRequest request)
			throws Exception {
		BuyCart cart = WebUtil.getBuyCart(request);
		if (cart.getDeliverInfo() == null) {
			request.setAttribute("directUrl", "/customer/shopping/deliver.do");
			return "share/directUrl";
		}
		bean.setDeliverway(DeliverWay.EXPRESSDELIVERY);
		bean.setPaymentway(PaymentWay.NET);

		if (cart.getPaymentWay() != null) {
			bean.setPaymentway(cart.getPaymentWay());
		}
		if (cart.getDeliverInfo().getDeliverWay() != null) {
			bean.setDeliverway(cart.getDeliverInfo().getDeliverWay());
		}
		if (cart.getDeliverInfo().getRequirement() != null) {// 为了实现时间要求数据回显
			List<String> contents = Arrays
					.asList("工作日、双休日与假日均可送货", "只双休日、假日送货", "只工作日送货(双休日、假日不用送)",
							"学校地址/地址白天没人，请尽量安排其他时间送货");
			if (contents.contains(cart.getDeliverInfo().getRequirement())) {
				bean.setRequirement(cart.getDeliverInfo().getRequirement());
			} else {
				bean.setRequirement("other");
				bean.setDelivernote(cart.getDeliverInfo().getRequirement());
			}
		}
		return "shopping/paymentway";
	}

	@RequestMapping("/customer/shopping/saveDeliverInfo")
	public String saveDeliverInfo(DeliverBean bean, HttpServletRequest request)
			throws Exception {
		BuyCart cart = WebUtil.getBuyCart(request);
		if (cart.getDeliverInfo() == null) {
			cart.setDeliverInfo(new OrderDeliverInfo());
		}
		cart.getDeliverInfo().setRecipients(bean.getRecipients());
		cart.getDeliverInfo().setGender(bean.getGender());
		cart.getDeliverInfo().setAddress(bean.getAddress());
		cart.getDeliverInfo().setPostalcode(bean.getPostalcode());
		cart.getDeliverInfo().setEmail(bean.getEmail());
		cart.getDeliverInfo().setTel(bean.getTel());
		cart.getDeliverInfo().setMobile(bean.getMobile());
		cart.setBuyerIsrecipients(bean.getBuyerIsrecipients());
		if (cart.getContactInfo() == null) {
			cart.setContactInfo(new OrderContactInfo());
		}
		if (cart.getBuyerIsrecipients()) {
			cart.getContactInfo().setBuyerName(bean.getRecipients());
			cart.getContactInfo().setGender(bean.getGender());
			cart.getContactInfo().setAddress(bean.getAddress());
			cart.getContactInfo().setPostalcode(bean.getPostalcode());
			cart.getContactInfo().setTel(bean.getTel());
			cart.getContactInfo().setMobile(bean.getMobile());
			cart.getContactInfo().setEmail(bean.getEmail());
		} else {
			cart.getContactInfo().setBuyerName(bean.getBuyer());
			cart.getContactInfo().setGender(bean.getBuyer_gender());
			cart.getContactInfo().setAddress(bean.getBuyer_address());
			cart.getContactInfo().setPostalcode(bean.getBuyer_postalcode());
			cart.getContactInfo().setTel(bean.getBuyer_tel());
			cart.getContactInfo().setMobile(bean.getBuyer_mobile());
			cart.getContactInfo().setEmail(
					WebUtil.getBuyer(request).getEmail());
		}
		String url = "/customer/shopping/paymentway.do";
		if (bean.getDirectUrl() != null) {
			url = bean.getDirectUrl();
		}
		request.setAttribute("directUrl", url);
		return "share/directUrl";
	}

	@RequestMapping("/customer/shopping/saveOrder")
	public String saveorder(DeliverBean bean, HttpServletRequest request)
			throws Exception {
		BuyCart cart = WebUtil.getBuyCart(request);
		cart.setNote(bean.getNote());
		Order order = this.locate(OrderLogic.class).createOrder(cart,
				WebUtil.getBuyer(request).getUsername());
		WebUtil.deleteBuyCart(request);
		request.setAttribute("directUrl", "/shopping/finish.do?orderid="
				+ order.getOrderid() + "&paymentway=" + order.getPaymentWay()
				+ "&payablefee=" + order.getPayablefee());
		return "share/directUrl";
	}

	@RequestMapping("/customer/shopping/savePaymentway")
	public String savePaymentway(DeliverBean bean, HttpServletRequest request)
			throws Exception {
		BuyCart cart = WebUtil.getBuyCart(request);
		cart.getDeliverInfo().setDeliverWay(bean.getDeliverway());
		cart.setPaymentWay(bean.getPaymentway());
		if (DeliverWay.EXPRESSDELIVERY.equals(bean.getDeliverway())
				|| DeliverWay.EXIGENCEEXPRESSDELIVERY.equals(bean
						.getDeliverway())) {
			if ("other".equals(bean.getRequirement())) {
				cart.getDeliverInfo().setRequirement(bean.getDelivernote());
			} else {
				cart.getDeliverInfo().setRequirement(bean.getRequirement());
			}
		} else {
			cart.getDeliverInfo().setRequirement(null);
		}
		request.setAttribute("directUrl", "/customer/shopping/confirm.do");
		return "share/directUrl";
	}

	@RequestMapping("/shopping/finish")
	public String shoppingFinish(ShoppingFinishBean bean,
			HttpServletRequest request) throws Exception {
		String forwardName = "shopping/finish_postofficeremittance";
		if (PaymentWay.COD.equals(bean.getPaymentway())) {
			forwardName = "shopping/finish_cod";
		} else if (PaymentWay.NET.equals(bean.getPaymentway())) {
			forwardName = "shopping/finish_net";
		} else if (PaymentWay.BANKREMITTANCE.equals(bean.getPaymentway())) {
			forwardName = "shopping/finish_bankremittance";
		}
		return forwardName;
	}
}
