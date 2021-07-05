package com.vet24.service.user;

import com.vet24.dao.user.CommentDao;
import com.vet24.models.user.Comment;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ReadWriteServiceImpl<Long, Comment> implements CommentService {

    private final CommentDao commentDao;

    @Autowired
    public CommentServiceImpl(CommentDao commentDao) {
        super(commentDao);
        this.commentDao = commentDao;
    }
}
