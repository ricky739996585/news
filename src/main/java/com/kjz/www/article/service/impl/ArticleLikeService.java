
package com.kjz.www.article.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.mapper.IArticleLikeMapper;
import com.kjz.www.article.domain.ArticleLike;
import com.kjz.www.article.service.IArticleLikeService;
import com.kjz.www.article.vo.ArticleLikeVo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("articleLikeService")
public class ArticleLikeService extends AbstractService<ArticleLike, ArticleLikeVo> implements IArticleLikeService {

	public ArticleLikeService() {
		this.setTableName("kjz_article_like");
	}
	@Resource
	private IArticleLikeMapper articleLikeMapper;

	@Override
	protected IOperations<ArticleLike, ArticleLikeVo> getMapper() {
		return articleLikeMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}

	@Override
	public List<Map<String, Object>> getLikeArticleByUserId(LinkedHashMap<String, String> paramMap) {
		return articleLikeMapper.getLikeArticleByUserId(paramMap);
	}

	@Override
	public List<Map<String, Object>> getLikeArticleCountByUserId(int userId) {
		return articleLikeMapper.getLikeArticleCountByUserId(userId);
	}
}

