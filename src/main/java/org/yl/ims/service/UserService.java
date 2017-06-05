package org.yl.ims.service;

import java.util.Map;

public interface UserService {
    public Map<String,Object> Login(String userName , String passWord);
}
