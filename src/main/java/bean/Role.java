package bean;

public class Role {
    private  String roleId;
    private String roleName;
    private String createUser;
    private Integer siteId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Role() {
    }

    public Role(String roleId, String roleName, String createUser, Integer siteId) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.createUser = createUser;
        this.siteId = siteId;
    }
}
