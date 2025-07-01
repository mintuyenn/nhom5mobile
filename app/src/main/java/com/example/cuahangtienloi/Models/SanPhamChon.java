package com.example.cuahangtienloi.Models;

public class SanPhamChon {
    private SanPham sanPham;
    private int soLuong = 0;

    public SanPhamChon(SanPham sp) {
        this.sanPham = sp;
    }

    public SanPham getSanPham() { return sanPham; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}