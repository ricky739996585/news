
package com.kjz.www.advertisement.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.advertisement.mapper.IAdvertisementTypeMapper;
import com.kjz.www.advertisement.domain.AdvertisementType;
import com.kjz.www.advertisement.service.IAdvertisementTypeService;
import com.kjz.www.advertisement.vo.AdvertisementTypeVo;
@Service("advertisementTypeService")
public class AdvertisementTypeService extends AbstractService<AdvertisementType, AdvertisementTypeVo> implements IAdvertisementTypeService {

	public AdvertisementTypeService() {
		this.setTableName("kjz_advertisement_type");
	}
	@Resource
	private IAdvertisementTypeMapper advertisementTypeMapper;

	@Override
	protected IOperations<AdvertisementType, AdvertisementTypeVo> getMapper() {
		return advertisementTypeMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

