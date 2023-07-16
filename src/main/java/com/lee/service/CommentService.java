package com.lee.service;

import com.lee.pojo.Comment;

import java.util.List;

public interface CommentService {
    /**
     * 根据文章id查询
     * @param articleId
     * @return
     */
    List<Comment> getByArticleId(Long articleId);

    /**
     * 按replyId查询
     * @param id
     * @return
     */
    List<Comment> getByReplyId(Integer id);

    /**
     * 主键查询
     * @param replyReplyId
     * @return
     */
    Comment getByKey(Long replyReplyId);

    /**
     * 添加一条评论
     * @param comment
     * @return
     */
    int insertOne(Comment comment);

    Comment getById(Long id);
}
