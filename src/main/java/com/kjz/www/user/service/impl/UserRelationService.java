
package com.kjz.www.user.service.impl;

import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.user.domain.UserRelation;
import com.kjz.www.user.mapper.IUserRelationMapper;
import com.kjz.www.user.service.IUserRelationService;
import com.kjz.www.user.vo.UserRelationVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userRelationService")
public class UserRelationService extends AbstractService<UserRelation, UserRelationVo> implements IUserRelationService {

	public UserRelationService() {
		this.setTableName("kjz_user_relation");
	}
	@Resource
	private IUserRelationMapper userRelationMapper;

	@Override
	protected IOperations<UserRelation, UserRelationVo> getMapper() {
		return userRelationMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

