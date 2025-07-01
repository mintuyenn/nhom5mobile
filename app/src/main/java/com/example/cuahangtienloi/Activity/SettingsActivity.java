package com.example.cuahangtienloi.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode;
    private Button btnBackup, btnRestore, btnAbout;

    private ActivityResultLauncher<String> pickJsonLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnBackup = findViewById(R.id.btnBackup);
        btnRestore = findViewById(R.id.btnRestore);
        btnAbout = findViewById(R.id.btnAbout);

        // Load trạng thái dark mode từ SharedPreferences
        boolean isDark = getSharedPreferences("app_prefs", MODE_PRIVATE).getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDark);
        updateDarkMode(isDark);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("app_prefs", MODE_PRIVATE).edit().putBoolean("dark_mode", isChecked).apply();
            updateDarkMode(isChecked);
        });

        btnBackup.setOnClickListener(v -> backupData());
        btnRestore.setOnClickListener(v -> pickJsonFile());
        btnAbout.setOnClickListener(v -> showAboutDialog());

        // Khởi tạo launcher chọn file JSON
        pickJsonLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        restoreData(uri);
                    }
                }
        );
    }

    private void updateDarkMode(boolean enabled) {
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void backupData() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        List<SanPham> sanPhamList = db.getAllSanPham();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(sanPhamList);

            File folder = new File(getExternalFilesDir(null), "backup");
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folder, "backup_sanpham.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes());
            fos.close();

            Toast.makeText(this, "Sao lưu thành công:\n" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Sao lưu thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickJsonFile() {
        pickJsonLauncher.launch("application/json");
    }

    private void restoreData(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            InputStreamReader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<SanPham>>(){}.getType();
            List<SanPham> sanPhamList = gson.fromJson(reader, listType);
            reader.close();

            DatabaseHelper db = DatabaseHelper.getInstance(this);
            db.clearSanPhamTable(); // Bạn cần tạo hàm này trong DatabaseHelper
            for (SanPham sp : sanPhamList) {
                db.insertSanPham(sp);
            }

            Toast.makeText(this, "Phục hồi dữ liệu thành công", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Phục hồi dữ liệu thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Giới thiệu ứng dụng")
                .setMessage("Ứng dụng Quản lý cửa hàng tiện lợi\n" +
                        "Phiên bản 5.0\n" +
                        "Tác giả: Cao Minh Tuyền\n" +
                        "Năm phát hành: 2025")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
