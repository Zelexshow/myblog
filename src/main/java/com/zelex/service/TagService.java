package com.zelex.service;

import com.zelex.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author Zelex
 * @Date 2021/2/19 19:35
 * @Version 1.0
 */
public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();
    //根据多个id来获取博客
    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);

    Tag updateTag(Long id,Tag tag);

    void deleteTag(Long id);
}
