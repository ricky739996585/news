
package com.kjz.www.article.mapper;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.domain.ArticleCollect;
import com.kjz.www.article.vo.ArticleCollectVo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IArticleCollectMapper extends IOperations<ArticleCollect, ArticleCollectVo> {
    //根据UserId 获取收藏文章的基本信息
    public List<Map<String,Object>> getCollectArticleByUserId(LinkedHashMap<String, String> paramMap);
    //获取收藏文章的总数
    public List<Map<String,Object>> getCollectArticleCountByUserId(int userId);

}

