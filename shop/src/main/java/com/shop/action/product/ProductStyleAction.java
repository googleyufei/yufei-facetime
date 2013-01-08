package com.shop.action.product;

import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.LogicUtils;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;

import com.shop.action.privilege.Permission;
import com.shop.domain.product.ProductInfo;
import com.shop.domain.product.ProductStyle;
import com.shop.logic.bean.BaseBean;
import com.shop.logic.bean.ProductStyleBean;
import com.shop.util.SiteUrl;
import com.shop.util.UploadUtil;

@Controller
public class ProductStyleAction extends Action implements ServletContextAware {

	private ServletContext application;

	@RequestMapping("/control/product/style/add")
	@Permission(module = "product", privilege = "insert")
	public String add(ProductStyleBean bean, ModelMap model) throws Exception {
		String errorMsg = BaseBean.checkImageFile(bean.getImagefile());
		if (errorMsg != null) {
			model.addAttribute("urladdress", SiteUrl.readUrl("control.product.style.addUI"));
			model.addAttribute("message", errorMsg);
			return "share/message";
		}
		ProductInfo product = this.defaultLogic.findById(ProductInfo.class, bean.getProductid());
		ProductStyle style = new ProductStyle(bean.getStylename(), UploadUtil.getFileName(bean.getImagefile()));
		String fileName = UploadUtil.getFileName(bean.getImagefile());
		style.setImagename(fileName);
		style.setProduct(product);
		UploadUtil.saveProductImageFile(application, bean.getImagefile(), fileName, style.getProduct().getType()
				.getTypeid(), style.getProduct().getId());

		model.addAttribute("message", "产品样式添加成功!");
		model.addAttribute("urladdress",
				SiteUrl.readUrl("control.product.style.list") + "?productid=" + product.getId());
		return "share/message";
	}

	@RequestMapping("/control/product/style/addUI")
	@Permission(module = "product", privilege = "insert")
	public String addUI() throws Exception {
		return "product/add_productstyle";
	}

	@RequestMapping("/control/product/style/disable")
	@Permission(module = "product", privilege = "visible")
	public String disable(ProductStyleBean bean, HttpServletRequest request) throws Exception {
		setVisibleStatu(bean.getStylesids(), false);
		request.setAttribute("message", "产品样式下架成功!");
		request.setAttribute("urladdress",
				SiteUrl.readUrl("control.product.style.list") + "?productid=" + bean.getProductid());
		return "share/message";
	}

	@RequestMapping("/control/product/style/edit")
	@Permission(module = "product", privilege = "update")
	public String edit(ProductStyleBean bean, ModelMap model) throws Exception {
		ProductStyle style = find(bean.getProductstyleid());
		style.setName(bean.getStylename());
		ProductInfo product = style.getProduct();
		String errorMsg = BaseBean.checkImageFile(bean.getImagefile());
		if (CheckUtil.isValid(bean.getImagefile()) && errorMsg != null) {
			model.addAttribute("message", errorMsg);
			model.addAttribute("urladdress",
					SiteUrl.readUrl("control.product.style.editUI") + "?productid=" + product.getId());
			return "share/message";
		}
		if (CheckUtil.isValid(bean.getImagefile())) {
			String imageName = UploadUtil.getFileName(bean.getImagefile());
			UploadUtil.saveProductImageFile(application, bean.getImagefile(), imageName, product.getType().getTypeid(),
					product.getId());
			style.setImagename(imageName);
		}
		this.getDefaultLogic().update(style);

		model.addAttribute("message", "产品样式修改成功!");
		model.addAttribute("urladdress",
				SiteUrl.readUrl("control.product.style.list") + "?productid=" + product.getId());
		return "share/message";
	}

	@RequestMapping("/control/product/style/editUI")
	@Permission(module = "product", privilege = "update")
	public String editUI(ProductStyleBean bean, HttpServletRequest request) throws Exception {
		ProductStyle productstyle = find(bean.getProductstyleid());
		request.setAttribute("stylename", productstyle.getName());
		request.setAttribute("imagepath", productstyle.getImageFullPath());
		return "product/edit_productstyle";
	}

	@RequestMapping("/control/product/style/enable")
	@Permission(module = "product", privilege = "visible")
	public String enable(ProductStyleBean bean, HttpServletRequest request) throws Exception {
		setVisibleStatu(bean.getStylesids(), true);
		request.setAttribute("message", "产品样式上架成功!");
		request.setAttribute("urladdress",
				SiteUrl.readUrl("control.product.style.list") + "?productid=" + bean.getProductid());
		return "share/message";
	}

	@RequestMapping("/control/product/style/list")
	@Permission(module = "product", privilege = "view")
	public String list(ProductStyleBean bean, HttpServletRequest request) throws Exception {
		request.setAttribute("styles", listStyle(bean.getProductid()));
		return "product/productstylelist";
	}

	@Override
	public void setServletContext(ServletContext paramServletContext) {
		application = paramServletContext;
	}

	private ProductStyle find(Integer styleId) {
		return this.defaultLogic.findById(ProductStyle.class, styleId);
	}

	private List<ProductStyle> listStyle(Integer productid) {
		return this.getDefaultLogic().findList(ProductStyle.class, LogicUtils.filterby("product.id", productid),
				LogicUtils.orderDesc("visible"), LogicUtils.orderDesc("id"));
	}

	private void setVisibleStatu(Integer[] productStyleIds, boolean statue) {
		List<ProductStyle> list = defaultLogic.findByIds(ProductStyle.class, productStyleIds);
		for (ProductStyle style : list)
			style.setVisible(statue);
		defaultLogic.update(list);
	}
}
