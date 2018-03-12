
package com.kjz.www.article.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.mapper.IArticleCommentMapper;
import com.kjz.www.article.domain.ArticleComment;
import com.kjz.www.article.service.IArticleCommentService;
import com.kjz.www.article.vo.ArticleCommentVo;
@Service("articleCommentService")
public class ArticleCommentService extends AbstractService<ArticleComment, ArticleCommentVo> implements IArticleCommentService {

	public ArticleCommentService() {
		this.setTableName("kjz_article_comment");
	}
	@Resource
	private IArticleCommentMapper articleCommentMapper;

	@Override
	protected IOperations<ArticleComment, ArticleCommentVo> getMapper() {
		return articleCommentMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

