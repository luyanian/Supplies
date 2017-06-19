package cn.soft_x.supplies.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-12-05.
 */
public class TruckItemModel implements Serializable {


    /**
     * gwcid : 1456597f26f647438fb63f50eeefd191
     * cgid : 44b5a4f4d5d84579a671d0146f726364
     * cgcode : TH2083599999
     * pzmc : 电视机
     * ggmc : 20寸
     * cgdw : 斤
     * lxdhc : 18522945693
     * danjia : 10.23
     * shjg : 90
     * fkqx : 12
     * jszq : 90
     * sl : 300
     * time : 2016-11-22 17:35:07.0
     */

    private String gwcid;
    private String cgid;
    private String cgcode;
    private String pzmc;
    private String ggmc;
    private String cgdw;
    private String lxdhc;
    private double danjia;
    private int shjg;
    private int fkqx;
    private int jszq;
    private int sl;
    private String time;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setGwcid(String gwcid) {
        this.gwcid = gwcid;
    }

    public void setCgid(String cgid) {
        this.cgid = cgid;
    }

    public void setCgcode(String cgcode) {
        this.cgcode = cgcode;
    }

    public void setPzmc(String pzmc) {
        this.pzmc = pzmc;
    }

    public void setGgmc(String ggmc) {
        this.ggmc = ggmc;
    }

    public void setCgdw(String cgdw) {
        this.cgdw = cgdw;
    }

    public void setLxdhc(String lxdhc) {
        this.lxdhc = lxdhc;
    }

    public void setDanjia(double danjia) {
        this.danjia = danjia;
    }

    public void setShjg(int shjg) {
        this.shjg = shjg;
    }

    public void setFkqx(int fkqx) {
        this.fkqx = fkqx;
    }

    public void setJszq(int jszq) {
        this.jszq = jszq;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGwcid() {
        return gwcid;
    }

    public String getCgid() {
        return cgid;
    }

    public String getCgcode() {
        return cgcode;
    }

    public String getPzmc() {
        return pzmc;
    }

    public String getGgmc() {
        return ggmc;
    }

    public String getCgdw() {
        return cgdw;
    }

    public String getLxdhc() {
        return lxdhc;
    }

    public double getDanjia() {
        return danjia;
    }

    public int getShjg() {
        return shjg;
    }

    public int getFkqx() {
        return fkqx;
    }

    public int getJszq() {
        return jszq;
    }

    public int getSl() {
        return sl;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "TruckItemModel{" +
                "gwcid='" + gwcid + '\'' +
                ", cgid='" + cgid + '\'' +
                ", cgcode='" + cgcode + '\'' +
                ", pzmc='" + pzmc + '\'' +
                ", ggmc='" + ggmc + '\'' +
                ", cgdw='" + cgdw + '\'' +
                ", lxdhc='" + lxdhc + '\'' +
                ", danjia=" + danjia +
                ", shjg=" + shjg +
                ", fkqx=" + fkqx +
                ", jszq=" + jszq +
                ", sl=" + sl +
                ", time='" + time + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
