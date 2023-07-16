package com.lee.service;

import com.lee.pojo.UserFollow;

public interface UserFollowService {
    /**
     * 是否关注
     * @param id
     * @param authorId
     * @return
     */
    boolean getIsFollow(Long id, Long authorId);

    /**
     * 关注
     * @param userFollow
     * @return
     */
    int followOne(UserFollow userFollow);

    /**
     * 关注取消
     * @param followerId
     * @param followedId
     * @return
     */
    int followCancel(Long followerId, Long followedId);
}
