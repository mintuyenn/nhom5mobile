package com.example.cuahangtienloi.Models;

public class DonHang {
    private int id;
    private String ngaylap;
    private double tongtien;
    private int khachhang_id;
    private String ghichu;
    private String trangthai;

    private String tenKhachHang;



    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }
    public DonHang(int id, String ngaylap, double tongtien, int khachhang_id, String ghichu, String trangthai) {
        this.id = id;
        this.ngaylap = ngaylap;
        this.tongtien = tongtien;
        this.khachhang_id = khachhang_id;
        this.ghichu = ghichu;
        this.trangthai = trangthai;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getNgaylap() { return ngaylap; }
    public double getTongtien() { return tongtien; }
    public int getKhachhang_id() { return khachhang_id; }
    public String getGhichu() { return ghichu; }
    public String getTrangthai() { return trangthai; }

    public void setId(int id) { this.id = id; }
    public void setNgaylap(String ngaylap) { this.ngaylap = ngaylap; }
    public void setTongtien(double tongtien) { this.tongtien = tongtien; }
    public void setKhachhang_id(int khachhang_id) { this.khachhang_id = khachhang_id; }
    public void setGhichu(String ghichu) { this.ghichu = ghichu; }
    public void setTrangthai(String trangthai) { this.trangthai = trangthai; }
}
