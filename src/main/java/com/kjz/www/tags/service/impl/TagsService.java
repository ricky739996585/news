
package com.kjz.www.tags.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.tags.mapper.ITagsMapper;
import com.kjz.www.tags.domain.Tags;
import com.kjz.www.tags.service.ITagsService;
import com.kjz.www.tags.vo.TagsVo;
@Service("tagsService")
public class TagsService extends AbstractService<Tags, TagsVo> implements ITagsService {

	public TagsService() {
		this.setTableName("kjz_tags");
	}
	@Resource
	private ITagsMapper tagsMapper;

	@Override
	protected IOperations<Tags, TagsVo> getMapper() {
		return tagsMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

