package com.shop.domain.product;

public enum Sex {
	NONE {
		@Override
		public String getName() {
			return "男女不限";
		}
	},
	MAN {
		@Override
		public String getName() {
			return "男";
		}
	},
	WOMEN {
		@Override
		public String getName() {
			return "女";
		}
	};
	public abstract String getName();
}
