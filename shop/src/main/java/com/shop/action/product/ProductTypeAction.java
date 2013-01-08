package com.shop.action.product;

import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.action.privilege.Permission;
import com.shop.domain.PageView;
import com.shop.domain.product.ProductType;
import com.shop.logic.bean.ProductTypeBean;
import com.shop.util.SiteUrl;

@Controller
public class ProductTypeAction extends Action {

	@RequestMapping("/control/product/type/add")
	@Permission(module = "productType", privilege = "insert")
	public String add(ProductTypeBean bean, HttpServletRequest request) throws Exception {
		ProductType type = new ProductType(bean.getName(), bean.getNote());
		if (CheckUtil.isValid(bean.getParentid())) {
			type.setParent(new ProductType(bean.getParentid()));
		}
		this.getDefaultLogic().save(type);
		request.setAttribute("message", "产品类别添加成功");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.product.type.list"));
		return "share/message";
	}

	@RequestMapping("/control/product/type/addUI")
	@Permission(module = "productType", privilege = "insert")
	public String addUI() throws Exception {
		return "product/add_productType";
	}

	@RequestMapping("/control/product/type/edit")
	@Permission(module = "productType", privilege = "update")
	public String edit(ProductTypeBean bean, HttpServletRequest request) throws Exception {
		ProductType type = find(bean.getTypeid());
		type.setName(bean.getName());
		type.setNote(bean.getNote());
		this.getDefaultLogic().update(type);
		request.setAttribute("message", "产品类别修改成功!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.product.type.list"));
		return "share/message";
	}

	@RequestMapping("/control/product/type/editUI")
	@Permission(module = "productType", privilege = "update")
	public String editUI(ProductTypeBean bean, ModelMap model) throws Exception {
		model.addAttribute("productType", find(bean.getTypeid()));
		return "product/edit_productType";
	}

	@RequestMapping("/control/product/type/list")
	@Permission(module = "productType", privilege = "view")
	public String list(ProductTypeBean bean, HttpServletRequest request) throws Exception {
		PageView<ProductType> pageView = new PageView<ProductType>(12, bean.getPage());
		pageView.setQueryResult(this.queryProductType(bean,
				new PageBy(pageView.getCurrentpage(), pageView.getMaxresult())));
		request.setAttribute("pageView", pageView);
		return "product/ProductTypeList";
	}

	@RequestMapping("/control/product/type/queryUI")
	@Permission(module = "productType", privilege = "view")
	public String queryUI() throws Exception {
		return "product/query_productType";
	}

	@RequestMapping("/control/product/type/selectUI")
	@Permission(module = "product", privilege = "view")
	public String selectUI(ProductTypeBean bean, HttpServletRequest request) throws Exception {
		if (CheckUtil.isValid(bean.getTypeid())) {
			List<ProductType> types = getAllTypes(bean.getTypeid());
			request.setAttribute("menutypes", types);
		}
		request.setAttribute("types", queryProductType(bean.getTypeid()));
		return "product/ProductTypeSelect";
	}

	private ProductType find(Integer typeId) {
		return this.defaultLogic.findById(ProductType.class, typeId);
	}

	private List<ProductType> getAllTypes(Integer typeId) {
		List<ProductType> types = new ArrayList<ProductType>();
		ProductType type = find(typeId);
		types.add(type);
		ProductType parent = type.getParent();
		while (parent != null) {
			types.add(parent);
			parent = parent.getParent();
		}
		return types;
	}

	private List<ProductType> queryProductType(Integer typeId) {
		List<QueryFilter> filters = LogicUtils.filterList();
		filters.add(LogicUtils.filterby("visible", true));
		if (CheckUtil.isValid(typeId)) {
			filters.add(LogicUtils.filterby("parent.typeid", typeId));
		} else {
			filters.add(LogicUtils.filterby("parent", PMLO.ISNULL));
		}
		return defaultLogic.findList(ProductType.class, LogicUtils.toArray(filters));
	}

	private Page<ProductType> queryProductType(ProductTypeBean bean, PageBy bound) {
		List<QueryFilter> filters = LogicUtils.filterList();
		filters.add(LogicUtils.filterby("visible", true));
		if ("true".equals(bean.getQuery())) {
			if (CheckUtil.isValid(bean.getName())) {
				filters.add(LogicUtils.filterby("name", PMLO.LIKE, "%" + bean.getName() + "%"));
			}
		} else {
			if (CheckUtil.isValid(bean.getParentid())) {
				filters.add(LogicUtils.filterby("parent.typeid", bean.getParentid()));
			} else {
				filters.add(LogicUtils.filterby("parent.typeid", PMLO.ISNULL));
			}
		}
		return defaultLogic.findPage(ProductType.class, LogicUtils.toArray(filters), bound);
	}
}
