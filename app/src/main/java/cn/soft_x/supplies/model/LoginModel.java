package cn.soft_x.supplies.model;

/**
 * Created by Administrator on 2016-11-24.
 */
public class LoginModel extends BaseModel{

    /**
     * YHID : 049ee74567e5488abfda6422785dac42
     */

    private String YHID;
    private String YHNC;
    private String longitude;
    private String latitude;
    private String YHTX;
    private String roleId;
    private String tags;

    public String getYHTX() {
        return YHTX;
    }

    public void setYHTX(String YHTX) {
        this.YHTX = YHTX;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getYHNC() {
        return YHNC;
    }

    public void setYHNC(String YHNC) {
        this.YHNC = YHNC;
    }

    public void setYHID(String YHID) {
        this.YHID = YHID;
    }

    public String getYHID() {
        return YHID;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tag) {
        this.tags = tag;
    }
}
