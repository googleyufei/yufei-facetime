package com.facetime.spring.support;

public enum PMLO {

	EQ("="), GE(">="), GT(">"), IN(" in "), ISNULL("is null", true), LE("<="), LIKE("like"), LT("<"), NE("!="), NN(
			"is not null", true);

	private String operate;
	private boolean single;

	private PMLO() {
		this("=", false);
	}

	private PMLO(String operate) {
		this(operate, false);
	}

	private PMLO(String operate, boolean single) {
		this.operate = operate;
		this.single = single;
	}

	public String getOperate() {
		return operate;
	}

	public boolean isSingle() {
		return single;
	}
}
