package com.example.cuahangtienloi.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.formatter.ValueFormatter;

import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    Spinner spinnerLoaiThongKe, spinnerMonth, spinnerYearForMonth, spinnerYearOnly;
    LinearLayout layoutMonthYear, layoutYearOnly;
    BarChart barChart;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);

        spinnerLoaiThongKe = findViewById(R.id.spinnerLoaiThongKe);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYearForMonth = findViewById(R.id.spinnerYearForMonth);
        spinnerYearOnly = findViewById(R.id.spinnerYearOnly);
        layoutMonthYear = findViewById(R.id.layoutMonthYear);
        layoutYearOnly = findViewById(R.id.layoutYearOnly);
        barChart = findViewById(R.id.barChart);

        db = DatabaseHelper.getInstance(this);

        setupSpinners();
        setupListeners();
    }

    private void setupSpinners() {
        // Loại thống kê
        List<String> loaiThongKe = new ArrayList<>();
        loaiThongKe.add("Theo ngày");
        loaiThongKe.add("Theo tháng");
        loaiThongKe.add("Theo năm");
        spinnerLoaiThongKe.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiThongKe));

        // Tháng
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) months.add("Tháng " + i);
        spinnerMonth.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months));

        // Năm (2022 - năm hiện tại)
        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = currentYear; y >= 2022; y--) years.add(String.valueOf(y));
        spinnerYearForMonth.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years));
        spinnerYearOnly.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years));

        // Mặc định ẩn
        layoutMonthYear.setVisibility(View.GONE);
        layoutYearOnly.setVisibility(View.GONE);

        spinnerLoaiThongKe.setSelection(0);
    }

    private void setupListeners() {
        spinnerLoaiThongKe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Theo ngày => hiện tháng năm
                    case 1: // Theo tháng => hiện tháng năm
                        layoutMonthYear.setVisibility(View.VISIBLE);
                        layoutYearOnly.setVisibility(View.GONE);
                        break;
                    case 2: // Theo năm
                        layoutMonthYear.setVisibility(View.GONE);
                        layoutYearOnly.setVisibility(View.VISIBLE);
                        break;
                }
                updateChart();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        AdapterView.OnItemSelectedListener updateChartListener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { updateChart(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerMonth.setOnItemSelectedListener(updateChartListener);
        spinnerYearForMonth.setOnItemSelectedListener(updateChartListener);
        spinnerYearOnly.setOnItemSelectedListener(updateChartListener);
    }

    private void updateChart() {
        int loai = spinnerLoaiThongKe.getSelectedItemPosition();

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        if (loai == 0) { // Theo ngày trong tháng, chỉ 3 ngày gần nhất
            int month = spinnerMonth.getSelectedItemPosition() + 1;
            int year = Integer.parseInt(spinnerYearForMonth.getSelectedItem().toString());

            List<Float> doanhThu = db.getDoanhThuTheoNgayTrongThang(month, year);

            int totalDays = doanhThu.size();
            int startIndex = Math.max(0, totalDays - 3);  // Lấy 3 ngày cuối hoặc ít hơn

            for (int i = startIndex; i < totalDays; i++) {
                entries.add(new BarEntry(i - startIndex + 1, doanhThu.get(i)));
                labels.add("Ngày " + (i + 1)); // ví dụ: Ngày 28
            }

        } else if (loai == 1) { // Theo tháng trong năm
            int year = Integer.parseInt(spinnerYearForMonth.getSelectedItem().toString());

            List<Float> doanhThu = db.getDoanhThuTheoThangTrongNam(year);

            for (int i = 0; i < doanhThu.size(); i++) {
                entries.add(new BarEntry(i + 1, doanhThu.get(i)));
                labels.add("" + (i + 1));
            }
        } else { // Theo năm
            int year = Integer.parseInt(spinnerYearOnly.getSelectedItem().toString());

            float doanhThuNam = db.getDoanhThuTheoNam(year);
            entries.add(new BarEntry(1, doanhThuNam)); // X=1 thay vì 0 để dễ định label
            labels.add("Năm " + year);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu (VNĐ)");
        dataSet.setColor(getResources().getColor(R.color.purple_500));
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(getResources().getColor(R.color.black));
        dataSet.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f", value); // làm tròn cho dễ đọc
            }
        });

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.7f); // tạo khoảng cách giữa các cột

        barChart.setData(data);

        // Trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(getResources().getColor(R.color.black));
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = (int) value - 1;
                if (idx >= 0 && idx < labels.size()) {
                    return labels.get(idx);
                }
                return "";
            }
        });

        // Trục Y
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setTextSize(12f);
        barChart.getAxisLeft().setTextColor(getResources().getColor(R.color.black));
        barChart.getAxisLeft().setDrawGridLines(true);
        barChart.getAxisRight().setEnabled(false);

        // Tắt mô tả góc dưới
        barChart.getDescription().setEnabled(false);

        // Tăng khoảng cách dòng "Doanh thu (VNĐ)" (Legend) so với biểu đồ
        barChart.getLegend().setVerticalAlignment(com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM);
        barChart.getLegend().setYOffset(30f); // Cách ra 24dp
        barChart.getLegend().setTextSize(14f); // Tăng cỡ chữ nếu cần

        barChart.setFitBars(true);
        barChart.animateY(800);
        barChart.invalidate(); // Vẽ lại biểu đồ
    }


}
