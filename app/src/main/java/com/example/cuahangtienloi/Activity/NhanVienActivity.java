package com.example.cuahangtienloi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Adapters.NhanVienAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.NhanVien;
import com.example.cuahangtienloi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NhanVienActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD = 1;

    RecyclerView rvNhanVien;
    FloatingActionButton fabAddNhanVien;
    NhanVienAdapter adapter;
    DatabaseHelper dbHelper;
    List<NhanVien> listNhanVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        // Ánh xạ view
        rvNhanVien = findViewById(R.id.rvNhanVien);
        fabAddNhanVien = findViewById(R.id.fabAddNhanVien);
        dbHelper = new DatabaseHelper(this);

        // Lấy dữ liệu từ SQLite
        listNhanVien = dbHelper.getAllNhanVien();

        // Khởi tạo adapter
        adapter = new NhanVienAdapter(this, listNhanVien, new NhanVienAdapter.OnNhanVienClickListener() {
            @Override

            public void onSuaClick(NhanVien nv) {
                Intent intent = new Intent(NhanVienActivity.this, SuaNhanVienActivity.class);
                intent.putExtra("id", nv.getId());
                intent.putExtra("ten", nv.getTen());
                intent.putExtra("tuoi", nv.getTuoi());
                intent.putExtra("diachi", nv.getDiachi());

                startActivityForResult(intent, REQUEST_CODE_ADD);
            }


            @Override
            public void onXoaClick(NhanVien nv) {
                boolean success = dbHelper.deleteNhanVien(nv.getId());
                if (success) {
                    Toast.makeText(NhanVienActivity.this, "Đã xóa " + nv.getTen(), Toast.LENGTH_SHORT).show();
                    List<NhanVien> capNhat = dbHelper.getAllNhanVien();
                    adapter.setData(capNhat);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(NhanVienActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gán layout và adapter cho RecyclerView
        rvNhanVien.setLayoutManager(new LinearLayoutManager(this));
        rvNhanVien.setAdapter(adapter);

        // Xử lý thêm nhân viên
        fabAddNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NhanVienActivity.this, "Bạn vừa bấm Thêm nhân viên!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NhanVienActivity.this, ThemNhanVienActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });
    }

    // Reload lại dữ liệu khi thêm nhân viên mới
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            List<NhanVien> capNhat = dbHelper.getAllNhanVien();
            adapter.setData(capNhat);
            adapter.notifyDataSetChanged();
        }
    }
}
