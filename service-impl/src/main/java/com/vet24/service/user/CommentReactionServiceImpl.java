package com.vet24.service.user;

import com.vet24.dao.user.CommentReactionDao;
import com.vet24.models.user.CommentReaction;
import com.vet24.models.user.CommentReactionId;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionServiceImpl extends ReadWriteServiceImpl<CommentReactionId, CommentReaction>  implements CommentReactionService {

    private final CommentReactionDao likeDao;

    public CommentReactionServiceImpl(CommentReactionDao likeDao) {
        super(likeDao);
        this.likeDao = likeDao;
    }
}
