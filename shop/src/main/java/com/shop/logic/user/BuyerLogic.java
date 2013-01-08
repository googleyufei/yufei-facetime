package com.shop.logic.user;

import com.facetime.spring.logic.Logic;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.Page;

import com.shop.domain.user.Buyer;
import com.shop.logic.bean.BuyerBean;

public interface BuyerLogic extends Logic {

	public abstract void disable(String[] usernames);

	void cancel(Buyer cus);

	boolean check(String username, String password);

	void enable(String[] usernames);

	Buyer get(String username, String password);

	Page<Buyer> getQueryResult(BuyerBean bean, PageBy pageBy);

	boolean isUserExisted(String username);

	void save(Buyer cus);

}
