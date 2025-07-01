package com.example.cuahangtienloi.Models;

public class NhanVien {
    private int id;
    private String ten;
    private String username;
    private String password;
    private String quyen;
    private String diachi;
    private String sodienthoai;
    private String gioitinh;
    private int tuoi;
    private String hinhanh; // 🆕 Trường hình ảnh

    public NhanVien() {
    }

    public NhanVien(int id, String ten, String username, String password, String quyen,
                    String diachi, String sodienthoai, String gioitinh, int tuoi, String hinhanh) {
        this.id = id;
        this.ten = ten;
        this.username = username;
        this.password = password;
        this.quyen = quyen;
        this.diachi = diachi;
        this.sodienthoai = sodienthoai;
        this.gioitinh = gioitinh;
        this.tuoi = tuoi;
        this.hinhanh = hinhanh;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuyen() {
        return quyen;
    }
    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public String getDiachi() {
        return diachi;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }
    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getGioitinh() {
        return gioitinh;
    }
    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public int getTuoi() {
        return tuoi;
    }
    public void setTuoi(int tuoi) {
        this.tuoi = tuoi;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
