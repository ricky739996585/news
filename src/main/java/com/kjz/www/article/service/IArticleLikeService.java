
package com.kjz.www.article.service;
import com.kjz.www.common.IServiceOperations;
import com.kjz.www.article.domain.ArticleLike;
import com.kjz.www.article.vo.ArticleLikeVo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IArticleLikeService extends IServiceOperations<ArticleLike, ArticleLikeVo> {
    public List<Map<String,Object>> getLikeArticleByUserId(LinkedHashMap<String, String> paramMap);

    public List<Map<String,Object>> getLikeArticleCountByUserId(int userId);
}

