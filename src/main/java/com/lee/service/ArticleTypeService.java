package com.lee.service;

import com.lee.pojo.ArticleType;

import java.util.List;

public interface ArticleTypeService {

    /**
     * 查询所有
     * @return
     */
    List<ArticleType> selectAllType();

    /**
     * 类型
     * @param typeId
     * @return
     */
    String getTypeName(Long typeId);

    /**
     * 查询Id
     * @param typeName
     * @return
     */
    Long getIdByName(String typeName);

    /**
     * 所有类型
     * @return
     */
    List<String> getAllTypes();
}
