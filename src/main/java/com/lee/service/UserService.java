package com.lee.service;

import com.github.pagehelper.PageInfo;
import com.lee.pojo.User;

import java.util.List;

public interface UserService {
    /**
     * 按ID查询
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 按邮箱查询
     * @param email
     * @return
     */
    User getUserByEmail(String email);

    /**
     * 更新登录时间
     * @param userByEmail
     * @return
     */
    int updateInfo(User userByEmail);

    /**
     * 粉丝
     * @param id
     * @return
     */
    List<User> getFans(Long id);

    /**
     * 关注的人
     * @param id
     * @return
     */
    List<User> getFollowers(Long id);

    /**
     * 判断邮箱是否已注册
     * @param email
     * @return
     */
    boolean getHasEmail(String email);

    /**
     * 用户分页
     * @param pageNum
     * @return
     */
    PageInfo<User> getUserPage(Integer pageNum);

    /**
     * 用户删除
     * @param userId
     * @return
     */
    int deleteUser(Long userId);
}
