package model;

import java.io.Serializable;

/**
 * Thực thể ánh xạ bảng Roles (Vai trò người dùng trong hệ thống).
 */
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private int roleId;
    private String roleName;

    public Role() {
    }

    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" + "roleId=" + roleId + ", roleName=" + roleName + '}';
    }
}
