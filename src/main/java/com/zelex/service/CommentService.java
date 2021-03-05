package com.zelex.service;

import com.zelex.po.Comment;

import java.util.List;

/**
 * @Author Zelex
 * @Date 2021/2/22 17:48
 * @Version 1.0
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
