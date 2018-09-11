package model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String Ma, Ten, MaDM;
    private int DonGia;


    public String getMa() {
        return Ma;
    }

    public void setMa(String ma) {
        Ma = ma;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getMaDM() {
        return MaDM;
    }

    public void setMaDM(String maDM) {
        MaDM = maDM;
    }

    public int getDonGia() {
        return DonGia;
    }

    public void setDonGia(int donGia) {
        DonGia = donGia;
    }

    @Override
    public String toString() {
        return Ma+"\n"+Ten+"\n"+DonGia;
    }
}
