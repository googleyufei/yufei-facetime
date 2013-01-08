package com.shop.domain.user;

public enum Gender {
	MAN("男"), WOMEN("女");

	private String chinese;

	private Gender(String chinese) {
		this.chinese = chinese;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}
}