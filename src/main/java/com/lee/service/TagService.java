package com.lee.service;

import com.lee.pojo.Tag;

import java.util.List;

public interface TagService {
    /**
     * 查询所有标签
     * @return
     */
    List<Tag> getAllTag();

    /**
     * 查询所有标签组
     * @return
     */
    List<String> getAllTagGroup();

    /**
     * 指定名字查询
     * @param t
     * @return
     */
    List<Tag> getTagByName(String t);
}
