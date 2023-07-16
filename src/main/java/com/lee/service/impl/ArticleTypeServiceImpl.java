package com.lee.service.impl;

import com.lee.mapper.ArticleTypeMapper;
import com.lee.pojo.ArticleType;
import com.lee.pojo.ArticleTypeExample;
import com.lee.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArticleTypeServiceImpl implements ArticleTypeService {

    @Autowired
    private ArticleTypeMapper articleTypeMapper;

    @Override
    public List<ArticleType> selectAllType() {
       return articleTypeMapper.selectByExample(null);
    }

    @Override
    public String getTypeName(Long typeId) {
        return articleTypeMapper.selectByPrimaryKey(typeId).getName();
    }

    @Override
    public Long getIdByName(String typeName) {
        ArticleTypeExample example = new ArticleTypeExample();
        example.createCriteria().andNameEqualTo(typeName);
        List<ArticleType> typeList = articleTypeMapper.selectByExample(example);
        return typeList.get(0).getId();
    }

    @Override
    public List<String> getAllTypes() {
        List<ArticleType> typeList = articleTypeMapper.selectByExample(null);
        List<String> res = new ArrayList<>();
        for (ArticleType t :
                typeList) {
            res.add(t.getName());
        }
        return res;
    }
}
