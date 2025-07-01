package com.example.cuahangtienloi.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Adapters.DonHangAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.DonHang;
import com.example.cuahangtienloi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DonHangActivity extends AppCompatActivity {

    private RecyclerView rvDonHang;
    private DonHangAdapter adapter;
    private List<DonHang> donHangList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donhang);

        rvDonHang = findViewById(R.id.rvDonHang);
        rvDonHang.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton btnThemDonHang = findViewById(R.id.btnThemDonHang);
        btnThemDonHang.setOnClickListener(v -> {
            Intent intent = new Intent(DonHangActivity.this, ThemDonHangActivity.class);
            startActivity(intent);
        });

        loadDonHang();
    }

    private void loadDonHang() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        donHangList = db.getAllDonHang();

        adapter = new DonHangAdapter(this, donHangList);
        rvDonHang.setAdapter(adapter);

        adapter.setOnItemClickListener(donHang -> {
            Intent intent = new Intent(DonHangActivity.this, ChiTietDonHangActivity.class);
            intent.putExtra("donhang_id", donHang.getId());
            startActivity(intent);
        });

        adapter.setOnItemDeleteClickListener((donHang, position) -> {
            new AlertDialog.Builder(DonHangActivity.this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa đơn hàng #" + donHang.getId() + " và tất cả chi tiết đơn hàng liên quan?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        boolean success = db.xoaDonHangVaChiTiet(donHang.getId());
                        if (success) {
                            adapter.removeItem(donHang); // ✅ Xóa theo object
                            Toast.makeText(DonHangActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DonHangActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        adapter.setOnItemEditClickListener((donHang, position) -> {
            showEditStatusDialog(donHang, position);
        });
    }

    private void showEditStatusDialog(DonHang donHang, int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_trangthai);
        dialog.setTitle("Cập nhật trạng thái đơn hàng");

        Spinner spinner = dialog.findViewById(R.id.spinnerStatus);
        Button btnLuu = dialog.findViewById(R.id.btnLuu);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        String[] trangThaiArray = {"Đang xử lý", "Đã Hủy", "Đã thanh toán"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, trangThaiArray);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        int currentPosition = 0;
        for (int i = 0; i < trangThaiArray.length; i++) {
            if (trangThaiArray[i].equals(donHang.getTrangthai())) {
                currentPosition = i;
                break;
            }
        }
        spinner.setSelection(currentPosition);

        btnLuu.setOnClickListener(v -> {
            String newStatus = spinner.getSelectedItem().toString();
            String oldStatus = donHang.getTrangthai();

            if (!newStatus.equals(oldStatus)) {
                DatabaseHelper db = DatabaseHelper.getInstance(this);
                boolean updated = db.updateTrangThaiDonHang(donHang.getId(), newStatus);
                if (updated) {
                    if ((oldStatus.equalsIgnoreCase("Đã hủy")) &&
                            (newStatus.equalsIgnoreCase("Đang xử lý") || newStatus.equalsIgnoreCase("Đã thanh toán"))) {
                        db.truSoLuongSanPhamTheoTrangThai(donHang.getId(), newStatus);
                    } else if ((oldStatus.equalsIgnoreCase("Đang xử lý") || oldStatus.equalsIgnoreCase("Đã thanh toán")) &&
                            (newStatus.equalsIgnoreCase("Đã hủy"))) {
                        db.congSoLuongSanPhamTheoDonHang(donHang.getId());
                    }

                    donHang.setTrangthai(newStatus);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDonHang();
    }
}
