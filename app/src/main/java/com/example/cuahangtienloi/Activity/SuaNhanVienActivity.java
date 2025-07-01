package com.example.cuahangtienloi.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.NhanVien;
import com.example.cuahangtienloi.R;

public class SuaNhanVienActivity extends AppCompatActivity {
    EditText edtTen, edtTuoi, edtDiaChi;
    Button btnLuu;

    DatabaseHelper dbHelper;
    int idNhanVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_nhanvien);

        edtTen = findViewById(R.id.edtTen);
        edtTuoi = findViewById(R.id.edtTuoi);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        btnLuu = findViewById(R.id.btnLuu);


        dbHelper = new DatabaseHelper(this);

        // Nhận dữ liệu từ intent
        idNhanVien = getIntent().getIntExtra("id", -1);
        String ten = getIntent().getStringExtra("ten");
        int tuoi = getIntent().getIntExtra("tuoi", 0);
        String diachi = getIntent().getStringExtra("diachi");


        edtTen.setText(ten);
        edtTuoi.setText(String.valueOf(tuoi));
        edtDiaChi.setText(diachi);

        // Hiển thị ảnh



        btnLuu.setOnClickListener(v -> {
            String tenMoi = edtTen.getText().toString().trim();
            int tuoiMoi = Integer.parseInt(edtTuoi.getText().toString().trim());
            String diaChiMoi = edtDiaChi.getText().toString().trim();

            NhanVien nvMoi = new NhanVien(
                    idNhanVien,
                    tenMoi,
                    "",         // username
                    "",         // password
                    "",         // quyền
                    diaChiMoi,
                    "",         // số điện thoại
                    "",         // giới tính
                    tuoiMoi,
                    ""
            );
            boolean success = dbHelper.updateNhanVien(nvMoi);

            if (success) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
