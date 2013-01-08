package com.shop.action.manage;

import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.PageBy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.action.privilege.Permission;
import com.shop.domain.PageView;
import com.shop.domain.user.Buyer;
import com.shop.logic.bean.BuyerBean;
import com.shop.logic.user.BuyerLogic;
import com.shop.util.SiteUrl;

@Controller
public class BuyerManageAction extends Action {

	@RequestMapping("/control/user/delete")
	@Permission(module = "buyer", privilege = "delete")
	public String delete(BuyerBean bean, ModelMap model) throws Exception {
		this.locate(BuyerLogic.class).disable(bean.getUsernames());
		model.addAttribute("message", "网站用户禁用成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.user.list"));
		return "share/message";
	}

	@RequestMapping("/control/user/enable")
	@Permission(module = "buyer", privilege = "enable")
	public String enable(BuyerBean bean, ModelMap model) throws Exception {
		this.locate(BuyerLogic.class).enable(bean.getUsernames());
		model.addAttribute("message", "网站用户启用成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.user.list"));
		return "share/message";
	}

	@RequestMapping("/control/user/list")
	@Permission(module = "buyer", privilege = "view")
	public String list(BuyerBean bean, ModelMap model) throws Exception {
		PageView<Buyer> pageView = new PageView<Buyer>(10, bean.getPage());
		pageView.setQueryResult(this.locate(BuyerLogic.class).getQueryResult(bean,
				new PageBy(pageView.getCurrentpage(), pageView.getMaxresult())));
		model.addAttribute("pageView", pageView);
		return "user/userlist";
	}

	@RequestMapping("/control/user/query")
	@Permission(module = "buyer", privilege = "view")
	public String queryUI() {
		return "user/query_user";
	}
}
