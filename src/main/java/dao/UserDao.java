package dao;

import bean.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    /*
    新增用户
     */
    public User createUser(User user);
    /*
    修改用户信息
     */
    public int updateUser(String sql,Object[] condition);
    /*
    删除用户信息
     */
    public int deleteUser(String userId);
    /*
    根据条件查询数据库
     */
    public List<Map<String,Object>> seleteUser(String sql,Object[] condition);
    /*
    根据用户id获取User对象
     */
    public User findUserById(String sql,Object[] objects);
}
