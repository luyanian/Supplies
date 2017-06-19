package cn.soft_x.supplies.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-12-06.
 */
public class MessageDetailModel extends BaseModel {

    /**
     * list : [{"TIME":"2016-12-05","XXLX":3,"XXDL":2,"CONTENT":"您有一条新到厂消息，订单号为：dd102570，请前往查看！"},{"TIME":"2016-12-05","XXLX":3,"XXDL":2,"CONTENT":"您有一条新到厂消息，订单号为：dd102569，请前往查看！"}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean {

        public ListBean(String GLCOMPANYID, long TIME, int XXLX, int XXDL, String CONTENT, String GLID,int read) {
            this.GLCOMPANYID = GLCOMPANYID;
            this.TIME = TIME;
            this.XXLX = XXLX;
            this.XXDL = XXDL;
            this.CONTENT = CONTENT;
            this.GLID = GLID;
            this.read = read;
        }

        public ListBean() {
        }

        /**
         * TIME : 2016-12-05
         * XXLX : 3
         * XXDL : 2
         * CONTENT : 您有一条新到厂消息，订单号为：dd102570，请前往查看！
         */
        private long TIME;
        private int XXLX;
        private int XXDL;
        private String CONTENT;
        private String GLID;
        private String GLCOMPANYID;
        private int read;
        private String timeStr;

        public String getTimeStr() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            this.timeStr = format.format(new Date(TIME));
            return timeStr;
        }

        public int getRead() {
            return read;
        }

        public void setRead(int read) {
            this.read = read;
        }

        public String getGLCOMPANYID() {
            return GLCOMPANYID;
        }

        public void setGLCOMPANYID(String GLCOMPANYID) {
            this.GLCOMPANYID = GLCOMPANYID;
        }

        public String getGLID() {
            return GLID;
        }

        public void setGLID(String GLID) {
            this.GLID = GLID;
        }

        public void setTIME(long TIME) {
            this.TIME = TIME;
        }

        public void setXXLX(int XXLX) {
            this.XXLX = XXLX;
        }

        public void setXXDL(int XXDL) {
            this.XXDL = XXDL;
        }

        public void setCONTENT(String CONTENT) {
            this.CONTENT = CONTENT;
        }

        public long getTIME() {
            return TIME;
        }

        public int getXXLX() {
            return XXLX;
        }

        public int getXXDL() {
            return XXDL;
        }

        public String getCONTENT() {
            return CONTENT;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "TIME='" + TIME + '\'' +
                    ", XXLX=" + XXLX +
                    ", XXDL=" + XXDL +
                    ", CONTENT='" + CONTENT + '\'' +
                    ", GLID='" + GLID + '\'' +
                    ", GLCOMPANYID='" + GLCOMPANYID + '\'' +
                    ", read=" + read +
                    "}";
        }
    }
}
