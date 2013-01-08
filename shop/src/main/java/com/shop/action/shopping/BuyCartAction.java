package com.shop.action.shopping;

import com.facetime.core.security.Base64;
import com.facetime.core.utils.CheckUtil;
import com.facetime.spring.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.domain.BuyCart;
import com.shop.domain.BuyItem;
import com.shop.domain.product.ProductInfo;
import com.shop.domain.product.ProductStyle;
import com.shop.logic.bean.BuyCartBean;
import com.shop.util.WebUtil;

@Controller
public class BuyCartAction extends Action {
	@RequestMapping("/shopping/cart")
	public String cart(BuyCartBean bean, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BuyCart cart = WebUtil.getBuyCart(request);
		if (cart == null) {
			cart = new BuyCart();
			request.getSession().setAttribute("buyCart", cart);
		}
		WebUtil.addCookie(response, "JSESSIONID", request.getSession().getId(),
				request.getSession().getMaxInactiveInterval());
		if (CheckUtil.isValid(bean.getProductid())) {
			ProductInfo product = this.defaultLogic.findById(ProductInfo.class,
					bean.getProductid());
			if (product != null) {
				ProductStyle currentStyle = null;
				for (ProductStyle style : product.getStyles()) {
					if (style.getId().equals(bean.getStyleid())) {
						currentStyle = style;
						break;
					}
				}
				product.getStyles().clear();
				product.addProductStyle(currentStyle);
			}
			BuyItem item = new BuyItem(product);
			cart.addBuyItem(item);
		}
		return "shopping/cart";
	}

	@RequestMapping("/shopping/cart/delete")
	public String delete(BuyCartBean bean, HttpServletRequest request)
			throws Exception {
		BuyCart cart = WebUtil.getBuyCart(request);
		ProductInfo product = new ProductInfo(bean.getProductid());
		product.addProductStyle(new ProductStyle(bean.getStyleid()));
		BuyItem item = new BuyItem(product);
		cart.deleteBuyItem(item);
		String param = CheckUtil.isValid(bean.getDirectUrl()) ? "?directUrl="
				+ bean.getDirectUrl() : "";
		request.setAttribute("directUrl", "/shopping/cart.do" + param);
		return "share/directUrl";
	}

	@RequestMapping("/shopping/cart/deleteAll")
	public String deleteAll(BuyCartBean bean, HttpServletRequest request)
			throws Exception {
		BuyCart cart = WebUtil.getBuyCart(request);
		cart.deleteAll();
		String param = CheckUtil.isValid(bean.getDirectUrl()) ? "?directUrl="
				+ bean.getDirectUrl() : "";
		request.setAttribute("directUrl", "/shopping/cart.do" + param);
		return "share/directUrl";
	}

	@RequestMapping("/shopping/cart/settleAccounts")
	public String settleAccounts(BuyCartBean bean, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		setAmount(request);
		String url = "/customer/shopping/deliver.do";
		if (CheckUtil.isValid(bean.getDirectUrl())) {
			url = new String(Base64.decode(bean.getDirectUrl().trim()
					.getBytes()));
		}
		request.setAttribute("directUrl", url);
		return "share/directUrl";
	}

	@RequestMapping("/shopping/cart/updateAmount")
	public String updateAmount(BuyCartBean bean, HttpServletRequest request)
			throws Exception {
		setAmount(request);
		String param = CheckUtil.isValid(bean.getDirectUrl()) ? "?directUrl="
				+ bean.getDirectUrl() : "";
		request.setAttribute("directUrl", "/shopping/cart.do" + param);
		return "share/directUrl";
	}

	private void setAmount(HttpServletRequest request) {
		BuyCart cart = WebUtil.getBuyCart(request);
		for (BuyItem item : cart.getItems()) {
			String paramName = "amount_" + item.getProduct().getId() + "_"
					+ item.getProduct().getStyles().iterator().next().getId();
			Integer amount = new Integer(request.getParameter(paramName));
			item.setAmount(amount);
		}
	}
}
