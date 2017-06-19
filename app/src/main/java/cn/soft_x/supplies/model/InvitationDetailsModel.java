package cn.soft_x.supplies.model;

import java.util.List;

/**
 * Created by Administrator on 2016-12-09.
 */
public class InvitationDetailsModel extends BaseModel {

    /**
     * yqlist : [{"TIME":"2016-12-12","CONTENT":"您有一条来自拆解企业的邀请函，订单号为：CG1612061523，请前往查看！","CGID":"024b834e80464c0a81e2644a2ac1e258"}]
     */

    private List<YqlistBean> yqlist;

    public void setYqlist(List<YqlistBean> yqlist) {
        this.yqlist = yqlist;
    }

    public List<YqlistBean> getYqlist() {
        return yqlist;
    }

    public static class YqlistBean {
        /**
         * TIME : 2016-12-12
         * CONTENT : 您有一条来自拆解企业的邀请函，订单号为：CG1612061523，请前往查看！
         * CGID : 024b834e80464c0a81e2644a2ac1e258
         * companyid

         */
        private String companyid;
        private String TIME;
        private String CONTENT;
        private String CGID;
        private String id;

        public String getCompanyid() {
            return companyid;
        }

        public void setCompanyid(String companyid) {
            this.companyid = companyid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTIME(String TIME) {
            this.TIME = TIME;
        }

        public void setCONTENT(String CONTENT) {
            this.CONTENT = CONTENT;
        }

        public void setCGID(String CGID) {
            this.CGID = CGID;
        }

        public String getTIME() {
            return TIME;
        }

        public String getCONTENT() {
            return CONTENT;
        }

        public String getCGID() {
            return CGID;
        }
    }
}
