
package com.kjz.www.admin.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.admin.mapper.IAdminMapper;
import com.kjz.www.admin.domain.Admin;
import com.kjz.www.admin.service.IAdminService;
import com.kjz.www.admin.vo.AdminVo;
@Service("adminService")
public class AdminService extends AbstractService<Admin, AdminVo> implements IAdminService {

	public AdminService() {
		this.setTableName("kjz_admin");
	}
	@Resource
	private IAdminMapper adminMapper;

	@Override
	protected IOperations<Admin, AdminVo> getMapper() {
		return adminMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

