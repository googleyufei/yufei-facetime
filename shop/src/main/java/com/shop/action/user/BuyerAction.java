package com.shop.action.user;

import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.action.Action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.shop.domain.user.Buyer;
import com.shop.logic.bean.CustomerBean;
import com.shop.logic.user.BuyerLogic;

@Controller
@SessionAttributes("user")
public class BuyerAction extends Action {

	private Logger log = Logger.getLogger(getClass());

	@RequestMapping("/user/reg/isUserExist")
	public String isUserExsit(@RequestParam("username") String username, ModelMap model) throws Exception {
		model.addAttribute("exist", this.locate(BuyerLogic.class).isUserExisted(username));
		return "user/checkuser";
	}

	@RequestMapping("/user/login")
	public String login(CustomerBean bean, ModelMap model) throws Exception {
		if (CheckUtil.isValid(bean.getUsername()) && CheckUtil.isValid(bean.getPassword())) {
			if (this.locate(BuyerLogic.class).check(bean.getUsername(), bean.getPassword())) {
				model.addAttribute("user", this.locate(BuyerLogic.class).get(bean.getUsername(), bean.getPassword()));
				String url = "/customer/shopping/deliver.do";
				if (CheckUtil.isValid(bean.getDirectUrl())) {
					url = bean.getDirectUrl();
				}
				model.addAttribute("directUrl", url);
				return "forward:" + url;
			}
			model.addAttribute("username", bean.getUsername());
			model.addAttribute("message", "用户名或密码错误!");
		}
		return "user/logon";
	}

	@RequestMapping("/user/logout")
	public String logout(CustomerBean bean, ModelMap model, @ModelAttribute("user") Buyer buyer) throws Exception {
		model.clear();
		return "user/logon";
	}

	@RequestMapping("/user/reg")
	public String reg(CustomerBean bean, ModelMap model) throws Exception {
		log.debug("username:" + bean.getUsername());
		if (this.locate(BuyerLogic.class).isUserExisted(bean.getUsername())) {
			model.addAttribute("message", "用户已经存在");
			return "share/message";
		}
		Buyer cus = new Buyer();
		cus.setUsername(bean.getUsername());
		cus.setEmail(bean.getEmail());
		cus.setPassword(bean.getPassword());
		this.locate(BuyerLogic.class).save(cus);

		model.addAttribute("user", cus);
		model.addAttribute("message", "用户注册成功");
		String url = "/customer/shopping/deliver.do";
		if (CheckUtil.isValid(bean.getDirectUrl())) {
			url = bean.getDirectUrl();
		}
		model.addAttribute("urladdress", url);
		return "share/message";
	}

	@RequestMapping("/user/regUI")
	public String regUI(ModelMap model) {
		model.addAttribute("directUrl", "/shop/user/login.do");
		return "user/userReg";
	}
}
