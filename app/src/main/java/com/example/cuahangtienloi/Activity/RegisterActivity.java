package com.example.cuahangtienloi.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.R;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edtTen, edtUsername, edtPassword, edtQuyen, edtDiachi, edtSdt, edtGioitinh, edtTuoi;
    Button btnDangKy;
    TextView tvDacotk;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        // Ánh xạ view
        edtTen = findViewById(R.id.edtTen);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtQuyen = findViewById(R.id.edtQuyen);
        edtDiachi = findViewById(R.id.edtDiachi);
        edtSdt = findViewById(R.id.edtSdt);
        edtGioitinh = findViewById(R.id.edtGioitinh);
        edtTuoi = findViewById(R.id.edtTuoi);
        btnDangKy = findViewById(R.id.btnDangKy);
        tvDacotk = findViewById(R.id.tvDacotk); // <- bạn bị thiếu dòng này

        // Xử lý đăng ký
        btnDangKy.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String quyen = edtQuyen.getText().toString().trim();
            String diachi = edtDiachi.getText().toString().trim();
            String sdt = edtSdt.getText().toString().trim();
            String gioitinh = edtGioitinh.getText().toString().trim();
            String tuoiStr = edtTuoi.getText().toString().trim();

            if (ten.isEmpty() || username.isEmpty() || password.isEmpty() || quyen.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int tuoi = 0;
            try {
                tuoi = Integer.parseInt(tuoiStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Tuổi phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase database = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("ten", ten);
            values.put("username", username);
            values.put("password", password);
            values.put("quyen", quyen);
            values.put("diachi", diachi);
            values.put("sodienthoai", sdt);
            values.put("gioitinh", gioitinh);
            values.put("tuoi", tuoi);

            long id = database.insert("nhanvien", null, values);
            if (id > 0) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Không cho quay lại bằng nút Back
            } else {
                Toast.makeText(this, "Đăng ký thất bại. Tên đăng nhập có thể đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        });

        // Chuyển sang màn hình đăng nhập nếu đã có tài khoản
        tvDacotk.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
