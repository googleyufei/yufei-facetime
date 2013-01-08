package com.shop.domain.privilege;

import com.facetime.core.bean.BusinessObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * 部门实体
 * 
 * @author yufei
 */
@Entity
public class Department implements BusinessObject {
	private static final long serialVersionUID = 1447086568406131782L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	private String name;

	public Department() {
	}

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
