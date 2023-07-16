package com.lee.service.impl;

import com.lee.mapper.UserFollowMapper;
import com.lee.pojo.UserFollow;
import com.lee.pojo.UserFollowExample;
import com.lee.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;

    @Override
    public boolean getIsFollow(Long id, Long authorId) {
        UserFollowExample example = new UserFollowExample();
        example.createCriteria().andFollowedEqualTo(authorId).andFollowerEqualTo(id);
        List<UserFollow> userFollows = userFollowMapper.selectByExample(example);
        return userFollows.size() != 0;
    }

    @Override
    public int followOne(UserFollow userFollow) {
        return userFollowMapper.insert(userFollow);
    }

    @Override
    public int followCancel(Long followerId, Long followedId) {
        UserFollowExample example = new UserFollowExample();
        example.createCriteria().andFollowerEqualTo(followedId).andFollowedEqualTo(followedId);

        return userFollowMapper.deleteByExample(example);
    }
}
