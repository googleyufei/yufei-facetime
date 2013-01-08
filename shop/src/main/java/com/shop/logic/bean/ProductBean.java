package com.shop.logic.bean;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class ProductBean extends BaseBean {

	private static final long serialVersionUID = -408138708357588829L;
	private Integer productid;
	private Integer[] productids;
	/** 货号 **/
	private String code;
	/** 产品名称 **/
	private String name;
	/** 型号 **/
	private String model;
	/** 底价(采购进来的价格) **/
	private Float baseprice;
	/** 市场价 **/
	private Float marketprice;
	/** 销售价 **/
	private Float sellprice;
	/** 重量 单位:克 **/
	private Integer weight;
	/** 产品简介 **/
	private String description;
	/** 购买说明 **/
	private String buyexplain;
	/** 性别要求 **/
	private String sex;
	private Float startsellprice;
	private Float endsellprice;
	private Float startbaseprice;
	private Float endbaseprice;
	private String word;

	/** 产品类型 **/
	private Integer typeid;
	private String typeName;

	/** 品牌 **/
	private String brandid;

	/** 样式 **/
	private Integer productstyleid;
	private Integer[] stylesids;
	private String stylename;
	private String imageName;
	private CommonsMultipartFile imagefile;

	public Float getBaseprice() {
		return baseprice;
	}

	public String getBrandid() {
		return brandid;
	}

	public String getBuyexplain() {
		return buyexplain;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Float getEndbaseprice() {
		return endbaseprice;
	}

	public Float getEndsellprice() {
		return endsellprice;
	}

	public CommonsMultipartFile getImagefile() {
		return imagefile;
	}

	public String getImageName() {
		return imageName;
	}

	public Float getMarketprice() {
		return marketprice;
	}

	public String getModel() {
		return model;
	}

	public String getName() {
		return name;
	}

	public Integer getProductid() {
		return productid;
	}

	public Integer[] getProductids() {
		return productids;
	}

	public Integer getProductstyleid() {
		return productstyleid;
	}

	public Float getSellprice() {
		return sellprice;
	}

	public String getSex() {
		return sex;
	}

	public Float getStartbaseprice() {
		return startbaseprice;
	}

	public Float getStartsellprice() {
		return startsellprice;
	}

	public String getStylename() {
		return stylename;
	}

	public Integer[] getStylesids() {
		return stylesids;
	}

	public Integer getTypeid() {
		return typeid;
	}

	public String getTypeName() {
		return typeName;
	}

	public Integer getWeight() {
		return weight;
	}

	public String getWord() {
		return word;
	}

	public void setBaseprice(Float baseprice) {
		this.baseprice = baseprice;
	}

	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}

	public void setBuyexplain(String buyexplain) {
		this.buyexplain = buyexplain;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEndbaseprice(Float endbaseprice) {
		this.endbaseprice = endbaseprice;
	}

	public void setEndsellprice(Float endsellprice) {
		this.endsellprice = endsellprice;
	}

	public void setImagefile(CommonsMultipartFile imagefile) {
		this.imagefile = imagefile;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setMarketprice(Float marketprice) {
		this.marketprice = marketprice;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public void setProductids(Integer[] productids) {
		this.productids = productids;
	}

	public void setProductstyleid(Integer productstyleid) {
		this.productstyleid = productstyleid;
	}

	public void setSellprice(Float sellprice) {
		this.sellprice = sellprice;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setStartbaseprice(Float startbaseprice) {
		this.startbaseprice = startbaseprice;
	}

	public void setStartsellprice(Float startsellprice) {
		this.startsellprice = startsellprice;
	}

	public void setStylename(String stylename) {
		this.stylename = stylename;
	}

	public void setStylesids(Integer[] stylesids) {
		this.stylesids = stylesids;
	}

	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public void setWord(String word) {
		this.word = word;
	}
}
