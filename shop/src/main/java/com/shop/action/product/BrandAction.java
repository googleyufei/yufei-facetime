package com.shop.action.product;

import com.facetime.core.utils.IdGenerator;
import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.OrderBy;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;

import com.shop.action.privilege.Permission;
import com.shop.domain.PageView;
import com.shop.domain.product.Brand;
import com.shop.logic.bean.BaseBean;
import com.shop.logic.bean.BrandBean;
import com.shop.util.SiteUrl;
import com.shop.util.UploadUtil;

@Controller
public class BrandAction extends Action implements ServletContextAware {

	private ServletContext application;

	@RequestMapping("/control/product/brand/add")
	@Permission(module = "brand", privilege = "insert")
	public String add(BrandBean bean, ModelMap model) throws Exception {
		String errorMsg = BaseBean.checkImageFile(bean.getLogofile());
		if (errorMsg != null) {
			model.addAttribute("urladdress", SiteUrl.readUrl("control.brand.addUI"));
			model.addAttribute("message", errorMsg);
			return "share/message";
		}
		Brand brand = new Brand();
		brand.setCode(IdGenerator.strId());
		brand.setName(bean.getName());
		String fileName = UploadUtil.writeFileToDisk(bean.getLogofile(), application, getPrefixPath());
		brand.setLogopath(getPrefixPath() + "/" + fileName);
		this.getDefaultLogic().save(brand);

		model.addAttribute("message", "添加品牌成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.brand.list"));
		return "share/message";
	}

	@RequestMapping("/control/product/brand/addUI")
	@Permission(module = "brand", privilege = "insert")
	public String addUI() throws Exception {
		return "product/add_brand";
	}

	@RequestMapping("/control/product/brand/edit")
	@Permission(module = "brand", privilege = "update")
	public String edit(BrandBean bean, ModelMap model) throws Exception {
		String errorMsg = BaseBean.checkImageFile(bean.getLogofile());
		if (errorMsg != null) {
			model.addAttribute("urladdress", SiteUrl.readUrl("control.brand.list"));
			model.addAttribute("message", errorMsg);
			return "share/message";
		}
		Brand brand = find(bean.getCode());
		brand.setName(bean.getName());
		if (CheckUtil.isValid(bean.getLogofile())) {
			String fileName = UploadUtil.writeFileToDisk(bean.getLogofile(), application, getPrefixPath());
			brand.setLogopath(getPrefixPath() + "/" + fileName);
		}
		this.getDefaultLogic().update(brand);
		model.addAttribute("message", "产品品牌修改成功!");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.brand.list"));
		return "share/message";
	}

	@RequestMapping("/control/product/brand/editUI")
	@Permission(module = "brand", privilege = "update")
	public String editUI(BrandBean bean, HttpServletRequest request) throws Exception {
		Brand brand = find(bean.getCode());
		bean.setName(brand.getName());
		bean.setLogoimagepath(brand.getLogopath());
		request.setAttribute("bean", bean);
		return "product/edit_brand";
	}

	@RequestMapping("/control/product/brand/list")
	@Permission(module = "brand", privilege = "view")
	public String list(BrandBean bean, HttpServletRequest request) {
		PageView<Brand> pageView = new PageView<Brand>(12, bean.getPage());
		pageView.setQueryResult(getBrandResult(bean, new PageBy(pageView.getCurrentpage(), pageView.getMaxresult())));
		request.setAttribute("pageView", pageView);
		return "product/brandlist";
	}

	@RequestMapping("/control/product/brand/queryUI")
	@Permission(module = "brand", privilege = "view")
	public String queryUI() throws Exception {
		return "product/query_brand";
	}

	@Override
	public void setServletContext(ServletContext paramServletContext) {
		application = paramServletContext;
	}

	private Brand find(String brandCode) {
		return this.defaultLogic.findById(Brand.class, brandCode);
	}

	private Page<Brand> getBrandResult(BrandBean brandBean, PageBy bound) {
		List<QueryFilter> filters = new ArrayList<QueryFilter>();
		filters.add(QueryFilter.valueOf("visible", true));
		if (brandBean != null && "true".equals(brandBean.getQuery())) {
			filters.add(QueryFilter.valueOf("name", PMLO.LIKE, "%" + brandBean.getName() + "%"));
		}
		return defaultLogic.findPage(Brand.class, LogicUtils.toArray(filters), OrderBy.desc("code"), bound);
	}

	private String getPrefixPath() {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd/HH");
		String logopathdir = "/images/brand/" + dateformat.format(new Date());
		return logopathdir;
	}
}
