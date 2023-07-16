package com.lee.pojo;

import java.util.Date;

public class UserFollow {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_follow.id
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_follow.followed
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    private Long followed;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_follow.followed_type
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    private String followedType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_follow.follower
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    private Long follower;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_follow.is_delete
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    private Boolean isDelete;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_follow.create_at
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    private Date createAt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_follow.update_at
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    private Date updateAt;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_follow.id
     *
     * @return the value of user_follow.id
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_follow.id
     *
     * @param id the value for user_follow.id
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_follow.followed
     *
     * @return the value of user_follow.followed
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public Long getFollowed() {
        return followed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_follow.followed
     *
     * @param followed the value for user_follow.followed
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public void setFollowed(Long followed) {
        this.followed = followed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_follow.followed_type
     *
     * @return the value of user_follow.followed_type
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public String getFollowedType() {
        return followedType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_follow.followed_type
     *
     * @param followedType the value for user_follow.followed_type
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public void setFollowedType(String followedType) {
        this.followedType = followedType == null ? null : followedType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_follow.follower
     *
     * @return the value of user_follow.follower
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public Long getFollower() {
        return follower;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_follow.follower
     *
     * @param follower the value for user_follow.follower
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public void setFollower(Long follower) {
        this.follower = follower;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_follow.is_delete
     *
     * @return the value of user_follow.is_delete
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_follow.is_delete
     *
     * @param isDelete the value for user_follow.is_delete
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_follow.create_at
     *
     * @return the value of user_follow.create_at
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_follow.create_at
     *
     * @param createAt the value for user_follow.create_at
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_follow.update_at
     *
     * @return the value of user_follow.update_at
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_follow.update_at
     *
     * @param updateAt the value for user_follow.update_at
     *
     * @mbggenerated Wed Nov 23 20:51:10 CST 2022
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}