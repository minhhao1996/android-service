package model;

import java.io.Serializable;

public class DanhMuc implements Serializable{
    private String MaDm,TenDm;

    public String getMaDm() {
        return MaDm;
    }

    public void setMaDm(String maDm) {
        MaDm = maDm;
    }

    public String getTenDm() {
        return TenDm;
    }

    public void setTenDm(String tenDm) {
        TenDm = tenDm;
    }

    @Override
    public String toString() {
        return MaDm+" - "+TenDm ;
    }
}
