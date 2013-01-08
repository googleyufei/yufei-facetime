package com.shop.domain.book;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 订单项
 */
@Entity
public class OrderItem implements BusinessObject {
	private static final long serialVersionUID = -4556739013763508505L;
	private Integer itemid;
	/* 产品名称 */
	private String productName;
	/* 产品id */
	private Integer productid;
	/* 产品销售价 */
	private Float productPrice = 0f;
	/* 购买数量 */
	private Integer amount = 1;
	/* 产品样式 */
	private String styleName;
	/* 产品样式ID */
	private Integer styleid;
	/* 所属订单 */
	private Order order;

	public OrderItem() {
	}

	public OrderItem(String productName, Integer productid, Float productPrice, Integer amount, String styleName,
			Integer styleid) {
		this.productName = productName;
		this.productid = productid;
		this.productPrice = productPrice;
		this.amount = amount;
		this.styleName = styleName;
		this.styleid = styleid;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OrderItem other = (OrderItem) obj;
		if (itemid == null) {
			if (other.itemid != null) {
				return false;
			}
		} else if (!itemid.equals(other.itemid)) {
			return false;
		}
		return true;
	}

	@Column(nullable = false)
	public Integer getAmount() {
		return amount;
	}

	@Id
	@GeneratedValue
	public Integer getItemid() {
		return itemid;
	}

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "order_id")
	public Order getOrder() {
		return order;
	}

	@Column(nullable = false)
	public Integer getProductid() {
		return productid;
	}

	@Column(length = 50, nullable = false)
	public String getProductName() {
		return productName;
	}

	@Column(nullable = false)
	public Float getProductPrice() {
		return productPrice;
	}

	@Column(nullable = false)
	public Integer getStyleid() {
		return styleid;
	}

	@Column(length = 30, nullable = false)
	public String getStyleName() {
		return styleName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (itemid == null ? super.hashCode() : itemid.hashCode());
		return result;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductPrice(Float productPrice) {
		this.productPrice = productPrice;
	}

	public void setStyleid(Integer styleid) {
		this.styleid = styleid;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

}
