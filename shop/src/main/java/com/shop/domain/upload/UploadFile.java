package com.shop.domain.upload;

import com.facetime.core.bean.BusinessObject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UploadFile implements BusinessObject {

	private static final long serialVersionUID = 7291850395925809042L;
	@Id
	private String id;
	private String filepath;
	private Date uploadtime = new Date();

	public UploadFile() {
	}

	public UploadFile(String filepath) {
		this.filepath = filepath;
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
		final UploadFile other = (UploadFile) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Column(nullable = false, length = 80)
	public String getFilepath() {
		return filepath;
	}

	public String getId() {
		return id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getUploadtime() {
		return uploadtime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}

}
