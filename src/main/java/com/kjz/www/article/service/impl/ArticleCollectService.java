
package com.kjz.www.article.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.mapper.IArticleCollectMapper;
import com.kjz.www.article.domain.ArticleCollect;
import com.kjz.www.article.service.IArticleCollectService;
import com.kjz.www.article.vo.ArticleCollectVo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("articleCollectService")
public class ArticleCollectService extends AbstractService<ArticleCollect, ArticleCollectVo> implements IArticleCollectService {

	public ArticleCollectService() {
		this.setTableName("kjz_article_collect");
	}
	@Resource
	private IArticleCollectMapper articleCollectMapper;

	@Override
	protected IOperations<ArticleCollect, ArticleCollectVo> getMapper() {
		return articleCollectMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}

	@Override
	public List<Map<String, Object>> getCollectArticleByUserId(LinkedHashMap<String, String> paramMap) {
		return articleCollectMapper.getCollectArticleByUserId(paramMap);
	}

	@Override
	public List<Map<String, Object>> getCollectArticleCountByUserId(int userId) {
		return articleCollectMapper.getCollectArticleCountByUserId(userId);
	}
}

