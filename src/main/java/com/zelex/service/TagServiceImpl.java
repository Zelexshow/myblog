package com.zelex.service;

import com.zelex.NotFoundException;
import com.zelex.dao.TagRepository;
import com.zelex.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Zelex
 * @Date 2021/2/19 19:40
 * @Version 1.0
 */
@Service
public class TagServiceImpl implements TagService {


    @Autowired
    private TagRepository tagRepository;
    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        Tag save = tagRepository.save(tag);
        return save;
    }
    @Transactional
    @Override
    public Tag getTag(Long id) {
        Tag tag = tagRepository.findById(id).get();
        return tag;
    }
    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findTagByName(name);
    }
    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        Page<Tag> p = tagRepository.findAll(pageable);
        return p;
    }

    @Transactional
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {//1.2.3
        List<Tag> allById = tagRepository.findAllById(convertToList(ids));
        return allById;
    }
    @Transactional
    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size,sort);
        return tagRepository.findTop(pageable);
    }

    private List<Long> convertToList(String ids){
        List<Long> list=new ArrayList<>();
        if (!"".equals(ids) && ids!=null){
            String[] split = ids.split(",");
            for (int i=0;i<split.length;i++){
                list.add(new Long(split[i]));
            }
        }
        return list;
    }
    @Transactional
    @Override
    public Tag updateTag(Long id,Tag tag) {
        Tag tag1 = getTag(id);
        if (tag1 == null){
            throw new NotFoundException("不存在这样的标签");
        }
        BeanUtils.copyProperties(tag,tag1);
        return tagRepository.save(tag1);
    }
    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
