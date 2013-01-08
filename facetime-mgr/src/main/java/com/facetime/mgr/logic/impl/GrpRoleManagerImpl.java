package com.facetime.mgr.logic.impl;

import com.facetime.mgr.bean.GrpRoleForm;
import com.facetime.mgr.domain.UsrGrpRole;
import com.facetime.mgr.domain.UsrRole;
import com.facetime.mgr.logic.GrpRoleManager;
import com.facetime.spring.logic.LogicImpl;
import com.facetime.spring.support.QueryFilter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GrpRoleManagerImpl extends LogicImpl implements GrpRoleManager {
	@Override
	public List<GrpRoleForm> findGrpRole(String grpcode) {
		List<UsrRole> roleList = this.findList(UsrRole.class);
		List<UsrGrpRole> grpRoleList = this.findList(UsrGrpRole.class, new QueryFilter("grpcode", grpcode));
		List<GrpRoleForm> result = new ArrayList<GrpRoleForm>();
		for (UsrRole role : roleList) {
			GrpRoleForm form = new GrpRoleForm();
			form.setRolecode(role.getRolecode());
			form.setRolename(role.getRolename());
			if (grpRoleList != null) {
				for (UsrGrpRole grprole : grpRoleList) {
					if (grprole.getRolecode().equals(form.getRolecode())) {
						form.setChecked("checked");
					}
				}
			}
			form.setGrpcode(grpcode);
			result.add(form);
		}
		return result;
	}

	@Override
	public int removeGrpRole(String grpcode) {
		return this.delete(UsrGrpRole.class, new QueryFilter("grpcode", grpcode));
	}

	@Override
	public void saveGrpRole(String grpcode, List<UsrGrpRole> grpRoleLst) {
		// 先删除所有和grpcode有关的群组关系，然后在添加
		removeGrpRole(grpcode);
		this.save(grpRoleLst);
	}

}
