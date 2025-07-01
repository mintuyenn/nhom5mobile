package com.example.cuahangtienloi.Models;

public class SanPham {
    private int id;
    private String tensp;
    private double gia;
    private int soluong;
    private String donvi;
    private int loaihang_id;
    private String hinhanh;

    public SanPham(int id, String tensp, double gia, int soluong, String donvi, int loaihang_id, String hinhanh) {
        this.id = id;
        this.tensp = tensp;
        this.gia = gia;
        this.soluong = soluong;
        this.donvi = donvi;
        this.loaihang_id = loaihang_id;
        this.hinhanh = hinhanh;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getTensp() { return tensp; }
    public double getGia() { return gia; }
    public int getSoluong() { return soluong; }
    public String getDonvi() { return donvi; }
    public int getLoaihang_id() { return loaihang_id; }
    public String getHinhanh() { return hinhanh; }

    public void setId(int id) { this.id = id; }
    public void setTensp(String tensp) { this.tensp = tensp; }
    public void setGia(double gia) { this.gia = gia; }
    public void setSoluong(int soluong) { this.soluong = soluong; }
    public void setDonvi(String donvi) { this.donvi = donvi; }
    public void setLoaihang_id(int loaihang_id) { this.loaihang_id = loaihang_id; }
    public void setHinhanh(String hinhanh) { this.hinhanh = hinhanh; }
}
