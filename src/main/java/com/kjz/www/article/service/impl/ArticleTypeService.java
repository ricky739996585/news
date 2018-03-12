
package com.kjz.www.article.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.mapper.IArticleTypeMapper;
import com.kjz.www.article.domain.ArticleType;
import com.kjz.www.article.service.IArticleTypeService;
import com.kjz.www.article.vo.ArticleTypeVo;
@Service("articleTypeService")
public class ArticleTypeService extends AbstractService<ArticleType, ArticleTypeVo> implements IArticleTypeService {

	public ArticleTypeService() {
		this.setTableName("kjz_article_type");
	}
	@Resource
	private IArticleTypeMapper articleTypeMapper;

	@Override
	protected IOperations<ArticleType, ArticleTypeVo> getMapper() {
		return articleTypeMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

