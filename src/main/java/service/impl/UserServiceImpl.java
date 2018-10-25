package service.impl;

import bean.User;
import dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import service.UserService;
import utils.PasswordHelper;

import java.util.Set;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    private PasswordHelper passwordHelper=new PasswordHelper();
    public User createUser(User user) {
        passwordHelper.encryptPassword(user);
        return userDao.createUser(user);
    }
    //修改user的password
    public void changePassword(String userId, String newPassword) {
        String sql="select * from sys_user where user_id=?";
        User user=userDao.findUserById(sql,new Object[]{userId});
        passwordHelper.encryptPassword(user);
        String updateSql="update sys_user set user_name=?,password=?,nick_name=?,email=?,salt=? where user_id=?";
        Object[] objects=new Object[]{user.getUserName(),user.getPassword(),user.getNickName(),user.getEmail(),user.getSalt(),user.getUserId()};
        userDao.updateUser(sql,objects);
    }
    //添加用户/角色关系
    public void correlationRoles(String userId, String... roleIds) {

    }
    //删除用户/角色关系
    public void uncorrelationRoles(String userId, String... roleIds) {

    }
    //查询某用户名下所有的角色
    public Set<String> findRoles(String username) {
        return null;
    }

    public Set<String> findPermissions(String username) {
        return null;
    }
}
