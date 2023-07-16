package com.lee.service.impl;

import com.lee.mapper.HasApprovalMapper;
import com.lee.pojo.HasApproval;
import com.lee.pojo.HasApprovalExample;
import com.lee.service.HasApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HasApprovalServiceImpl implements HasApprovalService {

    @Autowired
    private HasApprovalMapper hasApprovalMapper;

    @Override
    public boolean getIsApproval(Long id, Long articleId) {
        HasApprovalExample example = new HasApprovalExample();
        example.createCriteria().andArticleIdEqualTo(articleId).andApprovalerIdEqualTo(id);
        List<HasApproval> list = hasApprovalMapper.selectByExample(example);
        return list.size() != 0;
    }

    @Override
    public int insertOne(Long articleId, Long userId) {
        HasApproval approval = new HasApproval();
        approval.setArticleId(articleId);
        approval.setApprovalerId(userId);
        return hasApprovalMapper.insert(approval);
    }

    @Override
    public int deleteOne(Long articleId, Long userId) {
        HasApprovalExample example = new HasApprovalExample();
        example.createCriteria().andApprovalerIdEqualTo(userId).andArticleIdEqualTo(articleId);
        return hasApprovalMapper.deleteByExample(example);
    }
}
