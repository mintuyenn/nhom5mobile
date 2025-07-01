package com.example.cuahangtienloi.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Adapters.SanPhamChonAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.KhachHang;
import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.Models.SanPhamChon;
import com.example.cuahangtienloi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThemDonHangActivity extends AppCompatActivity {

    Spinner spinnerKhachHang;
    RecyclerView rvSanPhamChon;
    Button btnLuu;

    List<KhachHang> khachList;
    List<SanPhamChon> spChonList;
    SanPhamChonAdapter adapter;

    int khachHangIdChon = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themdonhang);

        // Ánh xạ
        spinnerKhachHang = findViewById(R.id.spinnerKhachHang);
        rvSanPhamChon = findViewById(R.id.rvSanPhamChon);
        btnLuu = findViewById(R.id.btnLuuDonHang);

        DatabaseHelper db = DatabaseHelper.getInstance(this);

        // Load khách hàng
        khachList = db.getAllKhachHang();
        List<String> tenKH = new ArrayList<>();
        for (KhachHang kh : khachList) tenKH.add(kh.getTen());

        ArrayAdapter<String> adapterKH = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenKH);
        adapterKH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhachHang.setAdapter(adapterKH);
        spinnerKhachHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                khachHangIdChon = khachList.get(pos).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Load sản phẩm
        List<SanPham> spList = db.getAllSanPham();
        spChonList = new ArrayList<>();
        for (SanPham sp : spList) {
            spChonList.add(new SanPhamChon(sp));
        }

        adapter = new SanPhamChonAdapter(this, spChonList);
        rvSanPhamChon.setLayoutManager(new LinearLayoutManager(this));
        rvSanPhamChon.setAdapter(adapter);

        // Sự kiện lưu
        btnLuu.setOnClickListener(v -> {
            List<SanPhamChon> daChon = adapter.getSelected();

            if (khachHangIdChon == -1) {
                Toast.makeText(this, "Vui lòng chọn khách hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (daChon.isEmpty()) {
                Toast.makeText(this, "Chưa chọn sản phẩm nào", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra số lượng sản phẩm đủ hay không
            for (SanPhamChon spChon : daChon) {
                int soLuongCon = spChon.getSanPham().getSoluong();
                int soLuongChon = spChon.getSoLuong();
                if (soLuongCon < soLuongChon) {
                    Toast.makeText(this,
                            "Sản phẩm '" + spChon.getSanPham().getTensp() + "' chỉ còn " + soLuongCon + ", không đủ để đặt " + soLuongChon,
                            Toast.LENGTH_LONG).show();
                    return; // Dừng thêm đơn hàng
                }
            }

            double tongTien = 0;
            for (SanPhamChon sp : daChon) {
                tongTien += sp.getSoLuong() * sp.getSanPham().getGia();
            }

            String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Thêm đơn hàng với trạng thái mặc định là "Đang xử lý"
            long idDonHang = db.insertDonHang(ngay, tongTien, khachHangIdChon, "Đang xử lý");

            if (idDonHang != -1) {
                // Thêm chi tiết đơn hàng
                for (SanPhamChon sp : daChon) {
                    db.insertChiTietDonHang(
                            (int) idDonHang,
                            sp.getSanPham().getId(),
                            sp.getSoLuong(),
                            sp.getSanPham().getGia()
                    );
                }

                db.truSoLuongSanPhamTheoTrangThai((int) idDonHang, "Đang xử lý");

                Toast.makeText(this, "Đã thêm đơn hàng thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi thêm đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
