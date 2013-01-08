package com.shop.action.order;

import com.facetime.mgr.domain.SysUser;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.OrderBy;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.action.privilege.Permission;
import com.shop.domain.PageView;
import com.shop.domain.book.Message;
import com.shop.domain.book.Order;
import com.shop.domain.book.OrderState;
import com.shop.logic.OrderLogic;
import com.shop.logic.bean.OrderBean;
import com.shop.util.SiteUrl;

@Controller
public class OrderAction extends Action {

	@RequestMapping("/control/order/addMessage")
	@Permission(module = "order", privilege = "view")
	public String addMessage(OrderBean bean, ModelMap model, HttpSession session) throws Exception {
		SysUser employee = (SysUser) session.getAttribute("employee");
		Message msg = new Message();
		msg.setContent(bean.getMessage());
		msg.setUsername(employee.getUsername());
		msg.setOrder(new Order(bean.getOrderid()));
		this.getDefaultLogic().save(msg);

		model.addAttribute("message", "添加消息成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.view") + "?orderid=" + bean.getOrderid());
		return "share/message";
	}

	@RequestMapping("/control/order/addMessageUI")
	@Permission(module = "order", privilege = "view")
	public String addMessageUI() throws Exception {
		return "order/ordermessage";
	}

	@RequestMapping("/control/order/allUnlock")
	@Permission(module = "order", privilege = "allUnLock")
	public String allUnLock(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).unlock(bean.getOrderids());
		model.addAttribute("message", "订单全部解锁成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("contorl.lockorder.list"));
		return "share/message";
	}

	@RequestMapping("/control/order/cancelOrder")
	@Permission(module = "order", privilege = "cancelOrder")
	public String cancelOrder(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).cannelOrder(bean.getOrderid());
		model.addAttribute("message", "取消订单成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.list"));
		return "share/message";
	}

	@RequestMapping("/control/order/confirmOrder")
	@Permission(module = "order", privilege = "confirmOrder")
	public String confirmOrder(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).confirmOrder(bean.getOrderid());
		model.addAttribute("message", "审核通过订单成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.list"));
		return "share/message";
	}

	@RequestMapping("/control/order/confirmPayment")
	@Permission(module = "order", privilege = "confirmPayment")
	public String confirmPayment(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).confirmPayment(bean.getOrderid());
		model.addAttribute("message", "财务确认订单已付款成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.list") + "?state=WAITPAYMENT");
		return "share/message";
	}

	@RequestMapping("/control/order/employeeUnlockOrder")
	@Permission(module = "order", privilege = "view")
	public String employeeUnlockOrder(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).unlock(bean.getOrderid());
		model.addAttribute("directUrl", "/control/order/list.do");
		return "share/directUrl";
	}

	@RequestMapping("/control/order/list")
	@Permission(module = "order", privilege = "view")
	public String list(OrderBean orderBean, ModelMap model) {
		PageView<Order> pageView = new PageView<Order>(12, orderBean.getPage());
		OrderState state = orderBean.getState() != null ? orderBean.getState() : OrderState.WAITCONFIRM;
		orderBean.setState(state);
		pageView.setQueryResult(this.locate(OrderLogic.class).getQueryResult(orderBean,
				new PageBy(pageView.getCurrentpage(), pageView.getMaxresult())));
		model.addAttribute("pageView", pageView);
		return "order/orderlist";
	}

	@RequestMapping("/control/lockorder/list")
	@Permission(module = "order", privilege = "view")
	public String lockOrderList(OrderBean bean, ModelMap model) throws Exception {
		PageView<Order> pageView = new PageView<Order>(12, bean.getPage());
		Page<Order> lockOrders = this.getDefaultLogic().findPage(Order.class, QueryFilter.valueOf("lockuser", PMLO.NN),
				OrderBy.asc("createDate"), PageBy.of(pageView.getCurrentpage(), pageView.getMaxresult()));
		pageView.setQueryResult(lockOrders);
		model.addAttribute("pageView", pageView);
		model.addAttribute("showButton", true);
		return "order/orderlist";
	}

	@RequestMapping("/control/order/printOrder")
	@Permission(module = "order", privilege = "view")
	public String printOrder(OrderBean bean, ModelMap model) throws Exception {
		model.addAttribute("order", this.getDefaultLogic().findById(Order.class, bean.getOrderid()));
		return "order/print";
	}

	@RequestMapping("/control/order/query")
	@Permission(module = "order", privilege = "view")
	public String queryUI() {
		return "order/queryorder";
	}

	@RequestMapping("/control/order/turnDelivered")
	@Permission(module = "order", privilege = "turnDelivered")
	public String turnDelivered(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).turnDelivered(bean.getOrderid());
		model.addAttribute("message", "订单转已发货成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.list") + "?state=WAITDELIVER");
		return "share/message";
	}

	@RequestMapping("/control/order/turnReceived")
	@Permission(module = "order", privilege = "turnReceived")
	public String turnReceived(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).turnReceived(bean.getOrderid());
		model.addAttribute("message", "订单转已收货成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.list") + "?state=DELIVERED");
		return "share/message";
	}

	@RequestMapping("/control/order/turnWaitdeliver")
	@Permission(module = "order", privilege = "turnWaitdeliver")
	public String turnWaitdeliver(OrderBean bean, ModelMap model) throws Exception {
		this.locate(OrderLogic.class).turnWaitdeliver(bean.getOrderid());
		model.addAttribute("message", "订单转等待发货成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.order.list") + "?state=ADMEASUREPRODUCT");
		return "share/message";
	}

	@RequestMapping("/control/order/view")
	@Permission(module = "order", privilege = "view")
	public String view(OrderBean bean, ModelMap model, HttpSession session) throws Exception {
		Order order = this.getDefaultLogic().findById(Order.class, bean.getOrderid());
		SysUser employee = (SysUser) session.getAttribute("employee");
		if (order.getLockuser() != null && !order.getLockuser().equals(employee.getUsername())) {
			model.addAttribute("message", "该订单已被" + order.getLockuser() + "锁定!");
			model.addAttribute("urladdress", SiteUrl.readUrl("control.order.list"));
			return "share/message";
		}
		order = this.locate(OrderLogic.class).addLock(bean.getOrderid(), employee.getUsername());
		model.addAttribute("order", order);
		return "order/orderview";
	}
}
