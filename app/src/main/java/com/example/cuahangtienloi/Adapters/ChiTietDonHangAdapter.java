package com.example.cuahangtienloi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuahangtienloi.Models.ChiTietDonHang;
import com.example.cuahangtienloi.R;

import java.io.File;
import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.ViewHolder> {

    private Context context;
    private List<ChiTietDonHang> list;

    public ChiTietDonHangAdapter(Context context, List<ChiTietDonHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChiTietDonHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chitiet_donhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietDonHangAdapter.ViewHolder holder, int position) {
        ChiTietDonHang ct = list.get(position);

        holder.tvTenSP.setText(ct.getTenSanPham());
        holder.tvSoLuong.setText("Số lượng: " + ct.getSoluong());
        holder.tvDonGia.setText(String.format("Đơn giá: %.0f VNĐ", ct.getDongia()));


        String hinhanh = ct.getHinhAnh();

        if (hinhanh != null && !hinhanh.isEmpty()) {
            if (hinhanh.startsWith("/")) {
                // Ảnh là đường dẫn file trong bộ nhớ máy
                Glide.with(context)
                        .load(new File(hinhanh))
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.imgSanPham);
            } else {
                // Ảnh là tên resource drawable
                int resourceId = context.getResources().getIdentifier(hinhanh, "drawable", context.getPackageName());
                if (resourceId != 0) {
                    holder.imgSanPham.setImageResource(resourceId);
                } else {
                    holder.imgSanPham.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }
        } else {
            // Ảnh mặc định khi không có hình
            holder.imgSanPham.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSP, tvSoLuong, tvDonGia;
        ImageView imgSanPham;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSP = itemView.findViewById(R.id.tvTenSanPham);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
        }
    }
}
