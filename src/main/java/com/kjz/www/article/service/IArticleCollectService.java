
package com.kjz.www.article.service;
import com.kjz.www.common.IServiceOperations;
import com.kjz.www.article.domain.ArticleCollect;
import com.kjz.www.article.vo.ArticleCollectVo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IArticleCollectService extends IServiceOperations<ArticleCollect, ArticleCollectVo> {

    public List<Map<String,Object>> getCollectArticleByUserId(LinkedHashMap<String, String> paramMap);

    public List<Map<String,Object>> getCollectArticleCountByUserId(int userId);
}

