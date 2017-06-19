package cn.soft_x.supplies.model;

import java.util.List;

/**
 * Created by Administrator on 2016-12-05.
 */
public class XiaoshourenyuanModel extends BaseModel {

    /**
     * list : [{"id":"53d3dba9feca4ddcbb5009fc490aa72e","name":"李灵儿"},{"id":"c90b7bfc2aa347f6bb4eb88a27ba0ac8","name":"王花花"},{"id":"1229cd79a0df4eeda6d86b93cf4ef86b","name":"吴晓"},{"id":"6aca6c20f85a41c2a6947dc89d6f7cc6","name":"李玲1"}]
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
         * id : 53d3dba9feca4ddcbb5009fc490aa72e
         * name : 李灵儿
         */

        private String id;
        private String name;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
