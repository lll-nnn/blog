package com.lee.service.impl;

import com.lee.mapper.TagArticleMappingMapper;
import com.lee.mapper.TagMapper;
import com.lee.pojo.Tag;
import com.lee.pojo.TagArticleMapping;
import com.lee.pojo.TagArticleMappingExample;
import com.lee.pojo.TagExample;
import com.lee.service.TagArticleMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TagArticleMappingServiceImpl implements TagArticleMappingService {

    @Autowired
    private TagArticleMappingMapper tagArticleMappingMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> getTagsByPostId(Long articleId) {
        TagArticleMappingExample example = new TagArticleMappingExample();
        example.createCriteria().andPostsIdEqualTo(articleId);
        List<TagArticleMapping> tagArticleMappings = tagArticleMappingMapper.selectByExample(example);
        List<Tag> res = new ArrayList<>();
        for (TagArticleMapping t :
                tagArticleMappings) {
            Tag tag = tagMapper.selectByPrimaryKey(t.getTagId());
            res.add(tag);
        }
        return res;
    }

    @Override
    public int updateArticleTags(List<String> tags, Long articleId) {
        int res = 1;
        TagArticleMappingExample tagArticleMappingExample = new TagArticleMappingExample();
        tagArticleMappingExample.createCriteria().andPostsIdEqualTo(articleId);
        int i1 = tagArticleMappingMapper.deleteByExample(tagArticleMappingExample);
        if(i1 == 0){
            res = 0;
        }
        for (String t :
                tags) {
            TagExample example = new TagExample();
            example.createCriteria().andNameEqualTo(t);
            List<Tag> tagList = tagMapper.selectByExample(example);
            TagArticleMapping mapping = new TagArticleMapping();
            mapping.setTagId(tagList.get(0).getId());
            mapping.setPostsId(articleId);
            mapping.setIsDelete((byte)0);
            mapping.setCreateAt(new Date());
            mapping.setUpdateAt(new Date());
            int i = tagArticleMappingMapper.insert(mapping);
            if (i == 0){
                res = 0;
            }
        }

        return res;
    }
}
