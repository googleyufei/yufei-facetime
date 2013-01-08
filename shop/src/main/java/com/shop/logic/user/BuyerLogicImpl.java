package com.shop.logic.user;

import com.facetime.mgr.utils.CheckUtil;
import com.facetime.mgr.utils.MD5;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.Limitable.PageBy;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;
import com.facetime.spring.support.Page;
import com.facetime.spring.support.QueryFilter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.domain.user.Buyer;
import com.shop.logic.bean.BuyerBean;

@Service
public class BuyerLogicImpl extends LogicImpl implements BuyerLogic {

	@Override
	public void cancel(Buyer cus) {
		this.delete(cus);
	}

	@Override
	public boolean check(String username, String password) {
		return get(username, password) != null;
	}

	@Override
	public void disable(String[] usernames) {
		List<Buyer> list = findByIds(Buyer.class, usernames);
		for (Buyer buyer : list)
			buyer.setVisible(false);
		update(list);
	}

	@Override
	public void enable(String[] usernames) {
		List<Buyer> list = findByIds(Buyer.class, usernames);
		for (Buyer buyer : list)
			buyer.setVisible(true);
		update(list);
	}

	@Override
	public Buyer get(String username, String password) {
		password = MD5.MD5Encode(password);
		Buyer buyer = findById(Buyer.class, username);
		if (buyer != null && password.equals(buyer.getPassword()))
			return buyer;
		return null;
	}

	@Override
	public Page<Buyer> getQueryResult(BuyerBean bean, PageBy pageBy) {
		List<QueryFilter> filters = LogicUtils.filterList();
		if ("true".equals(bean.getQuery())) {
			if (CheckUtil.isValid(bean.getUsername()))
				filters.add(LogicUtils.filterby("username", PMLO.LIKE, "%" + bean.getUsername().trim() + "%"));
			if (CheckUtil.isValid(bean.getEmail()))
				filters.add(LogicUtils.filterby("email", PMLO.LIKE, "%" + bean.getEmail().trim() + "%"));
			if (CheckUtil.isValid(bean.getRealname()))
				filters.add(LogicUtils.filterby("realnam", PMLO.LIKE, "%" + bean.getRealname().trim() + "%"));
		}
		return findPage(Buyer.class, LogicUtils.toArray(filters), LogicUtils.orderDesc("regTime"), pageBy);
	}

	@Override
	public boolean isUserExisted(String username) {
		return findById(Buyer.class, username) != null;
	}

	@Override
	public void save(Buyer cus) {
		cus.setPassword(MD5.MD5Encode(cus.getPassword()));
		this.save(cus);
	}
}
