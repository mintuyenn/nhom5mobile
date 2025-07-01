package com.example.cuahangtienloi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.LoaiHang;
import com.example.cuahangtienloi.R;

import java.util.List;

public class LoaiHangAdapter extends RecyclerView.Adapter<LoaiHangAdapter.ViewHolder> {

    private Context context;
    private List<LoaiHang> list;
    private OnLoaiClick listener;

    public interface OnLoaiClick {
        void onClick(LoaiHang loaiHang);
        void onDelete(LoaiHang loaiHang);
    }

    public LoaiHangAdapter(Context context, List<LoaiHang> list, OnLoaiClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenLoai, tvSoLuong;
        Button btnXoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenLoai = itemView.findViewById(R.id.tvTenLoai);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_loaihang, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiHang loai = list.get(position);
        holder.tvTenLoai.setText(loai.getTenloai());

        int soLuong = DatabaseHelper.getInstance(context).demSanPhamTheoLoai(loai.getId());
        holder.tvSoLuong.setText("Sản phẩm: " + soLuong);

        // Xử lý khi click vào toàn bộ item
        holder.itemView.setOnClickListener(v -> listener.onClick(loai));

        // Xử lý khi click nút Xóa
        holder.btnXoa.setOnClickListener(v -> listener.onDelete(loai));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
