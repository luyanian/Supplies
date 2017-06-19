package cn.soft_x.supplies.model;

import java.util.List;

/**
 * Created by Administrator on 2016-11-24.
 */
public class CommonModel extends BaseModel {
    /**
     * faqlists : [{"answer":"废旧纸张、建筑钢材都如此.","question":"什么货物按捆计算？"},{"answer":"这很多，比如很多废旧空调都如此计算","question":"什么样的货物按吨计算？"}]
     */

    private List<FaqlistsBean> faqlists;

    public void setFaqlists(List<FaqlistsBean> faqlists) {
        this.faqlists = faqlists;
    }

    public List<FaqlistsBean> getFaqlists() {
        return faqlists;
    }

    public static class FaqlistsBean {
        private boolean isClick = false;
        /**
         * answer : 废旧纸张、建筑钢材都如此.
         * question : 什么货物按捆计算？
         */

        private String answer;
        private String question;

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public String getQuestion() {
            return question;
        }

        public boolean isClick() {
            return isClick;
        }

        public void setClick(boolean click) {
            isClick = click;
        }
    }
}
