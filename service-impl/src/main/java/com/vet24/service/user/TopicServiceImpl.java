package com.vet24.service.user;

import com.vet24.dao.user.TopicDao;
import com.vet24.models.user.Topic;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl extends ReadWriteServiceImpl<Long, Topic> implements TopicService {

    private final TopicDao topicDao;

    protected TopicServiceImpl(TopicDao topicDao) {
        super(topicDao);
        this.topicDao = topicDao;
    }
}
