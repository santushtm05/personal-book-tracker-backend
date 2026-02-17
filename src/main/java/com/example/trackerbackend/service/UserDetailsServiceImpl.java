package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.UserDAO;
import com.example.trackerbackend.entity.User;
import com.example.trackerbackend.entity.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDAO userDAO;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDB = userDAO.findByUsernameAndDeletedAtIsNull(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(userDB);
    }

    public UserDetails loadUserById(Integer id) {
//        System.out.println("Loading User for id: "+id);
        User user = userDAO.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
//        System.out.println("Got User:"+user+"\n Password:"+user.getPassword());
        return new CustomUserDetails(user);
    }

}