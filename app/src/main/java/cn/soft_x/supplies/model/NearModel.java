package cn.soft_x.supplies.model;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by Administrator on 2016-11-24.
 */
public class NearModel extends BaseModel {
    /**
     * user_id : 0ac6c492c0a144b9ab43e2b783563768
     * org_id : 769c59c241184613948797b2afefe036
     * tags : DMMS
     * org_name : yanglw
     * uid : 1880788814
     * province : 天津市
     * user_name : yanglw
     * geotable_id : 158470
     * district : 西青区
     * create_time : 1480908637
     * city : 天津市
     * location : [117.251366,39.03212]
     * address : 天津市西青区天津市百景园国际旅行社
     * title : yanglw
     * coord_type : 3
     * direction : 西北
     * type : 0
     * distance : 16536
     * weight : 0
     */
    private String org_id;
    private String org_name;
    private String address;
    private String title;
    private LatLng latLng;
    private String org_pic;

    public String getOrg_pic() {
        return org_pic;
    }

    public void setOrg_pic(String org_pic) {
        this.org_pic = org_pic;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
