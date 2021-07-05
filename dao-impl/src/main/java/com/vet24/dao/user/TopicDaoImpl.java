package com.vet24.dao.user;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.user.Topic;
import org.springframework.stereotype.Repository;

@Repository
public class TopicDaoImpl extends ReadWriteDaoImpl<Long, Topic> implements TopicDao {
}
