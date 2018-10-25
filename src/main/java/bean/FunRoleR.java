package bean;

public class FunRoleR {
    private String id;
    private String funcId;
    private String roleId;

    public FunRoleR() {
    }

    public FunRoleR(String id, String funcId, String roleId) {
        this.id = id;
        this.funcId = funcId;
        this.roleId = roleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
