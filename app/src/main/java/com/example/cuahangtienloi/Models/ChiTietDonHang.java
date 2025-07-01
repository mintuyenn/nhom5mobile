package com.example.cuahangtienloi.Models;

public class ChiTietDonHang {
    private int id;
    private int donhang_id;
    private int sanpham_id;
    private int soluong;
    private double dongia;
    private String hinhAnh;
    private String tenSanPham;

    public ChiTietDonHang(int id, int donhang_id, int sanpham_id, int soluong, double dongia,String hinhAnh,String tenSanPham) {
        this.id = id;
        this.donhang_id = donhang_id;
        this.sanpham_id = sanpham_id;
        this.soluong = soluong;
        this.dongia = dongia;
        this.hinhAnh =hinhAnh;
        this.tenSanPham = tenSanPham;
    }
    public ChiTietDonHang() {
    }
    // Getter & Setter
    public int getId() { return id; }
    public int getDonhang_id() { return donhang_id; }
    public int getSanpham_id() { return sanpham_id; }
    public int getSoluong() { return soluong; }
    public double getDongia() { return dongia; }

    public void setId(int id) { this.id = id; }
    public void setDonhang_id(int donhang_id) { this.donhang_id = donhang_id; }
    public void setSanpham_id(int sanpham_id) { this.sanpham_id = sanpham_id; }
    public void setSoluong(int soluong) { this.soluong = soluong; }
    public void setDongia(double dongia) { this.dongia = dongia; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

}
