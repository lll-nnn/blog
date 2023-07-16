package com.lee.service;

import com.lee.pojo.Tag;

import java.util.List;

public interface TagArticleMappingService {
    List<Tag> getTagsByPostId(Long articleId);

    /**
     * 更新文章tags
     * @param tags
     * @param articleId
     * @return
     */
    int updateArticleTags(List<String> tags, Long articleId);
}
