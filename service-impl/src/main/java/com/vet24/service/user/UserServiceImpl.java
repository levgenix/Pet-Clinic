package com.vet24.service.user;

import com.vet24.dao.user.UserDao;
import com.vet24.models.user.User;
import com.vet24.service.ReadWriteServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ReadWriteServiceImpl<Long, User> implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDao.getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s doesn't exist!", email)));
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userDao.getByUserEmail(authentication.getName());
    }
}
