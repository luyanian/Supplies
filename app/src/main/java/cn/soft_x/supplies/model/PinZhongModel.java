package cn.soft_x.supplies.model;

import java.util.List;

/**
 * Created by ryon on 2017/6/19.
 */

public class PinZhongModel extends BaseModel {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * pzid : 083ab61bc4184b8a92c0aa6ab95e41a3
         * pzname : 不锈钢
         */

        private String pzid;
        private String pzname;

        public String getPzid() {
            return pzid;
        }

        public void setPzid(String pzid) {
            this.pzid = pzid;
        }

        public String getPzname() {
            return pzname;
        }

        public void setPzname(String pzname) {
            this.pzname = pzname;
        }
    }
}
