package com.facetime.mgr.logic;

import com.facetime.mgr.bean.UsrGrpForm;
import com.facetime.mgr.domain.UsrUsrGrp;
import com.facetime.spring.logic.Logic;

import java.util.List;

public interface UsrGrpManager extends Logic {

	public List<UsrGrpForm> findUsrGrp(String userid, String loginUser);
	public void updateGrpRole(String userid, List<UsrUsrGrp> usrGrpLst);
}
