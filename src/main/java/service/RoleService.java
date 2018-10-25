package service;

import javax.management.relation.Role;

public interface RoleService {
    public Role creatRole(Role role);
    public void deleteRole(Long roleId);
    //添加角色/权限关系
    public void correlationPermission(Long roleId,Long...permissionId);
    //移除角色/权限关系
    public void uncorrelationPermission(long roleId,Long...permissionId);

}
