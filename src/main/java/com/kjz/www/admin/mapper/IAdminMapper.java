
package com.kjz.www.admin.mapper;
import com.kjz.www.common.IOperations;
import com.kjz.www.admin.domain.Admin;
import com.kjz.www.admin.vo.AdminVo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

//@Resource
public interface IAdminMapper extends IOperations<Admin, AdminVo> {
}

