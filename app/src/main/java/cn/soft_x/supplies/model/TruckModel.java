package cn.soft_x.supplies.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016-12-05.
 */
public class TruckModel extends BaseModel implements Serializable {

    /**
     * data : [{"cjqymc":"天津市拆解企业","id":"aa0fdd089dad49aa8d605abd7ad5791a","cjqyid":"f4d96724a2874fff8c61bddfc125375e","list":"[{\"gwcid\":\"1456597f26f647438fb63f50eeefd191\",\"cgid\":\"44b5a4f4d5d84579a671d0146f726364\",\"cgcode\":\"TH2083599999\",\"pzmc\":\"电视机\",\"ggmc\":\"20寸\",\"cgdw\":\"斤\",\"lxdhc\":\"18522945693\",\"danjia\":10.23,\"shjg\":90,\"fkqx\":12,\"jszq\":90,\"sl\":300,\"time\":\"2016-11-22 17:35:07.0\"},{\"gwcid\":\"aff1370dc508493e94878ecd7b3655bc\",\"cgid\":\"44b5a4f4d5d84579a671d0146f726364\",\"cgcode\":\"TH2083599999\",\"pzmc\":\"电视机\",\"ggmc\":\"20寸\",\"cgdw\":\"斤\",\"lxdhc\":\"18522945693\",\"danjia\":10.23,\"shjg\":90,\"fkqx\":12,\"jszq\":90,\"sl\":300,\"time\":\"2016-11-22 17:35:05.0\"}]"},{"cjqymc":"天津市拆解企业","id":"064a57dc2b264a88ae6b6ff2017315e2","cjqyid":"f4d96724a2874fff8c61bddfc125375e","list":"[{\"gwcid\":\"e9d601dddd974dd79cff09e15f83a5ca\",\"cgid\":\"44b5a4f4d5d84579a671d0146f726364\",\"cgcode\":\"TH2083599999\",\"pzmc\":\"电视机\",\"ggmc\":\"20寸\",\"cgdw\":\"斤\",\"lxdhc\":\"18522945693\",\"danjia\":10.23,\"shjg\":90,\"fkqx\":12,\"jszq\":60,\"sl\":600,\"time\":\"2016-11-22 17:34:51.0\"}]"}]
     */

    private List<DataBean> data;

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        /**
         * cjqymc : 天津市拆解企业
         * id : aa0fdd089dad49aa8d605abd7ad5791a
         * cjqyid : f4d96724a2874fff8c61bddfc125375e
         * list : [{"gwcid":"1456597f26f647438fb63f50eeefd191","cgid":"44b5a4f4d5d84579a671d0146f726364","cgcode":"TH2083599999","pzmc":"电视机","ggmc":"20寸","cgdw":"斤","lxdhc":"18522945693","danjia":10.23,"shjg":90,"fkqx":12,"jszq":90,"sl":300,"time":"2016-11-22 17:35:07.0"},{"gwcid":"aff1370dc508493e94878ecd7b3655bc","cgid":"44b5a4f4d5d84579a671d0146f726364","cgcode":"TH2083599999","pzmc":"电视机","ggmc":"20寸","cgdw":"斤","lxdhc":"18522945693","danjia":10.23,"shjg":90,"fkqx":12,"jszq":90,"sl":300,"time":"2016-11-22 17:35:05.0"}]
         */

        private String cjqymc;
        private String id;
        private String cjqyid;
        private String jymss;
        private int jyms;
        private boolean isChecked;

        public int getJyms() {
            return jyms;
        }

        public void setJyms(int jyms) {
            this.jyms = jyms;
        }

        public String getJymss() {
            return jymss;
        }

        public void setJymss(String jymss) {
            this.jymss = jymss;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        private List<TruckItemModel> list;

        public void setCjqymc(String cjqymc) {
            this.cjqymc = cjqymc;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setCjqyid(String cjqyid) {
            this.cjqyid = cjqyid;
        }

        public void setList(List<TruckItemModel> list) {
            this.list = list;
        }

        public String getCjqymc() {
            return cjqymc;
        }

        public String getId() {
            return id;
        }

        public String getCjqyid() {
            return cjqyid;
        }

        public List<TruckItemModel> getList() {
            return list;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "isChecked=" + isChecked +
                    ", cjqyid='" + cjqyid + '\'' +
                    ", id='" + id + '\'' +
                    ", cjqymc='" + cjqymc + '\'' +
                    '}';
        }
    }
}
