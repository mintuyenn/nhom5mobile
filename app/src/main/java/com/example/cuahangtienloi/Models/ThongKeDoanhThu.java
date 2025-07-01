package com.example.cuahangtienloi.Models;

public class ThongKeDoanhThu {
    private String thoigian;
    private double doanhthu;

    public ThongKeDoanhThu(String thoigian, double doanhthu) {
        this.thoigian = thoigian;
        this.doanhthu = doanhthu;
    }

    public String getThoigian() {
        return thoigian;
    }

    public double getDoanhthu() {
        return doanhthu;
    }
}
