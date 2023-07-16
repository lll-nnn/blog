package com.lee.service;

public interface HasApprovalService {
    /**
     * 是否已点赞
     * @param id
     * @param articleId
     * @return
     */
    boolean getIsApproval(Long id, Long articleId);

    /**
     * 更新点赞表
     * @param articleId
     * @param userId
     * @return
     */
    int insertOne(Long articleId, Long userId);

    /**
     * 取消点赞
     * @param articleId
     * @param userId
     * @return
     */
    int deleteOne(Long articleId, Long userId);
}
