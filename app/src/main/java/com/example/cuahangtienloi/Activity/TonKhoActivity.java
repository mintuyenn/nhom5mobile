package com.example.cuahangtienloi.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Adapters.TonKhoAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.LoaiHang;
import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.R;

import java.util.ArrayList;
import java.util.List;

public class TonKhoActivity extends AppCompatActivity {

    RecyclerView rvTonKho;
    Spinner spinnerLoaiHang;
    TextView tvTongSanPham, tvTongSoLuong;

    TonKhoAdapter adapter;
    List<SanPham> sanPhamList;
    List<LoaiHang> loaiHangList;
    ArrayAdapter<String> loaiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tonkho);

        initUI();
    }

    private void initUI() {
        rvTonKho = findViewById(R.id.rvTonKho);
        spinnerLoaiHang = findViewById(R.id.spinnerLoaiHang);
        tvTongSanPham = findViewById(R.id.tvTongSanPham);
        tvTongSoLuong = findViewById(R.id.tvTongSoLuong);

        rvTonKho.setLayoutManager(new LinearLayoutManager(this));
        sanPhamList = new ArrayList<>();

        adapter = new TonKhoAdapter(this, sanPhamList, sanPham -> showDialogThemSoLuong(sanPham));
        rvTonKho.setAdapter(adapter);

        loadLoaiHang();
    }

    private void loadLoaiHang() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        loaiHangList = db.getAllLoaiHang();

        List<String> tenLoai = new ArrayList<>();
        tenLoai.add("Tất cả");
        for (LoaiHang tl : loaiHangList) {
            tenLoai.add(tl.getTenloai());
        }

        loaiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenLoai);
        loaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiHang.setAdapter(loaiAdapter);

        spinnerLoaiHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos == 0) {
                    loadSanPhamTheoLoai(-1);
                } else {
                    int idLoai = loaiHangList.get(pos - 1).getId();
                    loadSanPhamTheoLoai(idLoai);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        spinnerLoaiHang.setSelection(0);
    }

    private void loadSanPhamTheoLoai(int idLoai) {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        List<SanPham> list;
        if (idLoai == -1) {
            list = db.getAllSanPham();
        } else {
            list = db.getSanPhamTheoLoai(idLoai);
        }

        sanPhamList.clear();
        sanPhamList.addAll(list);
        adapter.notifyDataSetChanged();

        // Cập nhật thống kê
        tvTongSanPham.setText("📦 Tổng sản phẩm: " + sanPhamList.size());

        int tongSL = 0;
        for (SanPham sp : sanPhamList) {
            tongSL += sp.getSoluong();
        }
        tvTongSoLuong.setText("📊 Tổng số lượng còn lại: " + tongSL);
    }

    private void showDialogThemSoLuong(SanPham sanPham) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập số lượng muốn thêm");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Ví dụ: 10");
        input.setPadding(50, 30, 50, 30);
        builder.setView(input);

        builder.setPositiveButton("Thêm", null); // Tạm set null để custom xử lý nút

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(d -> {
            Button btnThem = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnThem.setOnClickListener(v -> {
                String soLuongStr = input.getText().toString().trim();
                if (soLuongStr.isEmpty()) {
                    input.setError("Vui lòng nhập số lượng");
                    return;
                }
                int soThem;
                try {
                    soThem = Integer.parseInt(soLuongStr);
                    if (soThem <= 0) {
                        input.setError("Số lượng phải lớn hơn 0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    input.setError("Vui lòng nhập số hợp lệ");
                    return;
                }

                int moi = sanPham.getSoluong() + soThem;

                DatabaseHelper db = DatabaseHelper.getInstance(this);
                db.capNhatSoLuongSanPham(sanPham.getId(), moi);

                // Load lại sản phẩm theo loại hiện tại
                int currentLoaiPosition = spinnerLoaiHang.getSelectedItemPosition();
                int currentLoaiId = currentLoaiPosition == 0 ? -1 : loaiHangList.get(currentLoaiPosition - 1).getId();
                loadSanPhamTheoLoai(currentLoaiId);

                dialog.dismiss();
            });
        });

        dialog.show();
    }

}
