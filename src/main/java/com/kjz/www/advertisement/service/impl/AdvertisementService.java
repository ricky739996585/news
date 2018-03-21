
package com.kjz.www.advertisement.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.advertisement.mapper.IAdvertisementMapper;
import com.kjz.www.advertisement.domain.Advertisement;
import com.kjz.www.advertisement.service.IAdvertisementService;
import com.kjz.www.advertisement.vo.AdvertisementVo;
@Service("advertisementService")
public class AdvertisementService extends AbstractService<Advertisement, AdvertisementVo> implements IAdvertisementService {

	public AdvertisementService() {
		this.setTableName("kjz_advertisement");
	}
	@Resource
	private IAdvertisementMapper advertisementMapper;

	@Override
	protected IOperations<Advertisement, AdvertisementVo> getMapper() {
		return advertisementMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

