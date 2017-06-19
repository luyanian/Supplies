package cn.soft_x.supplies.model;

import java.util.List;

/**
 * Created by ryon on 2017/6/19.
 */

public class RoleModel extends BaseModel {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * ywtags : 1
         * tubiao : /s/themes/default/images/def.png
         * roleName : 废钢铁
         * roleCode : GHS-FGT
         * roleId : 152c45e7b18f410ea7c61cf4d9e613df
         */

        private int ywtags;
        private String tubiao;
        private String roleName;
        private String roleCode;
        private String roleId;

        public int getYwtags() {
            return ywtags;
        }

        public void setYwtags(int ywtags) {
            this.ywtags = ywtags;
        }

        public String getTubiao() {
            return tubiao;
        }

        public void setTubiao(String tubiao) {
            this.tubiao = tubiao;
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

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }
    }
}
