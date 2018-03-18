
package com.kjz.www.article.mapper;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.domain.ArticleLike;
import com.kjz.www.article.vo.ArticleLikeVo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IArticleLikeMapper extends IOperations<ArticleLike, ArticleLikeVo> {

    //根据UserId 获取点赞文章的基本信息
    public List<Map<String,Object>> getLikeArticleByUserId(LinkedHashMap<String, String> paramMap);
    //获取点赞文章的总数
    public List<Map<String,Object>> getLikeArticleCountByUserId(int userId);
}

