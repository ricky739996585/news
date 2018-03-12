
package com.kjz.www.user.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.user.mapper.IUserMapper;
import com.kjz.www.user.domain.User;
import com.kjz.www.user.service.IUserService;
import com.kjz.www.user.vo.UserVo;
@Service("userService")
public class UserService extends AbstractService<User, UserVo> implements IUserService {

	public UserService() {
		this.setTableName("kjz_user");
	}
	@Resource
	private IUserMapper userMapper;

	@Override
	protected IOperations<User, UserVo> getMapper() {
		return userMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

