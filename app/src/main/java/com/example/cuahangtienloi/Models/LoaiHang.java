package com.example.cuahangtienloi.Models;

public class LoaiHang {
    private int id;
    private String tenloai;

    public LoaiHang(int id, String tenloai) {
        this.id = id;
        this.tenloai = tenloai;
    }

    public int getId() { return id; }
    public String getTenloai() { return tenloai; }

    public void setId(int id) { this.id = id; }
    public void setTenloai(String tenloai) { this.tenloai = tenloai; }
}
