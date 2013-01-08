package com.shop.logic.product;

import com.facetime.mgr.utils.CheckUtil;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.domain.product.Brand;
import com.shop.domain.product.ProductInfo;
import com.shop.domain.product.ProductStyle;
import com.shop.domain.product.ProductType;
import com.shop.domain.product.Sex;
import com.shop.logic.bean.FrontProductBean;
import com.shop.logic.bean.ProductBean;

@Service
@Transactional
public class ProductInfoLogicImpl extends LogicImpl implements ProductInfoLogic {

	@Override
	@Transactional(readOnly = true)
	public ProductInfo find(Integer productId) {
		if (productId == null)
			throw new IllegalArgumentException("product id is null.");
		return this.findById(ProductInfo.class, productId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Brand> getBrandsByProductTypeid(Integer[] typeids) {
		if (CheckUtil.notValid(typeids))
			return new ArrayList<Brand>();
		List<String> brandCodes = this.findPart(ProductInfo.class, LogicUtils.filterby("type.typeid", PMLO.IN, typeids),
				new String[] { "brand.code" });
		return this.findByIds(Brand.class, brandCodes.toArray(new String[] {}));
	}

	@Override
	public Page<ProductInfo> getFrontProducInfos(FrontProductBean bean, PageBy pageBy) {
		List<QueryFilter> filters = new ArrayList<QueryFilter>();
		filters.add(LogicUtils.filterby("visible", true));
		if (CheckUtil.isValid(bean.getTypeid())) {
			List<Integer> typeids = new ArrayList<Integer>();
			typeids.add(bean.getTypeid());
			getTypeids(typeids, new Integer[] { bean.getTypeid() });
			filters.add(LogicUtils.filterby("type.typeid", PMLO.IN, typeids.toArray()));
		}
		if (CheckUtil.isValid(bean.getSex()))
			filters.add(LogicUtils.filterby("sexrequest", Sex.valueOf(bean.getSex())));
		return this.findPage(ProductInfo.class, LogicUtils.toArray(filters), LogicUtils.orderby(bean.getSort()), pageBy);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductInfo> getProductInfos(ProductBean productBean, PageBy pageby) {
		List<QueryFilter> filters = new ArrayList<QueryFilter>();
		if ("true".equals(productBean.getQuery())) {
			if (CheckUtil.isValid(productBean.getName()))
				filters.add(LogicUtils.filterby("name", PMLO.LIKE, "%" + productBean.getName() + "%"));
			if (CheckUtil.isValid(productBean.getTypeid()))
				filters.add(LogicUtils.filterby("type.typeid", productBean.getTypeid()));
			if (CheckUtil.isValid(productBean.getStartbaseprice()))
				filters.add(LogicUtils.filterby("baseprice", PMLO.GE, productBean.getStartbaseprice()));
			if (CheckUtil.isValid(productBean.getEndbaseprice()))
				filters.add(LogicUtils.filterby("baseprice", PMLO.LE, productBean.getEndbaseprice()));
			if (CheckUtil.isValid(productBean.getStartsellprice()))
				filters.add(LogicUtils.filterby("sellprice", PMLO.GE, productBean.getStartsellprice()));
			if (CheckUtil.isValid(productBean.getEndsellprice()))
				filters.add(LogicUtils.filterby("sellprice", PMLO.LE, productBean.getEndsellprice()));
			if (CheckUtil.isValid(productBean.getCode()))
				filters.add(LogicUtils.filterby("code", productBean.getCode()));
			if (CheckUtil.isValid(productBean.getBrandid()))
				filters.add(LogicUtils.filterby("brand.code", productBean.getBrandid()));
		}
		return this.findPage(ProductInfo.class, LogicUtils.toArray(filters), LogicUtils.orderDesc("id"), pageby);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductInfo> getTopSell(Integer typeid, int maxResult) {
		List<Integer> typeids = new ArrayList<Integer>();
		typeids.add(typeid);
		getTypeids(typeids, new Integer[] { typeid });
		return this.findList(ProductInfo.class, new QueryFilter[] {
				LogicUtils.filterby("type.typeid", PMLO.IN, typeids.toArray()), LogicUtils.filterby("commend", true) },
				LogicUtils.orderDesc("sellcount"));
	}

	@Override
	public List<Integer> getTypeids(Integer typeId) {
		List<Integer> typeids = new ArrayList<Integer>();
		typeids.add(typeId);
		getTypeids(typeids, new Integer[] { typeId });
		return typeids;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductInfo> getViewHistory(Integer[] productids, int maxResult) {
		return this.findPage(ProductInfo.class, LogicUtils.filterby("id", productids), LogicUtils.pageby(1, maxResult)).getQueryResult();
	}

	@Override
	public ProductInfo save(ProductBean productBean) {
		ProductInfo product = new ProductInfo();
		product.setName(productBean.getName());
		product.setBaseprice(productBean.getBaseprice());
		product.setSellprice(productBean.getSellprice());
		product.setMarketprice(productBean.getMarketprice());
		if (CheckUtil.isValid(productBean.getBrandid()))
			product.setBrand(findById(Brand.class, productBean.getBrandid()));
		product.setBuyexplain(productBean.getBuyexplain());
		product.setCode(productBean.getCode());
		product.setDescription(productBean.getDescription());
		product.setModel(productBean.getModel());
		product.setSexrequest(Sex.valueOf(productBean.getSex()));
		product.setWeight(productBean.getWeight());
		product.setType(findById(ProductType.class, productBean.getTypeid()));
		ProductStyle style = new ProductStyle(productBean.getStylename(), productBean.getImageName());
		product.addProductStyle(style);

		this.save(product);
		this.save(style);
		return product;
	}

	@Override
	public void setCommendStatu(Integer[] productids, boolean status) {
		if (CheckUtil.notValid(productids))
			return;
		List<ProductInfo> list = this.findByIds(ProductInfo.class, productids);
		for (ProductInfo info : list)
			info.setCommend(status);
		this.update(list);
	}

	@Override
	public void setVisibleStatus(Integer[] productids, boolean status) {
		if (CheckUtil.notValid(productids))
			return;
		List<ProductInfo> list = this.findByIds(ProductInfo.class, productids);
		for (ProductInfo info : list)
			info.setVisible(status);
		this.update(list);
	}

	@Override
	public ProductInfo update(ProductBean productBean) {
		ProductInfo product = find(productBean.getProductid());
		product.setName(productBean.getName());
		product.setBaseprice(productBean.getBaseprice());
		product.setSellprice(productBean.getSellprice());
		product.setMarketprice(productBean.getMarketprice());
		if (CheckUtil.isValid(productBean.getBrandid()))
			product.setBrand(findById(Brand.class, productBean.getBrandid()));
		product.setBuyexplain(productBean.getBuyexplain());
		product.setCode(productBean.getCode());
		product.setDescription(productBean.getDescription());
		product.setModel(productBean.getModel());
		product.setSexrequest(Sex.valueOf(productBean.getSex()));
		product.setWeight(productBean.getWeight());
		product.setType(findById(ProductType.class, productBean.getTypeid()));
		this.update(product);
		return product;
	}

	private List<Integer> getSubTypeid(Integer[] parentids) {
		List<Integer> typeids = this.findPart(ProductType.class, LogicUtils.filterby("parent.typeid", PMLO.IN, parentids),
				new String[] { "typeid" });
		List<Integer> retVal = new ArrayList<Integer>();
		for (Integer typeid : typeids)
			retVal.add(typeid);
		return retVal;
	}

	@Transactional(readOnly = true)
	private void getTypeids(List<Integer> outtypeids, Integer[] typeids) {
		List<Integer> subtypeids = getSubTypeid(typeids);
		if (subtypeids != null && subtypeids.size() > 0) {
			outtypeids.addAll(subtypeids);
			Integer[] ids = new Integer[subtypeids.size()];
			for (int i = 0; i < subtypeids.size(); i++)
				ids[i] = subtypeids.get(i);
			getTypeids(outtypeids, ids);
		}
	}

}
