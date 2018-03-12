
package com.kjz.www.article.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.mapper.IArticleTagsMapper;
import com.kjz.www.article.domain.ArticleTags;
import com.kjz.www.article.service.IArticleTagsService;
import com.kjz.www.article.vo.ArticleTagsVo;
@Service("articleTagsService")
public class ArticleTagsService extends AbstractService<ArticleTags, ArticleTagsVo> implements IArticleTagsService {

	public ArticleTagsService() {
		this.setTableName("kjz_article_tags");
	}
	@Resource
	private IArticleTagsMapper articleTagsMapper;

	@Override
	protected IOperations<ArticleTags, ArticleTagsVo> getMapper() {
		return articleTagsMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

