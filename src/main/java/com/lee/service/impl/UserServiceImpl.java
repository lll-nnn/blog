package com.lee.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lee.mapper.UserFollowMapper;
import com.lee.mapper.UserMapper;
import com.lee.pojo.User;
import com.lee.pojo.UserExample;
import com.lee.pojo.UserFollow;
import com.lee.pojo.UserFollowExample;
import com.lee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional  //事务管理
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserFollowMapper followMapper;

    /**
     * 按ID查询
     * @param id
     * @return
     */
    @Override
    public User getUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User getUserByEmail(String email) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0){
            return null;
        }
        return users.get(0);
    }

    @Override
    public int updateInfo(User userByEmail) {
        return userMapper.updateByPrimaryKey(userByEmail);
    }

    @Override
    public List<User> getFans(Long id) {
        List<User> res = new ArrayList<>();
        UserFollowExample userFollowExample = new UserFollowExample();
        userFollowExample.createCriteria().andFollowedEqualTo(id);
        List<UserFollow> userFollows = followMapper.selectByExample(userFollowExample);
        for (UserFollow u :
                userFollows) {
            User user = userMapper.selectByPrimaryKey(u.getFollower());
            res.add(user);
        }
        return res;
    }

    @Override
    public List<User> getFollowers(Long id) {
        List<User> res = new ArrayList<>();
        UserFollowExample userFollowExample = new UserFollowExample();
        userFollowExample.createCriteria().andFollowerEqualTo(id);
        List<UserFollow> userFollows = followMapper.selectByExample(userFollowExample);
        for (UserFollow u :
                userFollows) {
            User user = userMapper.selectByPrimaryKey(u.getFollowed());
            res.add(user);
        }
        return res;
    }

    @Override
    public boolean getHasEmail(String email) {
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(email);
        List<User> userList = userMapper.selectByExample(example);
        return userList.size() > 1;
    }

    @Override
    public PageInfo<User> getUserPage(Integer pageNum) {
        List<User> userList = userMapper.selectByExample(null);
        PageHelper.startPage(pageNum, 15);
        return new PageInfo<>(userList);
    }

    @Override
    public int deleteUser(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setIsDelete((byte)1);
        return userMapper.updateByPrimaryKey(user);
    }

}
