package cn.soft_x.supplies.model;

import java.util.List;

/**
 * Created by Administrator on 2016-12-09.
 */
public class InvitationModel extends BaseModel {

    /**
     * yqlist : [{"TIME":"2016-12-12","CONTENT":"您有一条来自拆解企业的邀请函，请前往查看！"}]
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
         * CONTENT : 您有一条来自拆解企业的邀请函，请前往查看！
         */

        private String TIME;
        private String CONTENT;
        private int YQLX;

        public int getYQLX() {
            return YQLX;
        }

        public void setYQLX(int YQLX) {
            this.YQLX = YQLX;
        }

        public void setTIME(String TIME) {
            this.TIME = TIME;
        }

        public void setCONTENT(String CONTENT) {
            this.CONTENT = CONTENT;
        }

        public String getTIME() {
            return TIME;
        }

        public String getCONTENT() {
            return CONTENT;
        }
    }
}
