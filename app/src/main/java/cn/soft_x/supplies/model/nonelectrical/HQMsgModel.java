package cn.soft_x.supplies.model.nonelectrical;

import java.util.List;

import cn.soft_x.supplies.model.BaseModel;

/**
 * Created by ryon on 2017/6/29.
 */

public class HQMsgModel extends BaseModel {


    private List<HqlistBean> hqlist;

    public List<HqlistBean> getHqlist() {
        return hqlist;
    }

    public void setHqlist(List<HqlistBean> hqlist) {
        this.hqlist = hqlist;
    }

    public static class HqlistBean {
        /**
         * hqjg : 2277
         * cgdw : 西软
         * hqid : 5f7c4b46e97041359baed19efbda5d3f
         * ggms : 测试
         * xqf : 哇哈哈
         * hqpm : 中型废钢
         * xqfszd : 天津市
         * hqlb : 废钢铁
         */

        private int hqjg;
        private String cgdw;
        private String hqid;
        private String ggms;
        private String xqf;
        private String hqpm;
        private String xqfszd;
        private String hqlb;

        public int getHqjg() {
            return hqjg;
        }

        public void setHqjg(int hqjg) {
            this.hqjg = hqjg;
        }

        public String getCgdw() {
            return cgdw;
        }

        public void setCgdw(String cgdw) {
            this.cgdw = cgdw;
        }

        public String getHqid() {
            return hqid;
        }

        public void setHqid(String hqid) {
            this.hqid = hqid;
        }

        public String getGgms() {
            return ggms;
        }

        public void setGgms(String ggms) {
            this.ggms = ggms;
        }

        public String getXqf() {
            return xqf;
        }

        public void setXqf(String xqf) {
            this.xqf = xqf;
        }

        public String getHqpm() {
            return hqpm;
        }

        public void setHqpm(String hqpm) {
            this.hqpm = hqpm;
        }

        public String getXqfszd() {
            return xqfszd;
        }

        public void setXqfszd(String xqfszd) {
            this.xqfszd = xqfszd;
        }

        public String getHqlb() {
            return hqlb;
        }

        public void setHqlb(String hqlb) {
            this.hqlb = hqlb;
        }
    }
}
