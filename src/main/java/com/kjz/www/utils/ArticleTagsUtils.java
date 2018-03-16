package com.kjz.www.utils;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.kjz.www.article.domain.ArticleTags;
import com.kjz.www.article.service.IArticleTagsService;


@Component("articleTagsUtils")
public class ArticleTagsUtils {
	

	
	
	
	//设置一个ArticleTags实例
	public ArticleTags setEntity(Integer articleId,String tagsId,String tbStatus){
		//Integer articleIdNumeri = articleId;
		Integer tagsIdNumeri = Integer.parseInt(tagsId);
			
		ArticleTags articleTagsEntity=new ArticleTags();
		articleTagsEntity.setArticleId(articleId);
		articleTagsEntity.setArticleId(tagsIdNumeri);
		articleTagsEntity.setTbStatus(tbStatus);
		return articleTagsEntity;

	}
	

}
