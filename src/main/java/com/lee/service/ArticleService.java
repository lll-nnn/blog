package com.lee.service;

import com.github.pagehelper.PageInfo;
import com.lee.pojo.Article;
import com.lee.pojo.ArticleWithBLOBs;
import com.lee.pojo.TagArticleMapping;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    /**
     * 分页查询所有文章
     * @param pageNum
     * @return
     */
    PageInfo<Map<String, Object>> getArticlePage(Integer pageNum);

    /**
     * 插入一条
     * @param articleWithBLOBs
     * @param tams
     * @return
     */
    int insertOne(ArticleWithBLOBs articleWithBLOBs, List<TagArticleMapping> tams);

    /**
     * id查询
     * @param id
     * @return
     */
    ArticleWithBLOBs getArticleInfo(Long id);

    /**
     * 获取作者文章
     * @param authorId
     * @return
     */
    List<ArticleWithBLOBs> getAuthorArticle(Long authorId);

    /**
     * 更新浏览量
     * @param id
     * @return
     */
    int updateViews(Long id);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteOne(Long id);

    /**
     * 点赞
     * @param id
     * @return
     */
    Long addApproval(Long id);

    /**
     * 取消点赞
     * @param articleId
     * @return
     */
    Long cancelApproval(Long articleId);

    /**
     * 用户所有文章
     * @param userId
     * @return
     */
    List<Map<String, Object>> getAllArticle(Long userId);

    /**
     * 更新评论量
     * @param articleId
     * @return
     */
    int updateComments(Long articleId);

    /**
     * 文章更新
     * @param articleInfo
     * @return
     */
    int updateArticle(ArticleWithBLOBs articleInfo);

    /**
     * search
     * @param pageNum
     * @param key
     * @return
     */
    PageInfo<Map<String, Object>> getSearchArticle(Integer pageNum, String key);

    /**
     * tag-info.html
     * @param tagName
     * @param pageNum
     * @return
     */
    PageInfo<Map<String, Object>> getTagArticle(String tagName, Integer pageNum);

    /**
     *
     * @param pageNum2
     * @param typeName
     * @return
     */
    PageInfo<Map<String, Object>> getTypeArticle(Integer pageNum2, String typeName);
}
