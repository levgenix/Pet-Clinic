package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.CommentReaction;
import com.vet24.models.user.CommentReactionId;
import org.springframework.stereotype.Repository;

@Repository
public class CommentReactionDaoImpl extends ReadWriteDaoImpl<CommentReactionId, CommentReaction> implements CommentReactionDao {
}
