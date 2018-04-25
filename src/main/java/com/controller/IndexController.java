package com.controller;

import com.dao.UserVoMapper;
import com.domain.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author tangj
 * @date 2018/4/25 22:54
 */
@Controller
public class IndexController {

    @Autowired
    UserVoMapper userDao;

    @GetMapping(value = "")
    @ResponseBody
    public UserVo index(){
        UserVo userVo = userDao.selectByPrimaryKey(1);
        return userVo;
    }
}
