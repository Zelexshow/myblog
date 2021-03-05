package com.zelex.service;

import com.zelex.NotFoundException;
import com.zelex.dao.BlogRepository;
import com.zelex.po.Blog;
import com.zelex.util.MarkdownUtils;
import com.zelex.util.MyBeanUtils;
import com.zelex.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Author Zelex
 * @Date 2021/2/20 16:38
 * @Version 1.0
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        Blog one = blogRepository.getOne(id);
        return one;
    }

    //用于博客详情展示，对markdown中的文字进行了转换

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return b;
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        Page<Blog> all = blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                //添加第一个条件，标题
                if (!"".equals(blog.getTitle()) && blog.getTitle()!= null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                //根据类型id来进行判断
                if (blog.getTypeId()!= null){
                    predicates.add(cb.equal(root.<String>get("type").get("id"),blog.getTypeId()));
                }
                //根据是否推荐来查询
                if (blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
        return all;
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    //关联查询，关联tag表和blog表，多对多的关系
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");

                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    //用于全局查询的方法
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {

        return blogRepository.findByQuery(query,pageable);
    }

    @Transactional
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        //首先拿到所有的年份
       List<String> list= blogRepository.findGroupYear();
        Map<String, List<Blog>> blogs = new HashMap<>();
        for (String year : list) {
            blogs.put(year,blogRepository.findByYear(year));
        }
        return blogs;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null){
            //做一些初始化
            blog.setViews(0);
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
        }else{
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = getBlog(id);
        if (b == null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return saveBlog(b);
    }
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
