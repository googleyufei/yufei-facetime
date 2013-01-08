package com.shop.action.product;

import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.PageBy;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;

import com.shop.action.privilege.Permission;
import com.shop.domain.PageView;
import com.shop.domain.product.Brand;
import com.shop.domain.product.ProductInfo;
import com.shop.logic.bean.BaseBean;
import com.shop.logic.bean.ProductBean;
import com.shop.logic.product.ProductInfoLogic;
import com.shop.util.SiteUrl;
import com.shop.util.UploadUtil;
import com.shop.util.WebUtil;

@Controller
public class ProductAction extends Action implements ServletContextAware {

	@Autowired
	private ProductInfoLogic productInfoLogic;

	private ServletContext application;

	@RequestMapping("/control/product/add")
	@Permission(module = "product", privilege = "insert")
	public String add(ProductBean bean, ModelMap model) throws Exception {
		String errorMsg = BaseBean.checkImageFile(bean.getImagefile());
		if (errorMsg != null) {
			model.addAttribute("message", errorMsg);
			model.addAttribute("urladdress", SiteUrl.readUrl("control.product.add"));
			return "share/message";
		}
		String imageName = UploadUtil.getFileName(bean.getImagefile());
		bean.setImageName(imageName);
		ProductInfo product = productInfoLogic.save(bean);
		UploadUtil.saveProductImageFile(application, bean.getImagefile(), imageName, bean.getTypeid(), product.getId());

		model.addAttribute("message", "添加成功了");
		model.addAttribute("urladdress", SiteUrl.readUrl("control.product.list"));
		// 生成的文件存放在网站根目录的html/product/类别id/
		File saveDir = new File(WebUtil.getRealPath(application, "/html/product/" + product.getType().getTypeid()));
		BuildHtmlFile.createProductHtml(product, saveDir);
		return "share/message";
	}

	@RequestMapping("/control/product/addUI")
	@Permission(module = "product", privilege = "insert")
	public String addUI(HttpServletRequest request) throws Exception {
		request.setAttribute("brands", listBrand());
		return "product/add_product";
	}

	@RequestMapping("/control/product/disable")
	@Permission(module = "product", privilege = "visible")
	public String disable(@RequestParam("productids") Integer[] productids, HttpServletRequest request)
			throws Exception {
		productInfoLogic.setVisibleStatus(productids, false);
		request.setAttribute("message", "商品下架成功");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.product.list"));
		return "share/message";
	}

	@RequestMapping("/control/product/edit")
	@Permission(module = "product", privilege = "update")
	public String edit(ProductBean productBean, HttpServletRequest request) throws Exception {
		ProductInfo product = productInfoLogic.update(productBean);
		request.setAttribute("message", "产品修改成功!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.product.list"));

		File saveDir = new File(request.getSession().getServletContext()
				.getRealPath("/html/product/" + product.getType().getTypeid()));
		BuildHtmlFile.createProductHtml(product, saveDir);
		return "share/message";
	}

	@RequestMapping("/control/product/editUI")
	@Permission(module = "product", privilege = "update")
	public String editUI(ProductBean productBean, HttpServletRequest request) throws Exception {
		ProductInfo product = productInfoLogic.find(productBean.getProductid());
		fillProductBean(productBean, product);
		request.setAttribute("productBean", productBean);
		request.setAttribute("brands", listBrand());
		return "product/edit_product";
	}

	@RequestMapping("/control/product/enable")
	@Permission(module = "product", privilege = "visible")
	public String enable(@RequestParam("productids") Integer[] productids, HttpServletRequest request) throws Exception {
		productInfoLogic.setVisibleStatus(productids, true);
		request.setAttribute("message", "商品上架成功!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.product.list"));
		return "share/message";
	}

	@RequestMapping("/control/product/list")
	@Permission(module = "product", privilege = "view")
	public String list(ProductBean formbean, HttpServletRequest request) {
		PageView<ProductInfo> pageView = new PageView<ProductInfo>(12, formbean.getPage());
		PageBy bound = new PageBy(pageView.getCurrentpage(), pageView.getMaxresult());
		pageView.setQueryResult(productInfoLogic.getProductInfos(formbean, bound));
		request.setAttribute("pageView", pageView);
		return "product/productlist";
	}

	@RequestMapping("/control/product/queryUI")
	@Permission(module = "product", privilege = "view")
	public String queryUI(HttpServletRequest request) throws Exception {
		request.setAttribute("brands", listBrand());
		return "product/query_product";
	}

	@RequestMapping("/control/product/recommend")
	@Permission(module = "product", privilege = "commend")
	public String recommend(@RequestParam("productids") Integer[] productids, HttpServletRequest request)
			throws Exception {
		productInfoLogic.setCommendStatu(productids, true);
		request.setAttribute("message", "商品推荐成功!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.product.list"));
		return "share/message";
	}

	@Override
	public void setServletContext(ServletContext paramServletContext) {
		application = paramServletContext;
	}

	@RequestMapping("/control/product/unrecommend")
	@Permission(module = "product", privilege = "commend")
	public String unrecommend(@RequestParam("productids") Integer[] productids, HttpServletRequest request)
			throws Exception {
		productInfoLogic.setCommendStatu(productids, false);
		request.setAttribute("message", "商品不推荐成功!");
		request.setAttribute("urladdress", SiteUrl.readUrl("control.product.list"));
		return "share/message";
	}

	private void fillProductBean(ProductBean productBean, ProductInfo product) {
		productBean.setBaseprice(product.getBaseprice());
		if (product.getBrand() != null) {
			productBean.setBrandid(product.getBrand().getCode());
		}
		productBean.setBuyexplain(product.getBuyexplain());
		productBean.setCode(product.getCode());
		productBean.setDescription(product.getDescription());
		productBean.setMarketprice(product.getMarketprice());
		productBean.setModel(product.getModel());
		productBean.setName(product.getName());
		productBean.setSellprice(product.getSellprice());
		productBean.setSex(product.getSexrequest().toString());
		productBean.setTypeid(product.getType().getTypeid());
		productBean.setTypeName(product.getType().getName());
		productBean.setWeight(product.getWeight());
	}

	private List<Brand> listBrand() {
		return defaultLogic.findList(Brand.class);
	}
}
