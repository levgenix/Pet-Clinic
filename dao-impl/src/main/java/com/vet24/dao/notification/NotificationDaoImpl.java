package com.vet24.dao.notification;

import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.notification.Notification;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDaoImpl extends ReadWriteDaoImpl<Long, Notification> implements NotificationDao {
}
