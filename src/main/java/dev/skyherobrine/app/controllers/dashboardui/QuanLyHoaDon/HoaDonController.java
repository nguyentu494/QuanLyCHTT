package dev.skyherobrine.app.controllers.dashboardui.QuanLyHoaDon;


import com.lowagie.text.pdf.PdfDocument;
import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.order.ChiTietHoaDonDAO;
import dev.skyherobrine.app.daos.order.ChiTietPhieuNhapHangDAO;
import dev.skyherobrine.app.daos.order.HoaDonDAO;
import dev.skyherobrine.app.daos.product.SanPhamDAO;
import dev.skyherobrine.app.entities.order.ChiTietHoaDon;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import dev.skyherobrine.app.entities.order.HoaDon;
import dev.skyherobrine.app.entities.product.SanPham;
import dev.skyherobrine.app.views.dashboard.component.QuanLyHoaDon;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;

public class HoaDonController implements MouseListener, ActionListener, KeyListener, PropertyChangeListener {
    private static QuanLyHoaDon hoaDonUI;
    private static List<HoaDon> dsHoaDon;
    private static HoaDonDAO hoaDonDAO;
    private static List<ChiTietHoaDon> dsChiTietHoaDon;
    private static ChiTietHoaDonDAO chiTietHoaDonDAO;
    private static SanPhamDAO sanPhamDAO;
    private static ConnectDB connectDB;
    private static ChiTietPhieuNhapHangDAO chiTietPhieuNhapHangDAO;
    public HoaDonController(QuanLyHoaDon hoaDonUI) {
        try {
            this.hoaDonUI = hoaDonUI;
            hoaDonDAO = new HoaDonDAO();
            this.connectDB = new ConnectDB();
            chiTietHoaDonDAO = new ChiTietHoaDonDAO();
            sanPhamDAO = new SanPhamDAO();
            chiTietPhieuNhapHangDAO = new ChiTietPhieuNhapHangDAO();
            loadDsHoaDon();
            loadCbMucTien();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void loadDsHoaDon() throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultTableModel tmHoaDon= (DefaultTableModel) hoaDonUI.getTbDanhSachHoaDon().getModel();
                tmHoaDon.setRowCount(0);
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("NgayLap >= DATEADD(DAY, -7, GETDATE()) AND MaHD", "%%");
                try {
                    dsHoaDon = hoaDonDAO.timKiem(conditions);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                double tongTien = 0;
                for(HoaDon hd : dsHoaDon){
                    String row[] = {hd.getMaHD(), hd.getNgayLap()+"", hd.getNhanVienLap().getHoTen(), hd.getKhachHang().getHoTen()};
                    tmHoaDon.addRow(row);
                }
            }
        }).start();
    }
    public static void loadCbMucTien(){
        hoaDonUI.getCbTkCaLamViec().addItem("Tất cả");
        hoaDonUI.getCbTkCaLamViec().addItem("0 - 1 triệu");
        hoaDonUI.getCbTkCaLamViec().addItem("1 - 5 triệu");
        hoaDonUI.getCbTkCaLamViec().addItem("5 - 10 triệu");
        hoaDonUI.getCbTkCaLamViec().addItem("hơn 10 triệu");
    }
    public double tinhTongTien(String maHD){
        try {
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("MaHD", maHD);

            List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDonDAO.timKiem(conditions);
            double tongTien = 0;
            for(ChiTietHoaDon chiTietHoaDon : chiTietHoaDons){
                Map<String, Object> conditionsSP = new HashMap<>();
                Optional<SanPham> sanPham = Optional.of(new SanPham());
//                conditionsSP.put("MaSP", chiTietHoaDon.getChiTietPhienBanSanPham().getSanPham().getMaSP());
//                Optional<SanPham> sanPham = sanPhamDAO.timKiem(chiTietHoaDon.getChiTietPhienBanSanPham().getSanPham().getMaSP());
//                sanPham.get().setChiTietPhieuNhapHangs(chiTietPhieuNhapHangDAO.timKiem(conditionsSP));
                tongTien += chiTietHoaDon.getSoLuongMua() * sanPham.get().giaBan();
            }
            return tongTien;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void xemHoaDon(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int row = hoaDonUI.getTbDanhSachHoaDon().getSelectedRow();
                DefaultTableModel tmHoaDon = (DefaultTableModel) hoaDonUI.getTbDanhSachHoaDon().getModel();
                String maHD = hoaDonUI.getTbDanhSachHoaDon().getValueAt(row, 0).toString();
                hoaDonUI.getPnHoaDon().removeAll();
                hoaDonUI.getPnHoaDon().repaint();
                hoaDonUI.getPnHoaDon().revalidate();
                Map<String, Object> data= new HashMap<>();
                data.put("maHD", maHD);
                Map<String, Object> conditions= new HashMap<>();
                conditions.put("MaHD", maHD);
                List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
                BigDecimal tienKhachDua = null;
                DecimalFormat format = new DecimalFormat("0.00");
                try {
                    chiTietHoaDons = chiTietHoaDonDAO.timKiem(conditions);
                    tienKhachDua = hoaDonDAO.timKiem(maHD).get().getSoTienKHTra();
                    for(ChiTietHoaDon cthd : chiTietHoaDons){
                        Map<String, Object> conditionsSP = new HashMap<>();
                        Optional<SanPham> sanPham = Optional.of(new SanPham());

//                        conditionsSP.put("MaSP", cthd.getChiTietPhienBanSanPham().getSanPham().getMaSP());
//                        Optional<SanPham> sanPham = sanPhamDAO.timKiem(cthd.getChiTietPhienBanSanPham().getSanPham().getMaSP());
                        sanPham.get().setChiTietPhieuNhapHangs(chiTietPhieuNhapHangDAO.timKiem(conditionsSP));
                        data.put("ThanhTien", format.format(cthd.getSoLuongMua() * sanPham.get().giaBan()));
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                data.put("TongTien", tmHoaDon.getValueAt(row, 3).toString());
                data.put("tienKhachDua", tienKhachDua);
                data.put("tienThua",tienKhachDua.doubleValue());

                try {
                    JasperDesign jasperDesign = JRXmlLoader.load(new File("src/main/resources/HoaDon/hoadon.jrxml"));
                    JasperReport jreport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, data, connectDB.getConnection());
                    JRViewer v = new JRViewer(jprint);
                    hoaDonUI.getPnHoaDon().setLayout(new BorderLayout());
                    hoaDonUI.getPnHoaDon().add(v);
                }catch (Exception ex){
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }
    public static void inHoaDon(){
            int result = JOptionPane.showConfirmDialog(null, "Bạn có muốn in hóa đơn này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                int row = hoaDonUI.getTbDanhSachHoaDon().getSelectedRow();
                DefaultTableModel tmHoaDon = (DefaultTableModel) hoaDonUI.getTbDanhSachHoaDon().getModel();
                String maHD = hoaDonUI.getTbDanhSachHoaDon().getValueAt(row, 0).toString();
                hoaDonUI.getPnHoaDon().removeAll();
                hoaDonUI.getPnHoaDon().repaint();
                hoaDonUI.getPnHoaDon().revalidate();
                Map<String, Object> data= new HashMap<>();
                data.put("maHD", maHD);
                Map<String, Object> conditions= new HashMap<>();
                conditions.put("MaHD", maHD);
                List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
                BigDecimal tienKhachDua = null;
                try {
                    chiTietHoaDons = chiTietHoaDonDAO.timKiem(conditions);
                    tienKhachDua = hoaDonDAO.timKiem(maHD).get().getSoTienKHTra();
                    for(ChiTietHoaDon cthd : chiTietHoaDons){
                        Map<String, Object> conditionsSP = new HashMap<>();
                        Optional<SanPham> sanPham = Optional.of(new SanPham());

//                        conditionsSP.put("MaSP", cthd.getChiTietPhienBanSanPham().getSanPham().getMaSP());
//                        Optional<SanPham> sanPham = sanPhamDAO.timKiem(cthd.getChiTietPhienBanSanPham().getSanPham().getMaSP());
                        sanPham.get().setChiTietPhieuNhapHangs(chiTietPhieuNhapHangDAO.timKiem(conditionsSP));
                        data.put("ThanhTien", cthd.getSoLuongMua() * sanPham.get().giaBan());
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                data.put("TongTien", tmHoaDon.getValueAt(row, 4).toString());
                data.put("tienKhachDua", tienKhachDua);
                data.put("tienThua", abs(Double.parseDouble(tmHoaDon.getValueAt(row, 4).toString()) - tienKhachDua.doubleValue()));

                try {
                    JasperDesign jasperDesign = JRXmlLoader.load(new File("src/main/resources/HoaDon/hoadon.jrxml"));
                    JasperReport jreport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, data, connectDB.getConnection());
                    JasperViewer.viewReport(jprint, false);
                    JasperExportManager.exportReportToPdfFile(jprint, "src/main/resources/HoaDon/DanhSachHoaDonInLai/"+ maHD +".pdf");
                }catch (Exception ex){
                    throw new RuntimeException(ex);
                }
            }

    }
    public void xuatHDsHoaDon(){
        int result = JOptionPane.showConfirmDialog(null, "Bạn có muốn xuất file danh sách hóa đơn này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_OPTION) {
            DefaultTableModel tmHoaDon = (DefaultTableModel) hoaDonUI.getTbDanhSachHoaDon().getModel();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Danh sách hóa đơn");
            XSSFRow row = null;
            Cell cell = null;
            row = sheet.createRow(5);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Mã hóa đơn");

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue("Ngày lập");

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("Nhân viên lập");

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue("Khách hàng");

            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("Tổng tiền");

            for (int i = 0; i < tmHoaDon.getRowCount(); i++) {
                row = sheet.createRow(i + 6);

                cell = row.createCell(0, CellType.STRING);
                cell.setCellValue(tmHoaDon.getValueAt(i, 0).toString());

                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(tmHoaDon.getValueAt(i, 1).toString());

                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(tmHoaDon.getValueAt(i, 2).toString());

                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(tmHoaDon.getValueAt(i, 3).toString());

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(tmHoaDon.getValueAt(i, 4).toString());

            }

            File f = new File("src/main/resources/HoaDon/ExcelHoaDon/DanhSachHoaDon.xlsx");
            try {
                FileOutputStream fos = new FileOutputStream(f);
                workbook.write(fos);
                fos.close();

            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(null, "Xuất file thành công");
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        xemHoaDon();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    public LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().toString().equalsIgnoreCase(hoaDonUI.getBtnInHoaDon().toString())) {
            inHoaDon();
        }else if(e.getSource().toString().equalsIgnoreCase(hoaDonUI.getBtnXuatHoaDon().toString())){
            xuatHDsHoaDon();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) hoaDonUI.getTbDanhSachHoaDon().getModel());
        hoaDonUI.getTbDanhSachHoaDon().setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(hoaDonUI.getTxtTuKhoaTimKiem().getText()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getSource().toString().equalsIgnoreCase(hoaDonUI.getjDateChooserNgayLapHoaDon().toString())){
            Date selectedDate = hoaDonUI.getjDateChooserNgayLapHoaDon().getDate();
            LocalDate localDate = dateToLocalDate(selectedDate);
            if (selectedDate != null) {
                String nlap = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) hoaDonUI.getTbDanhSachHoaDon().getModel());
                hoaDonUI.getTbDanhSachHoaDon().setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter(nlap));
            }
        }
    }
}
