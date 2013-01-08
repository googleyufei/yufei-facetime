package com.shop.domain.product;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.compass.annotations.Index;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.Store;

@Entity
@Searchable(root = false)
public class Brand implements BusinessObject {
	private static final long serialVersionUID = -4540465642606278764L;
	private String code;
	/** Ʒ����� **/
	private String name;
	/** �Ƿ�ɼ� **/
	private Boolean visible = true;
	/** logoͼƬ·�� ��:/images/brand/2008/12/12/ooo.gif" **/
	private String logopath;

	public Brand() {
	}

	public Brand(String code) {
		this.code = code;
	}

	public Brand(String name, String logopath) {
		this.name = name;
		this.logopath = logopath;
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
		final Brand other = (Brand) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

	@Id
	@Column(length = 36)
	@SearchableProperty(index = Index.NO, store = Store.YES)
	public String getCode() {
		return code;
	}

	@Column(length = 80)
	public String getLogopath() {
		return logopath;
	}

	@Column(length = 40, nullable = false)
	@SearchableProperty(index = Index.NOT_ANALYZED, store = Store.YES, name = "brandName")
	public String getName() {
		return name;
	}

	@Column(nullable = false)
	public Boolean getVisible() {
		return visible;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (code == null ? 0 : code.hashCode());
		return result;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setLogopath(String logopath) {
		this.logopath = logopath;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
