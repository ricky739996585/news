package com.kjz.www.utils;

import org.springframework.stereotype.Component;

import com.kjz.www.article.domain.ArticlePhoto;

@Component("articlePhotoUtils")
public class ArticlePhotoUtils {


	//设置一个ArticlePhoto实例
	public ArticlePhoto setEntity(Integer articleId,String articlePhotoUrl,String tbStatus){
		
		ArticlePhoto articlePhotoEntity=new ArticlePhoto();
		articlePhotoEntity.setArticleId(articleId);
		articlePhotoEntity.setArticlePhotoUrl(articlePhotoUrl);
		articlePhotoEntity.setTbStatus(tbStatus);
		return articlePhotoEntity;
		}
	
}
