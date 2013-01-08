package com.shop.domain.product;

import com.facetime.core.bean.BusinessObject;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

@Entity
@Searchable(root = false)
public class ProductType implements BusinessObject {
	private static final long serialVersionUID = 8106351120886053881L;
	/** ���id **/
	private Integer typeid;
	/** ������ **/
	private String name;
	/** ��ע,����google����ҳ������ **/
	private String note;
	/** �Ƿ�ɼ� **/
	private Boolean visible = true;
	/** ����� **/
	private Set<ProductType> childtypes = new HashSet<ProductType>();
	/** �������� **/
	private ProductType parent;

	private Set<ProductInfo> products = new HashSet<ProductInfo>();

	public ProductType() {
	}

	public ProductType(Integer typeid) {
		this.typeid = typeid;
	}

	public ProductType(String name, String note) {
		this.name = name;
		this.note = note;
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
		final ProductType other = (ProductType) obj;
		if (typeid == null) {
			if (other.typeid != null) {
				return false;
			}
		} else if (!typeid.equals(other.typeid)) {
			return false;
		}
		return true;
	}

	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.REMOVE }, mappedBy = "parent")
	public Set<ProductType> getChildtypes() {
		return childtypes;
	}

	@Column(length = 36, nullable = false)
	@SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES, name = "typeName")
	public String getName() {
		return name;
	}

	@Column(length = 200)
	public String getNote() {
		return note;
	}

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "parentid")
	public ProductType getParent() {
		return parent;
	}

	@OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE)
	public Set<ProductInfo> getProducts() {
		return products;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SearchableProperty(index = Index.NO, store = Store.YES)
	public Integer getTypeid() {
		return typeid;
	}

	@Column(nullable = false)
	public Boolean getVisible() {
		return visible;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (typeid == null ? 0 : typeid.hashCode());
		return result;
	}

	public void setChildtypes(Set<ProductType> childtypes) {
		this.childtypes = childtypes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setParent(ProductType parent) {
		this.parent = parent;
	}

	public void setProducts(Set<ProductInfo> products) {
		this.products = products;
	}

	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
