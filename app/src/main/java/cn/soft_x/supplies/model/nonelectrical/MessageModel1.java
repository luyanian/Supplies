package cn.soft_x.supplies.model.nonelectrical;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.soft_x.supplies.model.BaseModel;

/**
 * Created by Administrator on 2016-12-06.
 */
public class MessageModel1 extends BaseModel {

    /**
     * list : [{"TIME":"2016-12-05","XXLX":3,"XXDL":2,"CONTENT":"您有一条到厂消息，请前往查看！"}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean {
        /**
         * TIME : 2016-12-05
         * XXLX : 3
         * XXDL : 2
         * CONTENT : 您有一条到厂消息，请前往查看！
         */

        private long time;
        private int xxlx;
        private int xxdl;
        private String content;
        private String ywtags;
        private String glid;
        private String glcompanyid;
        private int read;
        private String timeStr;

        public ListBean(long time, int xxlx, int xxdl, String content, String ywtags, String glid, String glcompanyid, int read) {
            this.time = time;
            this.xxlx = xxlx;
            this.xxdl = xxdl;
            this.content = content;
            this.ywtags = ywtags;
            this.glid = glid;
            this.glcompanyid = glcompanyid;
            this.read = read;
        }

        public ListBean() {
        }

        public String getTimeStr() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            this.timeStr = format.format(new Date(time));
            return timeStr;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getXxlx() {
            return xxlx;
        }

        public void setXxlx(int xxlx) {
            this.xxlx = xxlx;
        }

        public int getXxdl() {
            return xxdl;
        }

        public void setXxdl(int xxdl) {
            this.xxdl = xxdl;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getYwtags() {
            return ywtags;
        }

        public void setYwtags(String ywtags) {
            this.ywtags = ywtags;
        }

        public String getGlid() {
            return glid;
        }

        public void setGlid(String glid) {
            this.glid = glid;
        }

        public String getGlcompanyid() {
            return glcompanyid;
        }

        public void setGlcompanyid(String glcompanyid) {
            this.glcompanyid = glcompanyid;
        }

        public int getRead() {
            return read;
        }

        public void setRead(int read) {
            this.read = read;
        }
    }
}
