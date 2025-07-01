package com.example.cuahangtienloi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.R;

import java.io.File;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {

    private Context context;
    private List<SanPham> sanPhamList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(SanPham sp, int position);
        void onDeleteClick(SanPham sp);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    public SanPhamAdapter(Context context, List<SanPham> sanPhamList) {
        this.context = context;
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sanpham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham sp = sanPhamList.get(position);

        holder.tvTenSP.setText(sp.getTensp());
        holder.tvGia.setText("Giá: " + sp.getGia() + " VNĐ");
        holder.tvSoLuong.setText("Số lượng: " + sp.getSoluong());

        // Hiển thị hình ảnh (từ bộ nhớ hoặc drawable)
        String imagePath = sp.getHinhanh();
        if (imagePath != null && !imagePath.isEmpty()) {
            if (imagePath.startsWith("/")) {
                // Đường dẫn nội bộ (ảnh được lưu từ bộ nhớ)
                Glide.with(context)
                        .load(new File(imagePath))
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(holder.imgSP);
            } else {
                // Tên ảnh trong drawable
                int resId = context.getResources().getIdentifier(imagePath, "drawable", context.getPackageName());
                if (resId != 0) {
                    holder.imgSP.setImageResource(resId);
                } else {
                    holder.imgSP.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        } else {
            holder.imgSP.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(sp, position);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(sp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public static class SanPhamViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSP, tvGia, tvSoLuong;
        ImageView imgSP;
        Button btnEdit, btnDelete;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSP = itemView.findViewById(R.id.tvTenSP);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            imgSP = itemView.findViewById(R.id.imgSanPham);
            btnEdit = itemView.findViewById(R.id.btnSua);
            btnDelete = itemView.findViewById(R.id.btnXoa);
        }
    }
}
