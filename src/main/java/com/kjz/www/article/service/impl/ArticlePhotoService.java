
package com.kjz.www.article.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.kjz.www.common.AbstractService;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.mapper.IArticlePhotoMapper;
import com.kjz.www.article.domain.ArticlePhoto;
import com.kjz.www.article.service.IArticlePhotoService;
import com.kjz.www.article.vo.ArticlePhotoVo;
@Service("articlePhotoService")
public class ArticlePhotoService extends AbstractService<ArticlePhoto, ArticlePhotoVo> implements IArticlePhotoService {

	public ArticlePhotoService() {
		this.setTableName("kjz_article_photo");
	}
	@Resource
	private IArticlePhotoMapper articlePhotoMapper;

	@Override
	protected IOperations<ArticlePhoto, ArticlePhotoVo> getMapper() {
		return articlePhotoMapper;
	}
	@Override
	public void setTableName(String tableName){
		this.tableName = tableName;;
	}
}

