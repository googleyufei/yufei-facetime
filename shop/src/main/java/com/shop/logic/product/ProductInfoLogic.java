package com.shop.logic.product;

import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.Page;

import java.util.List;

import com.shop.domain.product.Brand;
import com.shop.domain.product.ProductInfo;
import com.shop.logic.bean.FrontProductBean;
import com.shop.logic.bean.ProductBean;

public interface ProductInfoLogic extends Logic {

	ProductInfo find(Integer productId);

	List<Brand> getBrandsByProductTypeid(Integer[] typeids);

	Page<ProductInfo> getFrontProducInfos(FrontProductBean bean, PageBy pageBy);

	/**
	 * 返回指定条件的产品集合
	 * 
	 * @param productBean
	 *            产品查询条件
	 * @param firstResult
	 *            分页的索引
	 * @param maxResult
	 *            分页的大小
	 */
	Page<ProductInfo> getProductInfos(ProductBean productBean, PageBy bound);

	List<ProductInfo> getTopSell(Integer typeid, int maxResult);

	List<Integer> getTypeids(Integer typeId);

	List<ProductInfo> getViewHistory(Integer[] productids, int maxResult);

	ProductInfo save(ProductBean productBean);

	void setCommendStatu(Integer[] productids, boolean statu);

	void setVisibleStatus(Integer[] productids, boolean statu);

	ProductInfo update(ProductBean productBean);
}
