package com.facetime.mgr.logic;

import com.facetime.mgr.bean.RolefuncVO;
import com.facetime.mgr.domain.UsrRole;
import com.facetime.mgr.domain.UsrRoleFunction;
import com.facetime.spring.logic.Logic;

import java.util.List;

public interface RoleInfoManager extends Logic {

	public void deleteRole(String rolecode);

	public List<RolefuncVO> getMenuTree();

	public List<UsrRoleFunction> getRoleFuncs(String rolecode);

	public List<UsrRole> getRoles();

	public void updateRoleFunc(String rolecode, List<String> roleFuncids);
}
