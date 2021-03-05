package com.zelex.dao;

import com.zelex.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author Zelex
 * @Date 2021/2/19 19:40
 * @Version 1.0
 */
public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findTagByName(String name);

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
