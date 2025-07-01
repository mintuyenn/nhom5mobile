package com.example.cuahangtienloi.Models;

public class KhachHang {
    private int id;
    private String ten;
    private String sdt;
    private String diachi;
    private String email;
    private String ngaytao;
    private String hinhanh;

    public KhachHang(int id, String ten, String sdt, String diachi, String email, String ngaytao,String hinhanh) {
        this.id = id;
        this.ten = ten;
        this.sdt = sdt;
        this.diachi = diachi;
        this.email = email;
        this.ngaytao = ngaytao;
        this.hinhanh = hinhanh;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getTen() { return ten; }
    public String getSdt() { return sdt; }
    public String getDiachi() { return diachi; }
    public String getEmail() { return email; }
    public String getNgaytao() { return ngaytao; }


    public String getHinhanh() {
        return hinhanh;
    }

    public void setId(int id) { this.id = id; }
    public void setTen(String ten) { this.ten = ten; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public void setDiachi(String diachi) { this.diachi = diachi; }
    public void setEmail(String email) { this.email = email; }
    public void setNgaytao(String ngaytao) { this.ngaytao = ngaytao; }
    public void setHinhanh(String hinhanh){ this.hinhanh = hinhanh;}
}