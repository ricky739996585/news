
package com.kjz.www.photo.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.photo.mapper.IPhotoTypeMapper;
import com.kjz.www.photo.domain.PhotoType;
import com.kjz.www.photo.service.IPhotoTypeService;
import com.kjz.www.photo.vo.PhotoTypeVo;
@Service("photoTypeService")
public class PhotoTypeService extends AbstractService<PhotoType, PhotoTypeVo> implements IPhotoTypeService {

	public PhotoTypeService() {
		this.setTableName("kjz_photo_type");
	}
	@Resource
	private IPhotoTypeMapper photoTypeMapper;

	@Override
	protected IOperations<PhotoType, PhotoTypeVo> getMapper() {
		return photoTypeMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

