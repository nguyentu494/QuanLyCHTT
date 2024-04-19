package dev.skyherobrine.app.controllers.dashboardui.person;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JYearChooser;
import dev.skyherobrine.app.daos.person.KhachHangImp;
import dev.skyherobrine.app.entities.person.KhachHang;
import dev.skyherobrine.app.views.dashboard.component.FrmKhachHang;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class KhachHangController implements MouseListener, ActionListener, PropertyChangeListener, KeyListener {
    private FrmKhachHang khachHangUI;
    private KhachHangImp khachHangImp;
    private List<KhachHang> dsKhachHang;


    private static int trangThaiNutXuatFile = 0;
    private static int trangThaiNutThemKH = 0;
    private static int trangThaiNutSuaKH = 0;


    public KhachHangController(FrmKhachHang khachHangUI) {
        try {
            khachHangImp = new KhachHangImp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.khachHangUI = khachHangUI;
    }

    //load danh sách lên table
    public void loaddsKhachHang() {
        DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
        clearTable.setRowCount(0);
        khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
        try {
            dsKhachHang = khachHangImp.timKiem();
            DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
            for(KhachHang kh : dsKhachHang){
                String row[] = {kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(), kh.isGioiTinh() ? "NAM" : "NỮ",
                        kh.getNgaySinh()+"", kh.getDiemTichLuy()+""};
                tmKhachHang.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        trangThaiNutThemKH = 0;
        trangThaiNutSuaKH = 0;
        trangThaiNutXuatFile = 0;
    }
    private static List<KhachHang> dsLoc;
    private List<KhachHang> dsTam = new ArrayList<>();
    @Override
    public void actionPerformed(ActionEvent event) {
        Object op = event.getSource();
        /*NÚT THÊM*/
        if(op.equals(khachHangUI.getButtonThemKhachHang())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ thêm khách hàng
            if (trangThaiNutThemKH==0) {
                khachHangUI.getButtonThemKhachHang().setText("Xác nhận thêm");
                khachHangUI.getButtonSuaKhachHang().setText("Xóa trắng");
                khachHangUI.getButtonXoaKhachHang().setText("Thoát thêm");
                trangThaiNutXuatFile = 1;
                trangThaiNutThemKH = 1;
                trangThaiNutSuaKH = 1;

                //Mở tương tác với thông tin
                tuongTac(true);
                tuongTacTimKiem(false);

                //Xóa trắng dữ liệu
                xoaTrangAll();
                //load sẵn mã khách hàng
                khachHangUI.getTxtMaKhachHang().setText(laymaKH());
                khachHangUI.getTxtMaKhachHang().setEnabled(false);
            }
            // Thực hiện chức năng nghiệp vụ thêm khách hàng
            else if(trangThaiNutThemKH==1) {
                KhachHang kh = layDataThem();
                if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thêm khách hàng mới", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
                    try {
                        if(khachHangImp.them(kh)){
                            loaddsKhachHang();
                            xoaTrangAll();
                            JOptionPane.showMessageDialog(null, "Thêm thành công!");
                            trangThaiNutThemKH = 1;
                            trangThaiNutXuatFile = 1;
                        }else{
                            JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
            //Thực hiện chức năng nghiệp vụ sửa khách hàng
            else if(trangThaiNutThemKH==2){
                if (khachHangUI.getTxtMaKhachHang().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng cần sửa!");
                }else {
                    KhachHang khSua = layDataSua();
                    if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn sửa khách hàng có mã " +khSua.getMaKH()+" không?", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
                        try {
                            if(khachHangImp.capNhat(khSua)){
                                loaddsKhachHang();
                                xoaTrangAll();
                                JOptionPane.showMessageDialog(null, "Sửa thành công!");
                                trangThaiNutThemKH = 2;
                            }else{
                                JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                trangThaiNutXuatFile = 1;
            }
            //Thực hiện chức năng xuất file danh sách khách hàng
            else if(trangThaiNutThemKH==3){
                int result = JOptionPane.showConfirmDialog(null, "Bạn có muốn xuất file danh sách khách hàng này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    DefaultTableModel tmHoaDon = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet sheet = workbook.createSheet("Danh sách khách hàng");
                    XSSFRow row = null;
                    Cell cell = null;
                    row = sheet.createRow(7);
                    cell = row.createCell(0, CellType.STRING);
                    cell.setCellValue("STT");

                    cell = row.createCell(1, CellType.STRING);
                    cell.setCellValue("Mã khách hàng");

                    cell = row.createCell(2, CellType.STRING);
                    cell.setCellValue("Họ và tên");

                    cell = row.createCell(3, CellType.STRING);
                    cell.setCellValue("Số điện thoại");

                    cell = row.createCell(4, CellType.STRING);
                    cell.setCellValue("Giới tính");

                    cell = row.createCell(5, CellType.STRING);
                    cell.setCellValue("Ngày sinh");

                    cell = row.createCell(6, CellType.STRING);
                    cell.setCellValue("Điểm tích lũy");

                    for (int i = 0; i < tmHoaDon.getRowCount(); i++) {
                        row = sheet.createRow(i + 8);

                        cell = row.createCell(0, CellType.STRING);
                        cell.setCellValue(String.valueOf(i+1));

                        cell = row.createCell(1, CellType.STRING);
                        cell.setCellValue(tmHoaDon.getValueAt(i, 0).toString());

                        cell = row.createCell(2, CellType.STRING);
                        cell.setCellValue(tmHoaDon.getValueAt(i, 1).toString());

                        cell = row.createCell(3, CellType.STRING);
                        cell.setCellValue(tmHoaDon.getValueAt(i, 2).toString());

                        cell = row.createCell(4, CellType.STRING);
                        cell.setCellValue(tmHoaDon.getValueAt(i, 3).toString());

                        cell = row.createCell(5, CellType.STRING);
                        cell.setCellValue(tmHoaDon.getValueAt(i, 4).toString());

                        cell = row.createCell(6, CellType.STRING);
                        cell.setCellValue(tmHoaDon.getValueAt(i, 5).toString());
                    }

                    File f = new File("src/main/resources/KhachHang/DanhSachKhachHang.xlsx");
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
        }

        /*NÚT SỬA*/
        if(op.equals(khachHangUI.getButtonSuaKhachHang())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ sửa khách hàng
            if (trangThaiNutSuaKH==0) {
                //Mở tương tác với thông tin
                tuongTac(true);

                khachHangUI.getButtonThemKhachHang().setText("Xác nhận sửa");
                khachHangUI.getButtonSuaKhachHang().setText("Xóa trắng");
                khachHangUI.getButtonXoaKhachHang().setText("Thoát sửa");
                trangThaiNutThemKH = 2;
                trangThaiNutSuaKH = 1;
                trangThaiNutXuatFile = 1;
                khachHangUI.getTxtMaKhachHang().setEnabled(false);
            }
            // Thực hiện xóa trắng dữ liệu ở nghiệp vụ sửa thông tín khách hàng
            else if(trangThaiNutSuaKH==1) {
                xoaTrangSua();
            }
        }

        /*NÚT XUẤT DANH SÁCH KHÁCH HÀNG*/
        if (op.equals(khachHangUI.getButtonXoaKhachHang())) {
            // Thực hiện chức năng nghiệp vụ xuất file danh sách khách hàng
            if (trangThaiNutXuatFile==0) {
                khachHangUI.getButtonThemKhachHang().setText("Xác nhận xuất");
                khachHangUI.getButtonSuaKhachHang().setVisible(false);
                khachHangUI.getButtonXoaKhachHang().setText("Thoát xuất");

                //Mở tương tác cho bộ lọc
                khachHangUI.getjDateChooserTuNgayLocDanhSach().setEnabled(true);
                khachHangUI.getjDateChooserDenNgayLocDanhSachFile().setEnabled(true);
                khachHangUI.getCbThangLoc().setEnabled(true);
                khachHangUI.getjYearChooserNam().setEnabled(true);
                khachHangUI.getBtnLocDanhSach().setEnabled(true);
                khachHangUI.getBtnLamMoiLoc().setEnabled(true);

                trangThaiNutXuatFile = 1;
                trangThaiNutThemKH = 3;
            }
            // Thực hiện trả các nút về giao diện quản lý khách hàng
            else if(trangThaiNutXuatFile==1) {
                tuongTac(false);
                tuongTacTimKiem(true);
                xoaTrangAll();
                khachHangUI.getButtonThemKhachHang().setText("Thêm khách hàng");
                khachHangUI.getButtonSuaKhachHang().setText("Sửa khách hàng");
                khachHangUI.getButtonSuaKhachHang().setVisible(true);
                khachHangUI.getButtonXoaKhachHang().setText("Xuất danh sách");

                //Đóng tương tác bộ lọc
                khachHangUI.getjDateChooserTuNgayLocDanhSach().setEnabled(false);
                khachHangUI.getjDateChooserTuNgayLocDanhSach().setDate(null);

                khachHangUI.getjDateChooserDenNgayLocDanhSachFile().setEnabled(false);
                khachHangUI.getjDateChooserDenNgayLocDanhSachFile().setDate(null);

                khachHangUI.getCbThangLoc().setEnabled(false);
                khachHangUI.getCbThangLoc().setSelectedIndex(0);

                khachHangUI.getjYearChooserNam().setEnabled(false);
                khachHangUI.getBtnLocDanhSach().setEnabled(false);
                khachHangUI.getBtnLamMoiLoc().setEnabled(false);

                trangThaiNutXuatFile = 0;
                trangThaiNutThemKH = 0;
                trangThaiNutSuaKH = 0;
            }
        }

        /*LỌC KHÁCH HÀNG*/
        if(op.equals(khachHangUI.getCbTkGioiTinh())){
            if(khachHangUI.getjDateChooserTkNgaySinh().getDate() == null){
                if(!khachHangUI.getCbTkGioiTinh().getSelectedItem().equals("--Giới tính--")){
                    Map<String, Object> conditions = new HashMap<>();
                    conditions.put("GioiTinh", khachHangUI.getCbTkGioiTinh().getSelectedItem().toString().equals("NAM") ? 1 : 0);
                    try {
                        dsLoc = khachHangImp.timKiem(conditions);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }else{
                    try {
                        dsLoc = khachHangImp.timKiem();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }else {
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("NgaySinh", dateToLocalDate(khachHangUI.getjDateChooserTkNgaySinh().getDate()));
                try {
                    dsLoc = khachHangImp.timKiem(conditions);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                if(!khachHangUI.getCbTkGioiTinh().getSelectedItem().equals("--Giới tính--")){
                    for(int i=0; i<dsLoc.size(); i++){
                        if(dsLoc.get(i).isGioiTinh() == (khachHangUI.getCbTkGioiTinh().getSelectedItem().equals("NAM") ? true : false)){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
            clearTable.setRowCount(0);
            khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
            try {
                DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                for(dev.skyherobrine.app.entities.person.KhachHang kh : dsLoc){
                    String row[] = {kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(), kh.isGioiTinh() ? "NAM" : "NỮ", kh.getNgaySinh()+"", kh.getDiemTichLuy()+""};
                    tmKhachHang.addRow(row);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /*LỌC DANH SÁCH ĐỂ IN*/
        if(op.equals(khachHangUI.getBtnLocDanhSach())){
            if(khachHangUI.getjDateChooserTuNgayLocDanhSach().getDate() != null && khachHangUI.getjDateChooserDenNgayLocDanhSachFile().getDate() != null){
                if(khachHangUI.getjDateChooserTuNgayLocDanhSach().getDate().compareTo(khachHangUI.getjDateChooserDenNgayLocDanhSachFile().getDate()) <= 0){
                    Map<String, Object> conditions1 = new HashMap<>();
                    conditions1.put("SUBSTRING(MaKH, 4, 8) BETWEEN '"+dateToLocalDate(khachHangUI.getjDateChooserTuNgayLocDanhSach().getDate()).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            +"' AND '"+dateToLocalDate(khachHangUI.getjDateChooserDenNgayLocDanhSachFile().getDate()).format(DateTimeFormatter.ofPattern("yyyyMMdd")) +"' AND maKH","");
                    String[] col = {"MaKH", "HoTen", "SoDienThoai", "GioiTinh", "NgaySinh", "DiemTichLuy"};
                    List<Map<String, Object>> kh;
                    try {
                        kh = khachHangImp.timKiem(conditions1, false, col);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                    clearTable.setRowCount(0);
                    khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
                    try {
                        DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                        for(int i=0; i<kh.size(); i++){
                            String row[] = {kh.get(i).get("MaKH")+"", kh.get(i).get("HoTen")+"",  kh.get(i).get("SoDienThoai")+"",
                                    (kh.get(i).get("GioiTinh").equals("0") ? "NỮ" : "NAM"), (kh.get(i).get("NgaySinh")+"").substring(0, 10), kh.get(i).get("DiemTichLuy")+""};
                            tmKhachHang.addRow(row);
                        }
                    } catch (Exception o) {
                        throw new RuntimeException(o);
                    }
                }
                else {
                    Map<String, Object> conditions1 = new HashMap<>();
                    conditions1.put("SUBSTRING(MaKH, 4, 8) BETWEEN '"+dateToLocalDate(khachHangUI.getjDateChooserDenNgayLocDanhSachFile().getDate()).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            +"' AND '"+dateToLocalDate(khachHangUI.getjDateChooserTuNgayLocDanhSach().getDate()).format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"' AND maKH","");
                    String[] col = {"MaKH", "HoTen", "SoDienThoai", "GioiTinh", "NgaySinh", "DiemTichLuy"};
                    List<Map<String, Object>> kh;
                    try {
                        kh = khachHangImp.timKiem(conditions1, false, col);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                    clearTable.setRowCount(0);
                    khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
                    try {
                        DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                        for(int i=0; i<kh.size(); i++){
                            String row[] = {kh.get(i).get("MaKH")+"", kh.get(i).get("HoTen")+"",  kh.get(i).get("SoDienThoai")+"",
                                    (kh.get(i).get("GioiTinh").equals("0") ? "NỮ" : "NAM"), (kh.get(i).get("NgaySinh")+"").substring(0, 10), kh.get(i).get("DiemTichLuy")+""};
                            tmKhachHang.addRow(row);
                        }
                    } catch (Exception o) {
                        throw new RuntimeException(o);
                    }
                }
            }
            else if(khachHangUI.getCbThangLoc().getSelectedIndex() == 0){
                Map<String, Object> conditions2 = new HashMap<>();
                conditions2.put("MaKH", String.valueOf(khachHangUI.getjYearChooserNam().getYear()));
                String[] col = {"MaKH", "HoTen", "SoDienThoai", "GioiTinh", "NgaySinh", "DiemTichLuy"};
                List<Map<String, Object>> kH;
                try {
                    kH = khachHangImp.timKiem(conditions2, false, col);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                clearTable.setRowCount(0);
                khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
                try {
                    DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                    for(int i=0; i<kH.size(); i++){
                        String row[] = {kH.get(i).get("MaKH")+"", kH.get(i).get("HoTen")+"",  kH.get(i).get("SoDienThoai")+"",
                                (kH.get(i).get("GioiTinh").equals("0") ? "NỮ" : "NAM"), (kH.get(i).get("NgaySinh")+"").substring(0, 10), kH.get(i).get("DiemTichLuy")+""};
                        tmKhachHang.addRow(row);
                    }
                } catch (Exception o) {
                    throw new RuntimeException(o);
                }
            }
            else {
                Map<String, Object> conditions2 = new HashMap<>();
                conditions2.put("MaKH", String.valueOf(khachHangUI.getjYearChooserNam().getYear())+khachHangUI.getCbThangLoc().getSelectedItem().toString());
                String[] col = {"MaKH", "HoTen", "SoDienThoai", "GioiTinh", "NgaySinh", "DiemTichLuy"};
                List<Map<String, Object>> kH;
                try {
                    kH = khachHangImp.timKiem(conditions2, false, col);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                clearTable.setRowCount(0);
                khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
                try {
                    DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                    for(int i=0; i<kH.size(); i++){
                        String row[] = {kH.get(i).get("MaKH")+"", kH.get(i).get("HoTen")+"",  kH.get(i).get("SoDienThoai")+"",
                                (kH.get(i).get("GioiTinh").equals("0") ? "NỮ" : "NAM"), (kH.get(i).get("NgaySinh")+"").substring(0, 10), kH.get(i).get("DiemTichLuy")+""};
                        tmKhachHang.addRow(row);
                    }
                } catch (Exception o) {
                    throw new RuntimeException(o);
                }
            }
        }

        if(op.equals(khachHangUI.getBtnLamMoiLoc())){
            khachHangUI.getjDateChooserTuNgayLocDanhSach().setDate(null);
            khachHangUI.getjDateChooserTuNgayLocDanhSach().setEnabled(true);

            khachHangUI.getjDateChooserDenNgayLocDanhSachFile().setDate(null);
            khachHangUI.getjDateChooserDenNgayLocDanhSachFile().setEnabled(true);

            khachHangUI.getjYearChooserNam().setEnabled(true);

            khachHangUI.getCbThangLoc().setSelectedIndex(0);
            khachHangUI.getCbThangLoc().setEnabled(true);

        }
    }

    private static String convertDateToString(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
        return "No Date Selected";
    }


    //Hàm đóng/mở tương tác
    public void tuongTac(boolean c){
        khachHangUI.getTxtMaKhachHang().setEnabled(c);
        khachHangUI.getTxtSoDienThoaiKhachHang().setEnabled(c);
        khachHangUI.getjDateChooserNgaySinhKhachHang().setEnabled(c);
        khachHangUI.getTxtHoTenKhachHang().setEnabled(c);
        khachHangUI.getCbGioiTinh().setEnabled(c);
        khachHangUI.getTxtDiemTichLuy().setEnabled(c);
    }
    //Hàm đóng/mở tương tác tìm kiếm
    public void tuongTacTimKiem(boolean o){
        khachHangUI.getCbTkGioiTinh().setEnabled(o);
        khachHangUI.getjDateChooserTkNgaySinh().setEnabled(o);
        khachHangUI.getTxtTuKhoaTimKiem().setEnabled(o);
    }

    //Hàm xóa trắng sửa
    public void xoaTrangSua(){
        loadComboBoxPhanThongTinNV();
        khachHangUI.getTxtSoDienThoaiKhachHang().setText("");
        khachHangUI.getjDateChooserNgaySinhKhachHang().setDate(null);
        khachHangUI.getTxtHoTenKhachHang().setText("");
        khachHangUI.getTxtDiemTichLuy().setText("");
    }

    //Hàm xóa trắng dữ liệu nhập
    public void xoaTrangAll(){
        loadComboBoxPhanThongTinNV();
        khachHangUI.getTxtMaKhachHang().setText("");
        khachHangUI.getTxtSoDienThoaiKhachHang().setText("");
        khachHangUI.getjDateChooserNgaySinhKhachHang().setDate(null);
        khachHangUI.getTxtHoTenKhachHang().setText("");
        khachHangUI.getTxtDiemTichLuy().setText("");
    }

    //Load các comboBox phần thông tin
    public void loadComboBoxPhanThongTinNV(){
        String[] gioiTinh = {"--Select--", "NAM", "NỮ"};
        DefaultComboBoxModel<String> gioiTinhCb = new DefaultComboBoxModel<>(gioiTinh);
        khachHangUI.getCbGioiTinh().setModel(gioiTinhCb);

    }

    //Load các comboBox phần tìm kiếm
    public void loadComboBoxPhanTimKiem(){
        //Giới tính
        String[] gioiTinh = {"--Giới tính--", "NAM", "NỮ"};
        DefaultComboBoxModel<String> gioiTinhCb = new DefaultComboBoxModel<>(gioiTinh);
        khachHangUI.getCbTkGioiTinh().setModel(gioiTinhCb);
    }

    //Hàm lấy khách hàng từ phần thông tin
    private KhachHang layDataThem() {
        KhachHang kh;
        String sdt = khachHangUI.getTxtSoDienThoaiKhachHang().getText();

        // Lấy ngày từ JDateChooser
        Date selectedDate = khachHangUI.getjDateChooserNgaySinhKhachHang().getDate();
        // Chuyển đổi từ Date sang LocalDate
        LocalDate localDate = dateToLocalDate(selectedDate);

        String ten = khachHangUI.getTxtHoTenKhachHang().getText();
        Boolean gt;
        if(khachHangUI.getCbGioiTinh().getSelectedItem().toString().equals("NAM")){
            gt = true;
        }else {
            gt = false;
        }
        float dtl = Float.parseFloat(khachHangUI.getTxtDiemTichLuy().getText());

        //Mã khách hàng
        String ma = khachHangUI.getTxtMaKhachHang().getText();
        try {
            kh = new KhachHang(ma,ten,sdt,gt,localDate,dtl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return kh;
    }

    //Hàm lấy khách hàng để update
    private KhachHang layDataSua() {
        KhachHang kh;
        String sdt = khachHangUI.getTxtSoDienThoaiKhachHang().getText();

        // Lấy ngày từ JDateChooser
        Date selectedDate = khachHangUI.getjDateChooserNgaySinhKhachHang().getDate();
        // Chuyển đổi từ Date sang LocalDate
        LocalDate localDate1 = dateToLocalDate(selectedDate);

        String ten = khachHangUI.getTxtHoTenKhachHang().getText();
        Boolean gt;
        if(khachHangUI.getCbGioiTinh().getSelectedItem().toString().equals("NAM")){
            gt = true;
        }else {
            gt = false;
        }
        float dtl = Float.parseFloat(khachHangUI.getTxtDiemTichLuy().getText());

        String ma = khachHangUI.getTxtMaKhachHang().getText();
        try {
            kh = new KhachHang(ma,ten,sdt,gt,localDate1,dtl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return kh;
    }

    //Hàm sinh mã khách hàng
    private String laymaKH() {
        String ma = "KH-";
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        ma = ma+nThem;
        ma = ma+"-"+formatNumber(laysoDuoiMaKH());
        return ma;
    }
    //Hàm lấy số đuôi
    public int laysoDuoiMaKH(){
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("MaKH", "%"+nThem+"%");
        List<KhachHang> khachHag;
        try {
            khachHag = khachHangImp.timKiem(conditions);
        } catch (Exception e) {
            return 1;
        }
        if(khachHag.size()==0){
            return 1;
        }
        KhachHang kh = khachHag.get(khachHag.size()-1);
        int soHD = Integer.parseInt(kh.getMaKH().substring(kh.getMaKH().length()-3));
        return soHD+1;
    }

    //Hàm gán thêm số ví dụ 001
    public static String formatNumber(int number) {
        if(number < 10)
            return String.format("00%d", number);
        else if((number >= 10) && (number < 100))
            return String.format("0%d", number);
        else
            return String.format("%d", number);
    }

    //Hàm lấy viết tắt của tên
//    public  String getInitials(String input) {
//        String[] words = input.split("\\s+"); // Sử dụng "\\s+" để tách các từ dựa trên khoảng trắng
//
//        // Lấy chữ cái đầu của từng từ ghép
//        StringBuilder initials = new StringBuilder();
//        for (String word : words) {
//            if (!word.isEmpty()) {
//                initials.append(word.charAt(0));
//            }
//        }
//
//        return initials.toString();
//    }


    //Hàm đổi Date thành LocalDate
    public LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }
    @Override
    public void mouseClicked(MouseEvent event) {
        if(trangThaiNutThemKH==1){
            JOptionPane.showMessageDialog(null, "Đang thực hiện chức năng thêm, không được click!!");
        }else {
            int row = khachHangUI.getTbDanhSachKhachHang().getSelectedRow();
            String ma = khachHangUI.getTbDanhSachKhachHang().getValueAt(row, 0).toString();
            KhachHang khHienThuc = null;
            try {
                khHienThuc = khachHangImp.timKiem(ma);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            khachHangUI.getTxtMaKhachHang().setText(khHienThuc.getMaKH());
            khachHangUI.getTxtHoTenKhachHang().setText(khHienThuc.getHoTen());
            khachHangUI.getTxtSoDienThoaiKhachHang().setText(khHienThuc.getSoDienThoai());
            khachHangUI.getTxtDiemTichLuy().setText(String.valueOf(khHienThuc.getDiemTichLuy()));


            //Xử lý ngày
            String date = String.valueOf(khHienThuc.getNgaySinh());
            Date date2 = null;
            try {
                date2 = new SimpleDateFormat("yyyy-mm-dd").parse(date);
            } catch (ParseException e) {throw new RuntimeException(e);

            }
            khachHangUI.getjDateChooserNgaySinhKhachHang().setDate(date2);
            khachHangUI.getCbGioiTinh().setSelectedItem(khHienThuc.isGioiTinh() ? "NAM" : "NỮ");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    //Tìm kiếm bất kỳ
    @Override
    public void keyReleased(KeyEvent e) {
        if(!khachHangUI.getTxtTuKhoaTimKiem().getText().equals("")){
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("CONCAT(MaKH, HoTen, SoDienThoai, GioiTinh, NgaySinh, DiemTichLuy)", khachHangUI.getTxtTuKhoaTimKiem().getText());
            String[] col = {"MaKH", "HoTen", "SoDienThoai", "GioiTinh", "NgaySinh", "DiemTichLuy"};
            List<Map<String, Object>> kh;
            try {
                kh = khachHangImp.timKiem(conditions, false, col);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
            clearTable.setRowCount(0);
            khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
            try {
                DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                for(int i=0; i<kh.size(); i++){
                    String row[] = {kh.get(i).get("MaKH")+"", kh.get(i).get("HoTen")+"",  kh.get(i).get("SoDienThoai")+"",
                            (kh.get(i).get("GioiTinh").equals("0") ? "NỮ" : "NAM"), (kh.get(i).get("NgaySinh")+"").substring(0, 10), kh.get(i).get("DiemTichLuy")+""};
                    tmKhachHang.addRow(row);
                }
            } catch (Exception o) {
                throw new RuntimeException(o);
            }
        }else {
            System.out.println(1);
            DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
            clearTable.setRowCount(0);
            khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
            try {
                dsKhachHang = khachHangImp.timKiem();
                DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                for(KhachHang kh : dsKhachHang){
                    String row[] = {kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(), kh.isGioiTinh() ? "NAM" : "NỮ", kh.getNgaySinh()+"", kh.getDiemTichLuy()+""};
                    tmKhachHang.addRow(row);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //Lọc khách hàng
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        handlePropertyChange(evt);
    }

    private void handlePropertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        String propertyName = evt.getPropertyName();
        khachHangUI.getjDateChooserTkNgaySinh().setName("NgaySinhTK");
        khachHangUI.getjDateChooserTuNgayLocDanhSach().setName("TuNgay");
        khachHangUI.getjDateChooserDenNgayLocDanhSachFile().setName("DenNgay");
        khachHangUI.getjYearChooserNam().setName("LocNam");
        if ("date".equals(propertyName) && source instanceof JDateChooser) {
            JDateChooser dateChooser = (JDateChooser) source;
            String dateChooserName = dateChooser.getName();

            if ("NgaySinhTK".equals(dateChooserName)) {
                if(!khachHangUI.getCbTkGioiTinh().getSelectedItem().equals("--Giới tính--")){
                    Map<String, Object> conditions = new HashMap<>();
                    conditions.put("GioiTinh", khachHangUI.getCbTkGioiTinh().getSelectedItem().toString().equals("NAM") ? 1 : 0);
                    try {
                        dsLoc = khachHangImp.timKiem(conditions);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    for (int i=0; i<dsLoc.size(); i++){
                        if(dsLoc.get(i).getNgaySinh().equals(dateToLocalDate(khachHangUI.getjDateChooserTkNgaySinh().getDate()))){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                }else {
                    Map<String, Object> conditions = new HashMap<>();
                    conditions.put("NgaySinh", dateToLocalDate(khachHangUI.getjDateChooserTkNgaySinh().getDate()));
                    try {
                        dsLoc = khachHangImp.timKiem(conditions);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }

                DefaultTableModel clearTable = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                clearTable.setRowCount(0);
                khachHangUI.getTbDanhSachKhachHang().setModel(clearTable);
                try {
                    DefaultTableModel tmKhachHang = (DefaultTableModel) khachHangUI.getTbDanhSachKhachHang().getModel();
                    for(KhachHang kh : dsLoc){
                        String row[] = {kh.getMaKH(), kh.getHoTen(), kh.getSoDienThoai(), kh.isGioiTinh() ? "NAM" : "NỮ", kh.getNgaySinh()+"", kh.getDiemTichLuy()+""};
                        tmKhachHang.addRow(row);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if ("TuNgay".equals(dateChooserName) || "DenNgay".equals(dateChooserName)) {
                khachHangUI.getCbThangLoc().setEnabled(false);
                khachHangUI.getjYearChooserNam().setEnabled(false);
            }
        }
        if("year".equals(propertyName) && source instanceof JYearChooser){
            JYearChooser yearChooser = (JYearChooser) source;
            String yearChooserName = yearChooser.getName();

            if("LocNam".equals(yearChooserName)){
                khachHangUI.getjDateChooserTuNgayLocDanhSach().setEnabled(false);
                khachHangUI.getjDateChooserDenNgayLocDanhSachFile().setEnabled(false);
                khachHangUI.getCbThangLoc().setEnabled(true);
            }
        }
    }
}
