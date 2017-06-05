package org.yl.ims.service.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.yl.ims.model.db.UserInfo;
import org.yl.ims.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseService<UserInfo> implements UserService {
    private BeanPropertyRowMapper<UserInfo> userInfoRowMapper = new BeanPropertyRowMapper<UserInfo>(
            UserInfo.class);

    @Override
    public Map<String, Object> Login(String username , String password) {
        Map<String,Object> map = new HashMap<String, Object>();
        boolean flag = false;
        String rawSql = "select * from userinfo where userName =:username and passWord =:password";
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("username", username);
        p.put("password",password);
        List<UserInfo> query = this.template.query(rawSql,p,userInfoRowMapper);
        if(query.size()<1){
            map.put("flag", flag);
            map.put("msg", "账号密码错误");
        }else{//登陆成功
            flag = true ;
            map.put("flag", flag);
        }
        return map;
    }
}
