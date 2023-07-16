package com.lee.service.impl;

import com.lee.mapper.TagMapper;
import com.lee.pojo.Tag;
import com.lee.pojo.TagExample;
import com.lee.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> getAllTag() {
        return tagMapper.selectByExample(null);
    }

    @Override
    public List<String> getAllTagGroup() {
        List<String> res = new ArrayList<>();
        List<Tag> tagList = tagMapper.selectByExample(null);
        for (Tag t:tagList) {
            res.add(t.getGroupName());
        }
        res = res.stream().distinct().collect(Collectors.toList());
        return res;
    }

    @Override
    public List<Tag> getTagByName(String t) {
        TagExample example = new TagExample();
        example.createCriteria().andNameEqualTo(t);
        return tagMapper.selectByExample(example);
    }
}
