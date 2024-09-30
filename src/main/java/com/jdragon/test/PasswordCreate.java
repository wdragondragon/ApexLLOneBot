package com.jdragon.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCreate {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("1"));
    }
}
