package dev.skyherobrine.app.controllers.dashboardui.thongKe;

import dev.skyherobrine.app.daos.order.ChiTietHoaDonImp;
import dev.skyherobrine.app.daos.order.ChiTietPhieuNhapHangImp;
import dev.skyherobrine.app.daos.product.SanPhamImp;
import dev.skyherobrine.app.entities.product.SanPham;
import dev.skyherobrine.app.views.dashboard.component.FormBaoCaoDoanhThuCuaHang;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class thongKeDoanhThuController {
    private FormBaoCaoDoanhThuCuaHang formBaoCaoDoanhThuCuaHang;
    private ChiTietHoaDonImp chiTietHoaDonImp;
    private ChiTietPhieuNhapHangImp chiTietPhieuNhapHangImp;
    private SanPhamImp sanPhamImp;

    public thongKeDoanhThuController(FormBaoCaoDoanhThuCuaHang formBaoCaoDoanhThuCuaHang){
        this.formBaoCaoDoanhThuCuaHang = formBaoCaoDoanhThuCuaHang;
        try {
            this.chiTietHoaDonImp = new ChiTietHoaDonImp();
            this.chiTietPhieuNhapHangImp = new ChiTietPhieuNhapHangImp();
            this.sanPhamImp = new SanPhamImp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        loadData();
    }
    public void loadData(){
        DefaultCategoryDataset dataset = createDataSet();

        JFreeChart chart = ChartFactory.createLineChart("", "Date", "Number of Visitor", dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        formBaoCaoDoanhThuCuaHang.getPnBieuDoTHongKeDoanhThu().removeAll();
        formBaoCaoDoanhThuCuaHang.getPnBieuDoTHongKeDoanhThu().setLayout(new java.awt.BorderLayout());
        formBaoCaoDoanhThuCuaHang.getPnBieuDoTHongKeDoanhThu().add(chartPanel);
    }
    public DefaultCategoryDataset createDataSet() {
        String series_1 =  "Doanh Thu";


        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String cols = " MaSP, SUM(SoLuongMua) as SoLuongBan, NgayLap";
        String join = "cthd inner join HoaDon hd on cthd.MaHD = hd.MaHD inner join PhienBanSanPham pbsp on cthd.MaPhienBanSP = pbsp.MaPhienBanSP";
        String query = " NgayLap >= DATEADD(DAY, -7, GETDATE())  group by MaSP, NgayLap";

        try {
            List<Map<String, Object>> result = chiTietHoaDonImp.timKiemHD(cols, join, query);
            double doanhthu = 0;
            for (Map<String, Object> map : result) {
                for(Map.Entry<String, Object> entry : map.entrySet()){
//                    Optional<SanPham> sp  = sanPhamImp.timKiem(entry.getKey());
                    SanPham sp = new SanPham();
                    Map<String, Object> conditions = new HashMap<>();
                    conditions.put("MaSP", entry.getKey());
                    sp.setChiTietPhieuNhapHangs(chiTietPhieuNhapHangImp.timKiem(conditions));
                    double dongia = sp.giaBan();
                    Map<String, Integer> data = (Map<String, Integer>) entry.getValue();
                    for(Map.Entry<String, Integer> entry1 : data.entrySet()){
                        dataset.addValue(entry1.getValue()*dongia, series_1, entry1.getKey().substring(0,10));
                        doanhthu =dongia*entry1.getValue();
                    }

               }
            }
            formBaoCaoDoanhThuCuaHang.getTxtBaoCaoDoanhThu().setText(String.valueOf(doanhthu));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return dataset;
    }
}
