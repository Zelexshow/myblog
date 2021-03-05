package com.zelex.service;

import com.zelex.po.Blog;
import com.zelex.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @Author Zelex
 * @Date 2021/2/20 16:36
 * @Version 1.0
 */
public interface BlogService {
    Blog getBlog(Long id);

    Blog getAndConvert(Long id);//用于将普通的博客转成markdown格式

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);
    //关联查询对应标签的博客
    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();//string对应年份，list对应博客列表，该方法用于归档

    Long countBlog();//统计博客数

    Blog saveBlog(Blog blog);


    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);
}
