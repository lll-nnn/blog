package com.lee.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mapper.*;
import com.lee.pojo.*;
import com.lee.service.ArticleService;
import com.lee.utils.SensitiveFilter;
import com.lee.utils.TransformStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lee.utils.RedisConstants.APPROVAL_KEY_PREFIX;
import static com.lee.utils.RedisConstants.VIEW_KEY_PREFIX;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TagArticleMappingMapper tagArticleMappingMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTypeMapper articleTypeMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public PageInfo<Map<String, Object>> getArticlePage(Integer pageNum) {
        PageHelper.startPage(pageNum, 15);
//        List<Article> articleList = articleMapper.selectByExample(null);
        List<ArticleWithBLOBs> articleWithBLOBsList = articleMapper.selectByExampleWithBLOBs(null);

        List<Map<String, Object>> res = new ArrayList<>();
        res = getArticleList(articleWithBLOBsList);

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(res);
        return pageInfo;
    }

    @Override
    public int insertOne(ArticleWithBLOBs articleWithBLOBs, List<TagArticleMapping> tams) {
        ArticleExample example = new ArticleExample();
        int i = articleMapper.insert(articleWithBLOBs);
        Long id = articleWithBLOBs.getId();
        for (TagArticleMapping t :
                tams) {
            t.setPostsId(id);
            tagArticleMappingMapper.insert(t);
        }

        return i;
    }

    @Override
    public ArticleWithBLOBs getArticleInfo(Long id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ArticleWithBLOBs> getAuthorArticle(Long authorId) {
        ArticleExample example = new ArticleExample();
        example.setOrderByClause("`update_at` DESC");
        example.createCriteria().andAuthorIdEqualTo(authorId);
        return articleMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public int updateViews(Long id) {
        String redisKey = VIEW_KEY_PREFIX + id;
        ArticleWithBLOBs articleWithBLOBs = articleMapper.selectByPrimaryKey(id);
        articleWithBLOBs.setViews(articleWithBLOBs.getViews()+1);
        return articleMapper.updateByPrimaryKeyWithBLOBs(articleWithBLOBs);
    }

    @Override
    public int deleteOne(Long id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Long addApproval(Long id) {
        String redisKey = APPROVAL_KEY_PREFIX + id;
        ArticleWithBLOBs articleWithBLOBs = articleMapper.selectByPrimaryKey(id);
        Long approvals = articleWithBLOBs.getApprovals()+1;
        articleWithBLOBs.setApprovals(approvals);
        articleMapper.updateByPrimaryKeyWithBLOBs(articleWithBLOBs);
        return approvals;
    }

    @Override
    public Long cancelApproval(Long articleId) {
        ArticleWithBLOBs articleWithBLOBs = articleMapper.selectByPrimaryKey(articleId);
        Long approvals = articleWithBLOBs.getApprovals()-1;
        articleWithBLOBs.setApprovals(approvals);
        articleMapper.updateByPrimaryKeyWithBLOBs(articleWithBLOBs);
        return approvals;
    }

    @Override
    public List<Map<String, Object>> getAllArticle(Long userId) {
        ArticleExample example = new ArticleExample();
        example.createCriteria().andAuthorIdEqualTo(userId);
        List<ArticleWithBLOBs> articleWithBLOBsList = articleMapper.selectByExampleWithBLOBs(example);
        return getArticleList(articleWithBLOBsList);
    }

    @Override
    public int updateComments(Long articleId) {
        ArticleWithBLOBs article = articleMapper.selectByPrimaryKey(articleId);
        article.setComments(article.getComments() + 1);
        return articleMapper.updateByPrimaryKeyWithBLOBs(article);
    }

    @Override
    public int updateArticle(ArticleWithBLOBs articleInfo) {
        return articleMapper.updateByPrimaryKeyWithBLOBs(articleInfo);
    }

    @Override
    public PageInfo<Map<String, Object>> getSearchArticle(Integer pageNum, String key) {
        List<ArticleWithBLOBs> articleWithBLOBsList = articleMapper.searchArticle(key);
        List<Map<String, Object>> articleList = getArticleList(articleWithBLOBsList);
        PageHelper.startPage(pageNum, 15);
        return new PageInfo<>(articleList);
    }

    @Override
    public PageInfo<Map<String, Object>> getTagArticle(String tagName, Integer pageNum) {
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andNameEqualTo(tagName);
        List<Tag> tagList = tagMapper.selectByExample(tagExample);
        TagArticleMappingExample example = new TagArticleMappingExample();
        example.createCriteria().andTagIdEqualTo(tagList.get(0).getId());
        List<TagArticleMapping> tagArticleMappings = tagArticleMappingMapper.selectByExample(example);
        List<ArticleWithBLOBs> articleWithBLOBsList = new ArrayList<>();
        for (TagArticleMapping t :
                tagArticleMappings) {
            ArticleWithBLOBs article = articleMapper.selectByPrimaryKey(t.getPostsId());
            articleWithBLOBsList.add(article);
        }
        List<Map<String, Object>> articleList = getArticleList(articleWithBLOBsList);
        PageHelper.startPage(pageNum, 15);
        return new PageInfo<>(articleList);
    }

    @Override
    public PageInfo<Map<String, Object>> getTypeArticle(Integer pageNum2, String typeName) {
        ArticleTypeExample example = new ArticleTypeExample();
        example.createCriteria().andNameEqualTo(typeName);
        List<ArticleType> articleType = articleTypeMapper.selectByExample(example);
        ArticleExample example1 = new ArticleExample();
        example1.createCriteria().andTypeIdEqualTo(articleType.get(0).getId());
        List<ArticleWithBLOBs> articleWithBLOBsList = articleMapper.selectByExampleWithBLOBs(example1);
        List<Map<String, Object>> articleList = getArticleList(articleWithBLOBsList);
        PageHelper.startPage(pageNum2, 15);
        return new PageInfo<>(articleList);
    }

    public List<Map<String, Object>> getArticleList(List<ArticleWithBLOBs> articleWithBLOBsList){
        List<Map<String, Object>> res = new ArrayList<>();
        for (ArticleWithBLOBs articleWithBLOBs : articleWithBLOBsList) {
            Map<String, Object> articleMap = new HashMap<>();
            Long articleId = articleWithBLOBs.getId();
            articleMap.put("id", articleId);
            articleMap.put("category", articleWithBLOBs.getCategory());
            Long userId = articleWithBLOBs.getAuthorId();
            articleMap.put("authorId", userId);
            articleMap.put("title", articleWithBLOBs.getTitle());
            articleMap.put("desc", sensitiveFilter.filter(TransformStringUtil.getIntroduction(
                    articleWithBLOBs.getMarkdownContent(),
                    !articleWithBLOBs.getHeadImg().equals(""))));
            articleMap.put("headImg", articleWithBLOBs.getHeadImg());
            User user = userMapper.selectByPrimaryKey(userId);
            articleMap.put("authorHeadImg", user.getAvatar());
            articleMap.put("authorName", user.getNickname());
            Date createdAt = articleWithBLOBs.getCreateAt();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            String format = sdf.format(createdAt);
            articleMap.put("createdAt", format);
            articleMap.put("views", articleWithBLOBs.getViews());
            articleMap.put("approvals", articleWithBLOBs.getApprovals());
            articleMap.put("comments", articleWithBLOBs.getComments());
            TagArticleMappingExample example1 = new TagArticleMappingExample();
            example1.createCriteria().andPostsIdEqualTo(articleId);
            List<TagArticleMapping> tagArticleMappings = tagArticleMappingMapper.selectByExample(example1);
            List<Tag> tagList = new ArrayList<>();
            for (TagArticleMapping t : tagArticleMappings) {
                Long tagId = t.getTagId();
                Tag tag = tagMapper.selectByPrimaryKey(tagId);
                tagList.add(tag);
            }
            articleMap.put("tagList", tagList);
            res.add(articleMap);
        }
        return res;
    }
}
