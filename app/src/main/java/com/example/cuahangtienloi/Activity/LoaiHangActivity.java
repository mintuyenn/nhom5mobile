package com.example.cuahangtienloi.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Adapters.LoaiHangAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.LoaiHang;
import com.example.cuahangtienloi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;

import java.util.List;

public class LoaiHangActivity extends AppCompatActivity {
    RecyclerView rvLoaiHang;
    FloatingActionButton fabAddLoai;
    List<LoaiHang> listLoai;
    LoaiHangAdapter adapter;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaihang);

        rvLoaiHang = findViewById(R.id.rvLoaiHang);
        fabAddLoai = findViewById(R.id.fabAddLoai);

        dbHelper = DatabaseHelper.getInstance(this);
        listLoai = dbHelper.getAllLoaiHang();

        adapter = new LoaiHangAdapter(this, listLoai, new LoaiHangAdapter.OnLoaiClick() {
            @Override
            public void onClick(LoaiHang loaiHang) {
                Intent intent = new Intent(LoaiHangActivity.this, SanPhamTheoLoaiActivity.class);
                // Đổi key truyền thành "loaihang_id"
                intent.putExtra("loaihang_id", loaiHang.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(LoaiHang loaiHang) {
                showDialogXoaLoai(loaiHang);
            }
        });

        rvLoaiHang.setLayoutManager(new LinearLayoutManager(this));
        rvLoaiHang.setAdapter(adapter);

        fabAddLoai.setOnClickListener(v -> showDialogThemLoai());
    }

    private void showDialogThemLoai() {
        View view = getLayoutInflater().inflate(R.layout.them_loaihang, null);
        EditText edtTenLoai = view.findViewById(R.id.edtTenLoai);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String ten = edtTenLoai.getText().toString().trim();
                    if (!ten.isEmpty()) {
                        boolean inserted = dbHelper.insertLoaiHang(new LoaiHang(0, ten));
                        if (inserted) {
                            capNhatDanhSach();
                            Toast.makeText(this, "Đã thêm loại hàng!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Tên loại đã tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Vui lòng nhập tên loại hàng", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showDialogXoaLoai(LoaiHang loaiHang) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa loại hàng \"" + loaiHang.getTenloai() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean deleted = dbHelper.deleteLoaiHang(loaiHang.getId());
                    if (deleted) {
                        Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                        capNhatDanhSach();
                    } else {
                        Toast.makeText(this, "Không thể xóa. Có thể đang có sản phẩm thuộc loại này!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void capNhatDanhSach() {
        listLoai.clear();
        listLoai.addAll(dbHelper.getAllLoaiHang());
        adapter.notifyDataSetChanged();
    }
}
