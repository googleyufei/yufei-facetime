package com.shop.action.shopping;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.domain.BuyCart;
import com.shop.domain.user.Buyer;
import com.shop.domain.user.Gender;
import com.shop.logic.bean.DeliverBean;
import com.shop.util.WebUtil;

/**
 * 用于用户输入配送地址
 */
@Controller
public class DeliverInfoAction {

	@RequestMapping("/customer/shopping/deliver.do")
	public String deliver(DeliverBean deliverInfo, HttpServletRequest request) {
		deliverInfo.setGender(Gender.MAN);
		deliverInfo.setBuyer_gender(Gender.MAN);
		deliverInfo.setEmail(WebUtil.getBuyer(request).getEmail());
		deliverInfo.setBuyerIsrecipients(true);
		BuyCart cart = WebUtil.getBuyCart(request);
		Buyer buyer = WebUtil.getBuyer(request);
		if (cart.getDeliverInfo() != null) {
			deliverInfo.setRecipients(cart.getDeliverInfo().getRecipients());
			deliverInfo.setGender(cart.getDeliverInfo().getGender());
			deliverInfo.setAddress(cart.getDeliverInfo().getAddress());
			deliverInfo.setPostalcode(cart.getDeliverInfo().getPostalcode());
			deliverInfo.setTel(cart.getDeliverInfo().getTel());
			deliverInfo.setMobile(cart.getDeliverInfo().getMobile());
			deliverInfo.setEmail(cart.getDeliverInfo().getEmail());
		} else {
			if (buyer.getContactInfo() != null) {
				deliverInfo.setRecipients(buyer.getRealname());
				deliverInfo.setGender(buyer.getGender());
				deliverInfo.setAddress(buyer.getContactInfo().getAddress());
				deliverInfo.setPostalcode(buyer.getContactInfo().getPostalcode());
				deliverInfo.setTel(buyer.getContactInfo().getPhone());
				deliverInfo.setMobile(buyer.getContactInfo().getMobile());
			}
		}
		if (cart.getBuyerIsrecipients() != null) {
			deliverInfo.setBuyerIsrecipients(cart.getBuyerIsrecipients());
		}

		if (cart.getContactInfo() != null) {
			deliverInfo.setBuyer(cart.getContactInfo().getBuyerName());
			deliverInfo.setBuyer_gender(cart.getContactInfo().getGender());
			deliverInfo.setBuyer_address(cart.getContactInfo().getAddress());
			deliverInfo.setBuyer_postalcode(cart.getContactInfo().getPostalcode());
			deliverInfo.setBuyer_tel(cart.getContactInfo().getTel());
			deliverInfo.setBuyer_mobile(cart.getContactInfo().getMobile());
		} else {
			if (buyer.getContactInfo() != null) {
				deliverInfo.setBuyer(buyer.getRealname());
				deliverInfo.setBuyer_gender(buyer.getGender());
				deliverInfo.setBuyer_address(buyer.getContactInfo().getAddress());
				deliverInfo.setBuyer_postalcode(buyer.getContactInfo().getPostalcode());
				deliverInfo.setBuyer_tel(buyer.getContactInfo().getPhone());
				deliverInfo.setBuyer_mobile(buyer.getContactInfo().getMobile());
			}
		}
		return "shopping/deliverInfo";
	}
}
