
package com.kjz.www.article.mapper;
import com.kjz.www.common.IOperations;
import com.kjz.www.article.domain.Article;
import com.kjz.www.article.vo.ArticleVo;

import java.util.List;
import java.util.Map;

public interface IArticleMapper extends IOperations<Article, ArticleVo> {
    List<Map<String,Object>> getTagsByArticleId(Integer articleId);
    //Map<'属性名(列表名)','属性值'>
    List<Map<String,Object>> getArticlesByTagId(Integer tagId);
}

