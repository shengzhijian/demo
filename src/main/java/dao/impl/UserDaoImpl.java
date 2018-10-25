package dao.impl;

import bean.User;
import dao.UserDao;
import java.util.List;
import java.util.Map;

public class UserDaoImpl extends BaseJdbcDaoImpl implements UserDao {
    /*
    保存新创建的User
     */
    public User createUser(User user) {
        String sql="insert into sys_user (user_id,user_name,password,nick_name,email) values (?,?,?,?,?)";
        Object[] objs=new Object[]{ user.getUserId(),user.getUserName(),user.getPassword(),user.getNickName(),user.getEmail()};
        if(super.update(sql,objs)!=-1){
            return user;
        }else{
            return new User();
        }
    }
    /*
    修改已有User信息
     */
    public int updateUser(String sql,Object[] objects) {
        //String sql="update sys_user set user_name=?,password=?,nick_name=?,email=? where user_id=?";
        //Object[] objects=new Object[]{user.getUserName(),user.getPassword(),user.getNickName(),user.getEmail(),user.getUserId()};
        return super.update(sql,objects);
    }
    /*
    删除已有的User信息
     */
    public int deleteUser(String userId) {
        String sql="delete from sys_user where user_id=?";
        Object[] objects=new Object[]{userId};
        return super.update(sql,objects);
    }
    /*
    查询数据库
    为适应大多数的查询功能，所以在这个地方不做sql赋值及参数赋值
     */
    public List<Map<String, Object>> seleteUser(String sql,Object[] condition) {
        return super.queryForList(sql,condition);
    }
    /*
    根据用户Id获取User对象
     */
    public User findUserById(String sql, Object[] objects) {
        return (User)super.findById(sql,objects);
    }
}
