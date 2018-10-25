package service;

import bean.User;

import java.util.Set;

public interface UserService {
    //创建账号
    public User createUser(User user);
    //修改密码
    public void changePassword(String UserId,String newPassword);
    //添加用户/角色关系
    public void correlationRoles(String userId,String...roleIds);
    //移除用户/角色关系
    public void uncorrelationRoles(String userId,String...roleIds);
    //根据用户名查找角色
    public Set<String> findRoles(String username);
    //根据用户名查找权限
    public Set<String> findPermissions(String username);

}
