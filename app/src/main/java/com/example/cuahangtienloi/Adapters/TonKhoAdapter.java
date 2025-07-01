package com.example.cuahangtienloi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.R;

import java.io.File;
import java.util.List;

public class TonKhoAdapter extends RecyclerView.Adapter<TonKhoAdapter.TonKhoViewHolder> {

    public interface OnThemSoLuongClickListener {
        void onThemClick(SanPham sanPham);
    }

    private Context context;
    private List<SanPham> sanPhamList;
    private OnThemSoLuongClickListener listener;

    public TonKhoAdapter(Context context, List<SanPham> sanPhamList, OnThemSoLuongClickListener listener) {
        this.context = context;
        this.sanPhamList = sanPhamList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TonKhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tonkho, parent, false);
        return new TonKhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TonKhoViewHolder holder, int position) {
        SanPham sp = sanPhamList.get(position);
        holder.tvTenSP.setText(sp.getTensp());
        holder.tvSoLuongSP.setText("Số lượng: " + sp.getSoluong());

        if (sp.getSoluong() < 20) {
            // Hiện cảnh báo + nút
            holder.layoutCanhBao.setVisibility(View.VISIBLE);
            holder.btnThemSoLuong.setOnClickListener(v -> {
                if (listener != null) listener.onThemClick(sp);
            });
        } else {
            // Ẩn cảnh báo + nút
            holder.layoutCanhBao.setVisibility(View.GONE);
        }

        // Load ảnh
        String hinhanh = sp.getHinhanh();
        if (hinhanh != null && !hinhanh.isEmpty()) {
            if (hinhanh.startsWith("/")) {
                Glide.with(context)
                        .load(new File(hinhanh))
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(holder.imgTonKho);
            } else {
                int resId = context.getResources().getIdentifier(hinhanh, "drawable", context.getPackageName());
                holder.imgTonKho.setImageResource(resId != 0 ? resId : R.drawable.ic_launcher_background);
            }
        } else {
            holder.imgTonKho.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return sanPhamList != null ? sanPhamList.size() : 0;
    }

    public static class TonKhoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTonKho;
        TextView tvTenSP, tvSoLuongSP;
        Button btnThemSoLuong;
        LinearLayout layoutCanhBao;

        public TonKhoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTonKho = itemView.findViewById(R.id.imgTonKho);
            tvTenSP = itemView.findViewById(R.id.tvTenSP);
            tvSoLuongSP = itemView.findViewById(R.id.tvSoLuongSP);
            btnThemSoLuong = itemView.findViewById(R.id.btnThemSoLuong);
            layoutCanhBao = itemView.findViewById(R.id.layoutCanhBao);
        }
    }
}
