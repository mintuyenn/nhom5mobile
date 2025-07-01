package com.example.cuahangtienloi.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // import thêm nút Button
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuahangtienloi.Models.KhachHang;
import com.example.cuahangtienloi.R;

import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {
    private Context context;
    private List<KhachHang> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onSuaClick(KhachHang khachHang, int position);
        void onXoaClick(KhachHang khachHang, int position);
    }

    public KhachHangAdapter(Context context, List<KhachHang> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgKhachHang;
        TextView tvTen, tvSDT, tvEmail, tvDiaChi, tvNgayTao;
        Button btnSua, btnXoa; // Thêm 2 nút Sửa Xóa

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgKhachHang = itemView.findViewById(R.id.imgKhachHang);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            tvNgayTao = itemView.findViewById(R.id.tvNgayTao);

            btnSua = itemView.findViewById(R.id.btnSua); // nhớ có trong layout item_khachhang.xml
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khachhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhachHang kh = list.get(position);

        holder.tvTen.setText(kh.getTen());
        holder.tvSDT.setText("SĐT: " + kh.getSdt());
        holder.tvEmail.setText("Email: " + kh.getEmail());
        holder.tvDiaChi.setText("Địa chỉ: " + kh.getDiachi());
        holder.tvNgayTao.setText("Ngày tạo: " + kh.getNgaytao());

        String hinhAnh = kh.getHinhanh();

        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            if (hinhAnh.startsWith("http") || hinhAnh.contains("/")) {
                Glide.with(context)
                        .load(hinhAnh)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(holder.imgKhachHang);
            } else {
                int resId = context.getResources().getIdentifier(hinhAnh, "drawable", context.getPackageName());
                if (resId != 0) {
                    holder.imgKhachHang.setImageResource(resId);
                } else {
                    holder.imgKhachHang.setImageResource(R.drawable.ic_person);
                }
            }
        } else {
            holder.imgKhachHang.setImageResource(R.drawable.ic_person);
        }

        // Bắt sự kiện nút Sửa
        holder.btnSua.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSuaClick(kh, position);
            }
        });

        // Bắt sự kiện nút Xóa
        holder.btnXoa.setOnClickListener(v -> {
            if (listener != null) {
                listener.onXoaClick(kh, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
