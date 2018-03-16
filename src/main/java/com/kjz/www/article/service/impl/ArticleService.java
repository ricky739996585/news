
package com.kjz.www.article.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.mapper.IArticleMapper;
import com.kjz.www.article.domain.Article;
import com.kjz.www.article.service.IArticleService;
import com.kjz.www.article.vo.ArticleVo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("articleService")
public class ArticleService extends AbstractService<Article, ArticleVo> implements IArticleService {

	public ArticleService() {
		this.setTableName("kjz_article");
	}
	@Resource
	private IArticleMapper articleMapper;

	@Override
	protected IOperations<Article, ArticleVo> getMapper() {
		return articleMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}

	@Override
	public List<Map<String, Object>> getTagsByArticleId(Integer articleId) {
		return articleMapper.getTagsByArticleId(articleId);
	}

	@Override
	public List<Map<String, Object>> getArticlesByTagId(Integer tagId) {
		return articleMapper.getArticlesByTagId(tagId);
	}

	@Override
	public List<Map<String, Object>> getArticlesInfoList(LinkedHashMap<String, String> condition, int pageNo, int pageSize, String order) {
		return articleMapper.getArticlesInfoList(condition,pageNo,pageSize,order);
	}
}

