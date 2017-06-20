package cn.soft_x.supplies.model;

/**
 * Created by ryon on 2017/6/20.
 */

public class RoleDetailModel extends BaseModel {

    /**
     * ywtags : 2
     * roleName : 废家电
     * roleCode : GHS-FJD
     */

    private String ywtags;
    private String roleName;
    private String roleCode;

    public String getYwtags() {
        return ywtags;
    }

    public void setYwtags(String ywtags) {
        this.ywtags = ywtags;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
