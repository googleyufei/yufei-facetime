package com.facetime.mgr.logic;

import com.facetime.mgr.bean.GrpRoleForm;
import com.facetime.mgr.domain.UsrGrpRole;
import com.facetime.spring.logic.Logic;

import java.util.List;

public interface GrpRoleManager extends Logic {

	/**
	 * 查找群组关系，构造GrpRoleFormLst
	 */
	public List<GrpRoleForm> findGrpRole(String grpcode);

	public int removeGrpRole(String grpcode);

	public void saveGrpRole(String grpcode, List<UsrGrpRole> grpRoleList);
}
