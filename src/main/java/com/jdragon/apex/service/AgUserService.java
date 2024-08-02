package com.jdragon.apex.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.AgUser;
import com.jdragon.apex.mapper.AgUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AgUserService extends ServiceImpl<AgUserMapper, AgUser> implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AgUser agUser = getOne(new LambdaQueryWrapper<AgUser>().eq(AgUser::getUsername, username));
        if (agUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return agUser;
    }
}
