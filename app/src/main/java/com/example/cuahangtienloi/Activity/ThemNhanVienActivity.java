package com.example.cuahangtienloi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.NhanVien;
import com.example.cuahangtienloi.R;

public class ThemNhanVienActivity extends AppCompatActivity {

    EditText edtTen, edtTuoi, edtDiachi, edtSdt, edtUsername, edtPassword, edtGioitinh, edtQuyen;
    Button btnLuuNV;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtTen = findViewById(R.id.edtTen);
        edtGioitinh = findViewById(R.id.edtGioitinh);
        edtTuoi = findViewById(R.id.edtTuoi);
        edtQuyen = findViewById(R.id.edtQuyen);
        edtDiachi = findViewById(R.id.edtDiachi);
        edtSdt = findViewById(R.id.edtSdt);
        btnLuuNV = findViewById(R.id.btnLuuNV);

        db = new DatabaseHelper(this);

        btnLuuNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edtTen.getText().toString().trim();
                String tuoiStr = edtTuoi.getText().toString().trim();
                String diachi = edtDiachi.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String gioitinh = edtGioitinh.getText().toString().trim();
                String quyen = edtQuyen.getText().toString().trim();

                if (ten.isEmpty() || tuoiStr.isEmpty() || diachi.isEmpty() || sdt.isEmpty()
                        || username.isEmpty() || password.isEmpty() || gioitinh.isEmpty() || quyen.isEmpty()) {
                    Toast.makeText(ThemNhanVienActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int tuoi;
                try {
                    tuoi = Integer.parseInt(tuoiStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(ThemNhanVienActivity.this, "Tuổi phải là số!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(db.isUsernameExists(username)) {
                    Toast.makeText(ThemNhanVienActivity.this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                NhanVien nv = new NhanVien();
                nv.setTen(ten);
                nv.setTuoi(tuoi);
                nv.setDiachi(diachi);
                nv.setSodienthoai(sdt);
                nv.setUsername(username);
                nv.setPassword(password);
                nv.setGioitinh(gioitinh);
                nv.setQuyen(quyen);

                boolean result = db.insertNhanVien(nv);

                if (result) {
                    Toast.makeText(ThemNhanVienActivity.this, "Đã thêm nhân viên!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Báo lại cho NhanVienActivity biết cần load lại danh sách
                    finish(); // Quay về
                } else {
                    Toast.makeText(ThemNhanVienActivity.this, "Lỗi khi thêm nhân viên!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
