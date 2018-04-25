package com.dao;

import com.domain.vo.UserVo;
import com.domain.vo.UserVoExample;
import org.apache.catalina.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Resource
    UserVoMapper userDao;



    @Test
    public void testInsert() {
        UserVo userVo = new UserVo();
        userVo.setUsername("测试");
        userVo.setPassword("123456");
        userVo.setAddress("上海");
        userVo.setEmail("8@q.com");
        userDao.insertSelective(userVo);
    }

    @Test
    public void testcountByExample(){
        UserVoExample example = new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(3);
        System.out.println(userDao.countByExample(example));
    }

    @Test
    public void deleteByPrimaryKey(){
        userDao.deleteByPrimaryKey(1);
    }

    @Test
    public void deleteByExample(){
        UserVoExample example = new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria();
        criteria.andAddressEqualTo("上海");
        criteria.andEmailIsNull();
        userDao.deleteByExample(example);
    }

    @Test
    public void insertSelective(){
        UserVo userVo = new UserVo();
        userVo.setUsername("SELECTIVE");
        userVo.setPassword("123456");
        userVo.setAddress("北京");
        userDao.insertSelective(userVo);
    }

    @Test
    public void selectByExample(){
        UserVoExample example = new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria();
        criteria.andAddressEqualTo("北京");
        System.out.println(userDao.selectByExample(example));
    }

    @Test
    public void selectByPrimaryKey(){
        System.out.println(userDao.selectByPrimaryKey(2));
    }

    @Test
    public void updateByExampleSelective(){
        UserVo record = new UserVo();
        record.setUsername("更新的name");
        UserVoExample example = new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria();
        criteria.andPasswordEqualTo("123456");
        userDao.updateByExampleSelective(record,example);
    }

    @Test
    public void  updateByExample(){
        UserVo record = new UserVo();
        record.setUsername("更新的name");
        UserVoExample example = new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo("8@q.com");
        userDao.updateByExampleSelective(record,example);
    }

    @Test
    public void updateByPrimaryKeySelective(){
        UserVo record = new UserVo();
        // 此方法 必须要填主键值
        record.setUid(1);
        record.setUsername("测试");
        userDao.updateByPrimaryKeySelective(record);
    }

    @Test
    public void  updateByPrimaryKey(){
        UserVo record = new UserVo();
        // 此方法 必须要填主键值
        record.setUid(1);
        record.setUsername("测试");
        userDao.updateByPrimaryKey(record);
    }
}
