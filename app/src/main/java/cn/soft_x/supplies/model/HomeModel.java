package cn.soft_x.supplies.model;

import java.util.List;

/**
 * Created by Administrator on 2016-12-07.
 */
public class HomeModel extends BaseModel {
    /**
     * newslist : [{"NEWSTITLE":"5555555","NEWSIMG":"/s/aonibote/images/ce1e45c213754563863dab68ee49738f.jpg","CONTENT":"51155555","NEWSID":"cab65a48aeb343df8617b15f996bc4e4"},{"NEWSTITLE":"测试","NEWSIMG":"/s/aonibote/images/f6149f01a4824ca9879aeef447369fe1.jpg","CONTENT":"测试测试","NEWSID":"072ea6b2df4b4a1e94b4a965651bf3e3"},{"NEWSTITLE":"123","NEWSIMG":"/s/aonibote/images/201611-de8227d647b3464f8b41dfca156e40d5.jpg","CONTENT":"123","NEWSID":"6df1b09012b54f4e88c7e347485a4039"},{"NEWSTITLE":"替换地方","NEWSIMG":null,"CONTENT":"计划vhk\"\"","NEWSID":"49602846b3c54867b74b3ed626af8157"}]
     * lbtlist : [{"PICURL":"/s/aonibote/images/80a59482cfdc4e1695776c65a0c76877.png"},{"PICURL":"/s/aonibote/usrimages/201610/201610-be51b5bee23848b2829cb38f3198f375.jpg"},{"PICURL":"/s/aonibote/usrimages/201610/201610-50de86ade7874e44b0f1f5faae1c97d2.png"},{"PICURL":""}]
     */
    private List<NewslistBean> newslist;
    private List<LbtlistBean> lbtlist;

    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

    public void setLbtlist(List<LbtlistBean> lbtlist) {
        this.lbtlist = lbtlist;
    }

    public List<NewslistBean> getNewslist() {
        return newslist;
    }

    public List<LbtlistBean> getLbtlist() {
        return lbtlist;
    }

    public static class NewslistBean {
        /**
         * NEWSTITLE : 5555555
         * NEWSIMG : /s/aonibote/images/ce1e45c213754563863dab68ee49738f.jpg
         * CONTENT : 51155555
         * NEWSID : cab65a48aeb343df8617b15f996bc4e4
         */

        private String NEWSTITLE;
        private String NEWSIMG;
        private String CONTENT;
        private String NEWSID;

        public void setNEWSTITLE(String NEWSTITLE) {
            this.NEWSTITLE = NEWSTITLE;
        }

        public void setNEWSIMG(String NEWSIMG) {
            this.NEWSIMG = NEWSIMG;
        }

        public void setCONTENT(String CONTENT) {
            this.CONTENT = CONTENT;
        }

        public void setNEWSID(String NEWSID) {
            this.NEWSID = NEWSID;
        }

        public String getNEWSTITLE() {
            return NEWSTITLE;
        }

        public String getNEWSIMG() {
            return NEWSIMG;
        }

        public String getCONTENT() {
            return CONTENT;
        }

        public String getNEWSID() {
            return NEWSID;
        }
    }

    public static class LbtlistBean {
        /**
         * PICURL : /s/aonibote/images/80a59482cfdc4e1695776c65a0c76877.png
         */

        private String PICURL;

        public void setPICURL(String PICURL) {
            this.PICURL = PICURL;
        }

        public String getPICURL() {
            return PICURL;
        }
    }
}
