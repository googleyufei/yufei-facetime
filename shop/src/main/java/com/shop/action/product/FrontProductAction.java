package com.shop.action.product;

import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.domain.PageView;
import com.shop.domain.product.ProductInfo;
import com.shop.domain.product.ProductStyle;
import com.shop.domain.product.ProductType;
import com.shop.logic.bean.FrontProductBean;
import com.shop.logic.product.ProductInfoLogic;
import com.shop.util.WebUtil;

@Controller
public class FrontProductAction extends Action {

	@RequestMapping("/product/list/display.do")
	public String display(FrontProductBean bean, ModelMap model) throws Exception {
		getProductInfo(bean, model);
		processProductBrand(bean, model);
		processProductType(bean, model);
		if ("imagetext".equalsIgnoreCase(bean.getStyle()))
			return "product/frontpage/productlist";
		else
			return "product/frontpage/productlist_text";
	}

	@RequestMapping("/product/switch/getViewHistory")
	public String getViewHistory(HttpServletRequest request) throws Exception {
		String cookieValue = WebUtil.getCookieByName(request, "productViewHistory");
		if (CheckUtil.isValid(cookieValue)) {
			String[] ids = cookieValue.split("-");
			Integer[] productids = new Integer[ids.length];
			for (int i = 0; i < ids.length; i++)
				productids[i] = new Integer(ids[i].trim());
			request.setAttribute("viewHistory", this.locate(ProductInfoLogic.class).getViewHistory(productids, 10));
		}
		return "product/frontpage/viewHistory";
	}

	@RequestMapping("/product/query")
	public String query(String word, Integer page, ModelMap model) throws Exception {
		PageView<ProductInfo> pageView = new PageView<ProductInfo>(18, page);
		pageView.setQueryResult(searchProduct(word, pageView.getCurrentpage(), pageView.getMaxresult()));
		model.addAttribute("pageView", pageView);
		return "product/frontpage/queryproductlist";
	}

	@RequestMapping("/product/switch/showimage")
	public String showimage() throws Exception {
		return "product/frontpage/showimage";
	}

	@RequestMapping("/product/switch/topsell")
	public String topsell(Integer typeid, ModelMap model) throws Exception {
		model.addAttribute("topsellproducts", this.locate(ProductInfoLogic.class).getTopSell(typeid, 10));
		return "product/frontpage/topsell";
	}

	@RequestMapping("/product/view")
	public String view(FrontProductBean bean, ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProductInfo product = this.locate(ProductInfoLogic.class).find(bean.getProductid());
		if (product == null) {
			model.addAttribute("message", "产品不存在");
			model.addAttribute("urladdress", "/");
			return "share/message";
		}
		WebUtil.addCookie(response, "productViewHistory", buildViewHistory(request, bean.getProductid()),
				30 * 24 * 60 * 60);
		List<ProductType> stypes = new ArrayList<ProductType>();
		ProductType parent = product.getType();
		while (parent != null) {
			stypes.add(parent);
			parent = parent.getParent();
		}
		model.addAttribute("product", product);
		model.addAttribute("stypes", stypes);
		return "product/frontpage/productview";
	}

	private String buildViewHistory(HttpServletRequest request, Integer currentProductId) {
		//23-2-6-5
		//1.如果当前浏览的id已经在浏览历史里了,我们要把移到最前面
		//2.如果浏览历史里已经达到了10个产品了,我们需要把最选进入的元素删除
		String cookieValue = WebUtil.getCookieByName(request, "productViewHistory");
		LinkedList<Integer> productids = new LinkedList<Integer>();
		if (CheckUtil.isValid(cookieValue)) {
			String[] ids = cookieValue.split("-");
			for (String id : ids)
				productids.offer(new Integer(id.trim()));
			if (productids.contains(currentProductId))
				productids.remove(currentProductId);
			if (productids.size() >= 10)
				productids.poll();
		}
		productids.offer(currentProductId);
		StringBuffer out = new StringBuffer();
		for (Integer id : productids)
			out.append(id).append('-');
		out.deleteCharAt(out.length() - 1);
		return out.toString();
	}

	private ProductType findProductType(Integer typeId) {
		return getDefaultLogic().findById(ProductType.class, typeId);
	}

	private void getProductInfo(FrontProductBean bean, ModelMap model) {
		PageView<ProductInfo> pageView = new PageView<ProductInfo>(2, bean.getPage());
		pageView.setPagecode(20);
		int firstindex = (pageView.getCurrentpage() - 1) * pageView.getMaxresult();

		pageView.setQueryResult(this.locate(ProductInfoLogic.class).getFrontProducInfos(bean,
				new PageBy(firstindex, pageView.getMaxresult())));
		for (ProductInfo product : pageView.getRecords()) {
			Set<ProductStyle> styles = new HashSet<ProductStyle>();
			for (ProductStyle style : product.getStyles())
				if (style.getVisible()) {
					styles.add(style);
					break;
				}
			product.setStyles(styles);
			//注意:执行此句代码会把修改后的数据同步回数据库,如果不想把数据同步回数据库,请在其后调用productInfoService.clear();
			product.setDescription(WebUtil.htmltoText(product.getDescription()));
		}
		model.addAttribute("pageView", pageView);
	}

	private void processProductBrand(FrontProductBean bean, ModelMap model) {
		List<Integer> typeids = this.locate(ProductInfoLogic.class).getTypeids(bean.getTypeid());
		model.addAttribute("brands",
				this.getDefaultLogic().findById(ProductInfo.class, typeids.toArray(new Integer[] {})));
	}

	private void processProductType(FrontProductBean bean, ModelMap model) {
		if (bean.getTypeid() != null && bean.getTypeid() > 0) {
			ProductType type = findProductType(bean.getTypeid());
			if (type != null) {
				List<ProductType> types = new ArrayList<ProductType>();
				types.add(type);
				ProductType parent = type.getParent();
				while (parent != null) {
					types.add(parent);
					parent = parent.getParent();
				}
				model.addAttribute("producttype", type);
				model.addAttribute("types", types);
			}
		}
	}

	/**
	 * 应用Compass和Lucence进行查询搜索
	 * @param keyword
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	private Page<ProductInfo> searchProduct(String keyword, int firstResult, int maxResult) {
		// FIXME 应用Compass和Lucence进行查询搜索
		// return compassTemplate.execute(new QueryCallback(keyword, firstResult, maxResult));
		return null;
	}
}
