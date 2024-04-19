package dev.skyherobrine.app.controllers.dashboardui.thongKe;

import dev.skyherobrine.app.daos.order.ChiTietHoaDonImp;
import dev.skyherobrine.app.views.dashboard.component.FormBaoCaoSanPhamCuaHang;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.util.List;
import java.util.Map;

public class thongKeSanPhamController {
    private FormBaoCaoSanPhamCuaHang formBaoCaoSanPhamCuaHang;
    private ChiTietHoaDonImp chiTietHoaDonImp;
    public thongKeSanPhamController(FormBaoCaoSanPhamCuaHang formBaoCaoSanPhamCuaHang){
        this.formBaoCaoSanPhamCuaHang = formBaoCaoSanPhamCuaHang;
        try {
            this.chiTietHoaDonImp = new ChiTietHoaDonImp();
            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void loadData(){
        PieDataset dataSet = createDataSet();
        JFreeChart chart = createChart(dataSet);
        ChartPanel chartPanel = new ChartPanel(chart);
        formBaoCaoSanPhamCuaHang.getPnBieuDoTHongKeSanPham().removeAll();
        formBaoCaoSanPhamCuaHang.getPnBieuDoTHongKeSanPham().setLayout(new java.awt.BorderLayout());
        formBaoCaoSanPhamCuaHang.getPnBieuDoTHongKeSanPham().add(chartPanel);
    }
    private PieDataset createDataSet() {

        String cols = " TOP 10 TenSP, sum(soLuongMua) as soLuongBan ";
        String join = " cthd inner join HoaDon hd on cthd.MaHD = hd.MaHD inner join PhienBanSanPham pbsp on cthd.MaPhienBanSP = pbsp.MaPhienBanSP inner join SanPham sp on pbsp.MaSP =sp.MaSP";
        String query = " NgayLap >= DATEADD(DAY, -7, GETDATE()) GROUP BY TenSP ORDER BY soLuongBan DESC";
        DefaultPieDataset dataset = new DefaultPieDataset();

        try {
            List<Map<String, Integer>> result = chiTietHoaDonImp.timKiem(cols, join, query);
            String max = "";
            int maxSoLuong = 0;
            for (Map<String, Integer> map : result) {
                for(Map.Entry<String, Integer> entry : map.entrySet()){
                    dataset.setValue(entry.getKey(), entry.getValue());
                    for (Map.Entry<String, Integer> entry1 : map.entrySet()) {
                        if (entry1.getValue() > maxSoLuong) {
                            maxSoLuong = entry1.getValue();
                            max = entry1.getKey();
                        }
                    }
                }
            }
            formBaoCaoSanPhamCuaHang.getTxtBaoCaoDoanhThu1().setText(max);
            formBaoCaoSanPhamCuaHang.getTxtBaoCaoDoanhThu().setEnabled(false);
            formBaoCaoSanPhamCuaHang.getTxtBaoCaoDoanhThu().setText(String.valueOf(maxSoLuong));
            formBaoCaoSanPhamCuaHang.getTxtBaoCaoDoanhThu1().setEnabled(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataset;
    }
    private JFreeChart createChart(PieDataset dataset) {
        return ChartFactory.createPieChart("Biểu đồ biểu hiện top 10 sản phẩm bán chạy", dataset, true, true, false);
    }
}
