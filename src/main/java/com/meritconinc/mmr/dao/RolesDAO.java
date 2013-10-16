package com.meritconinc.mmr.dao;

import java.util.List;

import com.meritconinc.mmr.model.common.RoleVO;

public interface RolesDAO {
	
	/**
	 * Get all roles that are visible to the specified user
	 * @param user
	 * @return
	 */
	public List<RoleVO> getRolesByUser(String locale, String user);
	public List<RoleVO> getRolesByType(String locale, int type);
	
}