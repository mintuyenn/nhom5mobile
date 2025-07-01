package com.example.cuahangtienloi.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Adapters.ChiTietDonHangAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.ChiTietDonHang;
import com.example.cuahangtienloi.R;

import java.util.List;

public class ChiTietDonHangActivity extends AppCompatActivity {

    private RecyclerView rvChiTietDonHang;
    private ChiTietDonHangAdapter adapter;
    private List<ChiTietDonHang> chiTietList;

    private int donHangId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietdonhang);

        rvChiTietDonHang = findViewById(R.id.rvChiTietDonHang);
        rvChiTietDonHang.setLayoutManager(new LinearLayoutManager(this));

        donHangId = getIntent().getIntExtra("donhang_id", -1);
        if (donHangId != -1) {
            loadChiTietDonHang(donHangId);
        }
    }

    private void loadChiTietDonHang(int donHangId) {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        chiTietList = db.getChiTietDonHangTheoDonHangId(donHangId); // Hàm lấy chi tiết đơn hàng
        adapter = new ChiTietDonHangAdapter(this, chiTietList);
        rvChiTietDonHang.setAdapter(adapter);
    }
}
