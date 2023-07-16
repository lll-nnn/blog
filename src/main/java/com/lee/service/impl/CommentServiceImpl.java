package com.lee.service.impl;

import com.lee.mapper.CommentMapper;
import com.lee.pojo.Comment;
import com.lee.pojo.CommentExample;
import com.lee.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getByArticleId(Long articleId) {
//        CommentExample example = new CommentExample();
//        example.createCriteria().andPostsIdEqualTo(articleId);
        List<Comment> comments = commentMapper.getByArticleId(articleId);
        return comments;
    }

    @Override
    public List<Comment> getByReplyId(Integer id) {
//        CommentExample example = new CommentExample();
//        example.createCriteria().andReplyIdEqualTo(Long.valueOf(id));
//        List<Comment> comments = commentMapper.selectByExample(example);
//        return comments;
        return commentMapper.getByReplyId(id);
    }

    @Override
    public Comment getByKey(Long replyReplyId) {
        return commentMapper.selectByPrimaryKey(Math.toIntExact(replyReplyId));
    }

    @Override
    public int insertOne(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public Comment getById(Long id) {
        return commentMapper.selectByPrimaryKey(Math.toIntExact(id));
    }
}
