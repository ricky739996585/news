
package com.kjz.www.photo.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.photo.mapper.IPhotoMapper;
import com.kjz.www.photo.domain.Photo;
import com.kjz.www.photo.service.IPhotoService;
import com.kjz.www.photo.vo.PhotoVo;
@Service("photoService")
public class PhotoService extends AbstractService<Photo, PhotoVo> implements IPhotoService {

	public PhotoService() {
		this.setTableName("kjz_photo");
	}
	@Resource
	private IPhotoMapper photoMapper;

	@Override
	protected IOperations<Photo, PhotoVo> getMapper() {
		return photoMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

