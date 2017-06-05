package org.yl.ims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yl.ims.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("user")
public class UserCtrl {
    @Autowired
    private UserService userService;

  /*  @RequestMapping("verify")
    @ResponseBody
    public Map<String,Object> Login(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
         Map<String,Object> map =(userService.Login(userInfo, request,response));
         return map;
    }

    @RequestMapping("logined")
    public Map<String,Object> getLogin (HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
//userId = 1;
        Map<String, Object> map = new HashMap<String, Object>();
        if(userId == null) {
            map.put("logined", false);
            return map;
        } else {
            map.put("logined", true);
            map.put("username", session.getAttribute("username"));
            return map;
        }
    }*/
    @RequestMapping("benull")
    @ResponseBody
    public Map<String, Object> findUser(String username , String password){
        Map<String, Object> map  =(userService.Login(username, password));
        return map;
    }
}
