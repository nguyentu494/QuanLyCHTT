package dev.skyherobrine.app.controllers.dashboardui.person;

import dev.skyherobrine.app.daos.person.NhanVienImp;
import dev.skyherobrine.app.entities.person.NhanVien;

import dev.skyherobrine.app.enums.*;
import dev.skyherobrine.app.views.dashboard.component.FrmNhanVien;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class NhanVienController implements ActionListener, MouseListener, KeyListener {
    private FrmNhanVien nhanVienUI;
    private NhanVienImp nhanVienImp;
    private List<NhanVien> dsNhanVien;


    private static int trangThaiNutXoaNV = 0;
    private static int trangThaiNutThemNV = 0;
    private static int trangThaiNutSuaNV = 0;
    private static String fileAnhNV = "";


    public NhanVienController(FrmNhanVien nhanVienUI) {
        try {
            nhanVienImp = new NhanVienImp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.nhanVienUI = nhanVienUI;
    }

    //load danh sách lên table
    public void loadDsNhanVien() {
        DefaultTableModel clearTable = (DefaultTableModel) nhanVienUI.getTbDanhSachNhanVien().getModel();
        clearTable.setRowCount(0);
        nhanVienUI.getTbDanhSachNhanVien().setModel(clearTable);
        try {
            dsNhanVien = nhanVienImp.timKiem();
            DefaultTableModel tmNhanVien = (DefaultTableModel) nhanVienUI.getTbDanhSachNhanVien().getModel();
            for(dev.skyherobrine.app.entities.person.NhanVien nv : dsNhanVien){
                String row[] = {nv.getMaNV(), nv.getHoTen(), nv.getSoDienThoai(), nv.isGioiTinh() ? "NAM" : "NỮ", nv.getNgaySinh()+"", nv.getEmail(), nv.getDiaChi(), nv.getChucVu()+"", nv.getCaLamViec()+"", nv.getTenTaiKhoan(), nv.getMatKhau(), nv.getTinhTrang()+""};
                tmNhanVien.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        trangThaiNutThemNV = 0;
        trangThaiNutSuaNV = 0;
        trangThaiNutXoaNV = 0;
    }
    private List<NhanVien> dsLoc;
    private List<NhanVien> dsTam = new ArrayList<>();
    @Override
    public void actionPerformed(ActionEvent event) {
        Object op = event.getSource();
        /*NÚT THÊM*/
        if(op.equals(nhanVienUI.getButtonThemNhanVien())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ thêm nhân viên
            if (trangThaiNutThemNV==0) {
                nhanVienUI.getButtonThemNhanVien().setText("Xác nhận thêm");
                nhanVienUI.getButtonSuaNhanVien().setText("Xóa trắng");
                nhanVienUI.getButtonXoaNhanVien().setText("Thoát thêm");
                trangThaiNutXoaNV = 1;
                trangThaiNutThemNV = 1;
                trangThaiNutSuaNV = 1;

                //Mở tương tác với thông tin
                tuongTac(true);
                tuongTacTimKiem(false);

                //Xóa trắng dữ liệu
                xoaTrangAll();
                //load sẵn tình trang còn bán
                nhanVienUI.getTxtMaNhanVien().setText(laymaNV());
                nhanVienUI.getTxtMaNhanVien().setEnabled(false);
                nhanVienUI.getCbTinhTrangNhanVien().setSelectedItem("DANG_LAM");
            }
            // Thực hiện chức năng nghiệp vụ thêm nhân viên
            else if(trangThaiNutThemNV==1) {
                NhanVien nv = layDataThem();
                if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thêm nhân viên mới", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
                    try {
                        if(nhanVienImp.them(nv)){
                            loadDsNhanVien();
                            xoaTrangAll();
                            JOptionPane.showMessageDialog(null, "Thêm thành công!");
                            trangThaiNutThemNV = 1;
                            trangThaiNutXoaNV = 1;
                        }else{
                            JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
            //Thực hiện chức năng nghiệp vụ sửa nhân viên
            else if(trangThaiNutThemNV==2){
                if (nhanVienUI.getTxtMaNhanVien().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần sửa!");
                }else {
                    NhanVien nvSua = layDataSua();
                    if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn sửa nhân viên có mã " +nvSua.getMaNV()+" không?", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
                        try {
                            if(nhanVienImp.capNhat(nvSua)){
                                loadDsNhanVien();
                                xoaTrangAll();
                                JOptionPane.showMessageDialog(null, "Sửa thành công!");
                                trangThaiNutThemNV = 2;
                            }else{
                                JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                trangThaiNutXoaNV = 1;
            }
        }

        /*NÚT SỬA*/
        if(op.equals(nhanVienUI.getButtonSuaNhanVien())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ sửa nhân viên
            if (trangThaiNutSuaNV==0) {
                //Mở tương tác với thông tin
                tuongTac(true);

                nhanVienUI.getButtonThemNhanVien().setText("Xác nhận sửa");
                nhanVienUI.getButtonSuaNhanVien().setText("Xóa trắng");
                nhanVienUI.getButtonXoaNhanVien().setText("Thoát sửa");
                trangThaiNutThemNV = 2;
                trangThaiNutSuaNV = 1;
                trangThaiNutXoaNV = 1;
                nhanVienUI.getTxtMaNhanVien().setEnabled(false);
            }
            // Thực hiện xóa trắng dữ liệu ở nghiệp vụ sửa thông tín nhân viên
            else if(trangThaiNutSuaNV==1) {
                xoaTrangSua();
            }
        }

        /*NÚT XÓA*/
        if (op.equals(nhanVienUI.getButtonXoaNhanVien())) {
            // Thực hiện chức năng nghiệp vụ xóa nhân viên
            if (trangThaiNutXoaNV==0) {
                if (nhanVienUI.getTxtMatKhauNhanVien().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xóa!");
                } else {
                    String ma = nhanVienUI.getTxtMaNhanVien().getText();
                    if ((JOptionPane.showConfirmDialog(null,
                            "Bạn có chắc muốn ngừng bán nhân viên có mã " + nhanVienUI.getTxtMaNhanVien().getText() + " không?", "Lựa chọn",
                            JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION) {
                        try {
                            if (nhanVienImp.xoa(ma)){
                                loadDsNhanVien();
                                xoaTrangAll();
                                JOptionPane.showMessageDialog(null, "Xóa thành công!");
                            }else {
                                JOptionPane.showMessageDialog(null, "Xóa thất bại!");
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            // Thực hiện trả các nút về giao diện quản lý nhân viên
            else if(trangThaiNutXoaNV==1) {
                tuongTac(false);
                tuongTacTimKiem(true);
                xoaTrangAll();
                nhanVienUI.getButtonThemNhanVien().setText("Thêm nhân viên");
                nhanVienUI.getButtonSuaNhanVien().setText("Sửa nhân viên");
                nhanVienUI.getButtonXoaNhanVien().setText("Xóa nhân viên");
                trangThaiNutXoaNV = 0;
                trangThaiNutThemNV = 0;
                trangThaiNutSuaNV = 0;
            }
        }

        /*LỌC NHÂN VIÊN*/
        if(op.equals(nhanVienUI.getCbTkGioiTinh()) || op.equals(nhanVienUI.getCbTkChucVu()) || op.equals(nhanVienUI.getCbTkTinhTrang()) || op.equals(nhanVienUI.getCbTkCaLamViec())){
            if(!nhanVienUI.getCbTkGioiTinh().getSelectedItem().equals("--Giới tính--")){
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("gioiTinh", nhanVienUI.getCbTkGioiTinh().getSelectedItem().equals("NAM")? true : false);
                try {
                    dsLoc = nhanVienImp.timKiem(conditions);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(!nhanVienUI.getCbTkChucVu().getSelectedItem().equals("--Chức vụ--")){
                    for(int i=0; i<dsLoc.size(); i++){
                        if(nhanVienUI.getCbTkChucVu().getSelectedItem().equals(dsLoc.get(i).getChucVu().toString())){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                    if(!nhanVienUI.getCbTkCaLamViec().getSelectedItem().equals("--Ca làm việc--")){
                        for(int i=0; i<dsLoc.size(); i++){
                            if(nhanVienUI.getCbTkCaLamViec().getSelectedItem().equals(dsLoc.get(i).getCaLamViec().toString())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                        if(!nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                            for(int i=0; i<dsLoc.size(); i++){
                                if(nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                                    dsTam.add(dsLoc.get(i));
                                }
                            }
                            dsLoc = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if(!nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                        for(int i=0; i<dsLoc.size(); i++){
                            if(nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!nhanVienUI.getCbTkCaLamViec().getSelectedItem().equals("--Ca làm việc--")){
                    for(int i=0; i<dsLoc.size(); i++){
                        if(nhanVienUI.getCbTkCaLamViec().getSelectedItem().equals(dsLoc.get(i).getCaLamViec().toString())){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                    if(!nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                        for(int i=0; i<dsLoc.size(); i++){
                            if(nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                    for(int i=0; i<dsLoc.size(); i++){
                        if(nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            else if (!nhanVienUI.getCbTkChucVu().getSelectedItem().equals("--Chức vụ--")) {
                Map<String, Object> conditions = new HashMap<>();
                String chucVu = nhanVienUI.getCbTkChucVu().getSelectedItem().toString();
                if(chucVu.equals("QUAN_LY_NHAN_SU")){
                    conditions.put("chucVu", ChucVu.QUAN_LY_NHAN_SU);
                } else{
                    conditions.put("chucVu", ChucVu.NHAN_VIEN_BAN_HANG);
                }
                try {
                    dsLoc = nhanVienImp.timKiem(conditions);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(!nhanVienUI.getCbTkCaLamViec().getSelectedItem().equals("--Ca làm việc--")){
                    for(int i=0; i<dsLoc.size(); i++){
                        if(nhanVienUI.getCbTkCaLamViec().getSelectedItem().equals(dsLoc.get(i).getCaLamViec().toString())){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                    if(!nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                        for(int i=0; i<dsLoc.size(); i++){
                            if(nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                    for(int i=0; i<dsLoc.size(); i++){
                        if(nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            else if(!nhanVienUI.getCbTkCaLamViec().getSelectedItem().equals("--Ca làm việc--")){
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("CaLamViec", nhanVienUI.getCbTkCaLamViec().getSelectedItem().toString());
                try {
                    dsLoc = nhanVienImp.timKiem(conditions);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(!nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                    for(int i=0; i<dsLoc.size(); i++){
                        if(nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            else if(!nhanVienUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("TinhTrang", nhanVienUI.getCbTkTinhTrang().getSelectedItem().toString());
                try {
                    dsLoc = nhanVienImp.timKiem(conditions);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    dsLoc = nhanVienImp.timKiem();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            DefaultTableModel clearTable = (DefaultTableModel) nhanVienUI.getTbDanhSachNhanVien().getModel();
            clearTable.setRowCount(0);
            nhanVienUI.getTbDanhSachNhanVien().setModel(clearTable);
            try {
                DefaultTableModel tmNhanVien = (DefaultTableModel) nhanVienUI.getTbDanhSachNhanVien().getModel();
                for(NhanVien nv : dsLoc){
                    String row[] = {nv.getMaNV(), nv.getHoTen(), nv.getSoDienThoai(), nv.isGioiTinh() ? "NAM" : "NỮ", nv.getNgaySinh()+"", nv.getEmail(), nv.getDiaChi(), nv.getChucVu()+"", nv.getCaLamViec()+"", nv.getTenTaiKhoan(), nv.getMatKhau(), nv.getTinhTrang()+""};
                    tmNhanVien.addRow(row);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        /*NÚT LẤY ẢNH TỪ MÁY*/
        if(op.equals(nhanVienUI.getButtonThemHinhAnh())){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png"));
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // Lấy tên file và đuôi file
                fileAnhNV = selectedFile.getName();
                URL path = getClass().getResource("/img/imgSanPham/"+fileAnhNV);
                if(path==null){
                    path = getClass().getResource("/img/imgSanPham/Image_not_available.png");
                }

                ImageIcon iconGoc = new ImageIcon(path);
                Image anh = iconGoc.getImage();
                Image tinhChinhAnh = anh.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(tinhChinhAnh);
                JLabel picLabel = new JLabel();
                nhanVienUI.getPnImgNhanVien().removeAll();
                nhanVienUI.getPnImgNhanVien().add(picLabel);
                picLabel.setSize(new Dimension(240,320));
                picLabel.setIcon(icon);
            }
        }
    }

    //Hàm đóng/mở tương tác
    public void tuongTac(boolean c){
        nhanVienUI.getTxtMaNhanVien().setEnabled(c);
        nhanVienUI.getTxtSoDienThoaiNhanVien().setEnabled(c);
        nhanVienUI.getjDateChooserNgaySinhNhanVien().setEnabled(c);
        nhanVienUI.getTxtDiaChiNhanVien().setEnabled(c);
        nhanVienUI.getCbCaLamViecNhanVien().setEnabled(c);
        nhanVienUI.getCbTinhTrangNhanVien().setEnabled(c);
        nhanVienUI.getTxtHoTenNhanVien().setEnabled(c);
        nhanVienUI.getCbGioiTinh().setEnabled(c);
        nhanVienUI.getTxtEmail().setEnabled(c);
        nhanVienUI.getCbChucVu().setEnabled(c);
        nhanVienUI.getTxtTaiKhoanNhanVien().setEnabled(c);
        nhanVienUI.getTxtMatKhauNhanVien().setEnabled(c);
        nhanVienUI.getButtonThemHinhAnh().setEnabled(c);
    }
    //Hàm đóng/mở tương tác tìm kiếm
    public void tuongTacTimKiem(boolean o){
        nhanVienUI.getCbTkGioiTinh().setEnabled(o);
        nhanVienUI.getCbTkChucVu().setEnabled(o);
        nhanVienUI.getCbTkCaLamViec().setEnabled(o);
        nhanVienUI.getCbTkTinhTrang().setEnabled(o);
        nhanVienUI.getTxtTuKhoaTimKiem().setEnabled(o);
    }

    //Hàm xóa trắng sửa
    public void xoaTrangSua(){
        loadComboBoxPhanThongTinNV();
        nhanVienUI.getTxtSoDienThoaiNhanVien().setText("");
        nhanVienUI.getjDateChooserNgaySinhNhanVien().setDate(null);
        nhanVienUI.getTxtDiaChiNhanVien().setText("");
        nhanVienUI.getTxtHoTenNhanVien().setText("");
        nhanVienUI.getTxtEmail().setText("");
        nhanVienUI.getTxtTaiKhoanNhanVien().setText("");
        nhanVienUI.getTxtMatKhauNhanVien().setText("");
    }

    //Hàm xóa trắng dữ liệu nhập
    public void xoaTrangAll(){
        loadComboBoxPhanThongTinNV();
        nhanVienUI.getTxtMaNhanVien().setText("");
        nhanVienUI.getTxtSoDienThoaiNhanVien().setText("");
        nhanVienUI.getjDateChooserNgaySinhNhanVien().setDate(null);
        nhanVienUI.getTxtDiaChiNhanVien().setText("");
        nhanVienUI.getTxtHoTenNhanVien().setText("");
        nhanVienUI.getTxtEmail().setText("");
        nhanVienUI.getTxtTaiKhoanNhanVien().setText("");
        nhanVienUI.getTxtMatKhauNhanVien().setText("");
    }

    //Load các comboBox phần thông tin
    public void loadComboBoxPhanThongTinNV(){
        //Lấy ca làm việc
        CaLamViec[] dsCaLamViec = CaLamViec.values();
        String[] itemsCaLamViec = new String[dsCaLamViec.length + 1];
        itemsCaLamViec[0] = "--Select--";
        for (int i = 0; i < dsCaLamViec.length; i++) {
            itemsCaLamViec[i + 1] = dsCaLamViec[i].toString();
        }
        DefaultComboBoxModel<String> caLamViecCb = new DefaultComboBoxModel<>(itemsCaLamViec);
        nhanVienUI.getCbCaLamViecNhanVien().setModel(caLamViecCb);

        //Lấy tình trạng từ enum
        TinhTrangNhanVien[] dsTinhTrang = TinhTrangNhanVien.values();
        String[] itemsTinhTrang = new String[dsTinhTrang.length + 1];
        itemsTinhTrang[0] = "--Select--";
        for (int i = 0; i < dsTinhTrang.length; i++) {
            itemsTinhTrang[i + 1] = dsTinhTrang[i].toString();
        }
        DefaultComboBoxModel<String> tinhTrangCb = new DefaultComboBoxModel<>(itemsTinhTrang);
        nhanVienUI.getCbTinhTrangNhanVien().setModel(tinhTrangCb);

        //Giới tính
        String[] gioiTinh = {"--Select--", "NAM", "NỮ"};
        DefaultComboBoxModel<String> gioiTinhCb = new DefaultComboBoxModel<>(gioiTinh);
        nhanVienUI.getCbGioiTinh().setModel(gioiTinhCb);

        //Lấy tình
        ChucVu[] dsChucVu = ChucVu.values();
        String[] itemsPhongCachMac = new String[dsChucVu.length + 1];
        itemsPhongCachMac[0] = "--Select--";
        for (int i = 0; i < dsChucVu.length; i++) {
            itemsPhongCachMac[i + 1] = dsChucVu[i].toString();
        }
        DefaultComboBoxModel<String> PhongCachMacCb = new DefaultComboBoxModel<>(itemsPhongCachMac);
        nhanVienUI.getCbChucVu().setModel(PhongCachMacCb);
    }

    //Load các comboBox phần tìm kiếm
    public void loadComboBoxPhanTimKiem(){
        //Lấy ca làm việc
        CaLamViec[] dsCaLamViec = CaLamViec.values();
        String[] itemsCaLamViec = new String[dsCaLamViec.length + 1];
        itemsCaLamViec[0] = "--Ca làm việc--";
        for (int i = 0; i < dsCaLamViec.length; i++) {
            itemsCaLamViec[i + 1] = dsCaLamViec[i].toString();
        }
        DefaultComboBoxModel<String> caLamViecCb = new DefaultComboBoxModel<>(itemsCaLamViec);
        nhanVienUI.getCbTkCaLamViec().setModel(caLamViecCb);

        //Lấy tình trạng từ enum
        TinhTrangNhanVien[] dsTinhTrang = TinhTrangNhanVien.values();
        String[] itemsTinhTrang = new String[dsTinhTrang.length + 1];
        itemsTinhTrang[0] = "--Tình trạng--";
        for (int i = 0; i < dsTinhTrang.length; i++) {
            itemsTinhTrang[i + 1] = dsTinhTrang[i].toString();
        }
        DefaultComboBoxModel<String> tinhTrangCb = new DefaultComboBoxModel<>(itemsTinhTrang);
        nhanVienUI.getCbTkTinhTrang().setModel(tinhTrangCb);

        //Giới tính
        String[] gioiTinh = {"--Giới tính--", "NAM", "NỮ"};
        DefaultComboBoxModel<String> gioiTinhCb = new DefaultComboBoxModel<>(gioiTinh);
        nhanVienUI.getCbTkGioiTinh().setModel(gioiTinhCb);

        //Lấy chức vụ
        ChucVu[] dsChucVu = ChucVu.values();
        String[] itemsChucVu = new String[dsChucVu.length + 1];
        itemsChucVu[0] = "--Chức vụ--";
        for (int i = 0; i < dsChucVu.length; i++) {
            itemsChucVu[i + 1] = dsChucVu[i].toString();
        }
        DefaultComboBoxModel<String> ChucVuCb = new DefaultComboBoxModel<>(itemsChucVu);
        nhanVienUI.getCbTkChucVu().setModel(ChucVuCb);
    }
    
    //Hàm lấy nhân viên từ phần thông tin
    private NhanVien layDataThem() {
        NhanVien nv;
        String sdt = nhanVienUI.getTxtSoDienThoaiNhanVien().getText();

        // Lấy ngày từ JDateChooser
        Date selectedDate = nhanVienUI.getjDateChooserNgaySinhNhanVien().getDate();
        // Chuyển đổi từ Date sang LocalDate
        LocalDate localDate = dateToLocalDate(selectedDate);

        String dc = nhanVienUI.getTxtDiaChiNhanVien().getText();
        CaLamViec caLamViec = CaLamViec.layGiaTri(nhanVienUI.getCbCaLamViecNhanVien().getSelectedItem().toString());
        TinhTrangNhanVien tinhTrang = TinhTrangNhanVien.layGiaTri(nhanVienUI.getCbTinhTrangNhanVien().getSelectedItem().toString());
        String ten = nhanVienUI.getTxtHoTenNhanVien().getText();
        Boolean gt;
        if(nhanVienUI.getCbGioiTinh().getSelectedItem().toString().equals("NAM")){
            gt = true;
        }else {
            gt = false;
        }
        String email = nhanVienUI.getTxtEmail().getText();
        ChucVu cv = ChucVu.layGiaTri(nhanVienUI.getCbChucVu().getSelectedItem().toString());
        String tk = nhanVienUI.getTxtTaiKhoanNhanVien().getText();
        String mk = nhanVienUI.getTxtMatKhauNhanVien().getText();

        //Hình ảnh
        String anh = fileAnhNV;
        //Mã nhân viên
        String ma = nhanVienUI.getTxtMaNhanVien().getText();
        try {
            nv = new NhanVien(ma,ten,sdt,gt,localDate,email,dc,cv,caLamViec,tk,mk,anh,tinhTrang);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nv;
    }

    //Hàm lấy nhân viên để update
    private NhanVien layDataSua() {
        NhanVien nv;
        String sdt = nhanVienUI.getTxtSoDienThoaiNhanVien().getText();

        // Lấy ngày từ JDateChooser
        Date selectedDate = nhanVienUI.getjDateChooserNgaySinhNhanVien().getDate();
        // Chuyển đổi từ Date sang LocalDate
        LocalDate localDate1 = dateToLocalDate(selectedDate);

        String dc = nhanVienUI.getTxtDiaChiNhanVien().getText();
        CaLamViec caLamViec = CaLamViec.layGiaTri(nhanVienUI.getCbCaLamViecNhanVien().getSelectedItem().toString());
        TinhTrangNhanVien tinhTrangNv = TinhTrangNhanVien.layGiaTri(nhanVienUI.getCbTinhTrangNhanVien().getSelectedItem().toString());
        String ten = nhanVienUI.getTxtHoTenNhanVien().getText();
        Boolean gt;
        if(nhanVienUI.getCbGioiTinh().getSelectedItem().toString().equals("NAM")){
            gt = true;
        }else {
            gt = false;
        }
        String email = nhanVienUI.getTxtEmail().getText();
        ChucVu cv = ChucVu.layGiaTri(nhanVienUI.getCbChucVu().getSelectedItem().toString());
        String tk = nhanVienUI.getTxtTaiKhoanNhanVien().getText();
        String mk = nhanVienUI.getTxtMatKhauNhanVien().getText();

        //Hình ảnh
        String anh = fileAnhNV;
        //Mã nhân viên
        String ma = nhanVienUI.getTxtMaNhanVien().getText();
        try {
            nv = new NhanVien(ma,ten,sdt,gt,localDate1,email,dc,cv,caLamViec,tk,mk,anh,tinhTrangNv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nv;
    }

    //Hàm sinh mã nhân viên
    private String laymaNV() {
        String ma = "NV-";
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        ma = ma+nThem;
        ma = ma+"-"+formatNumber(laysoDuoiMaNV());
        return ma;
    }
    //Hàm lấy số đuôi
    public int laysoDuoiMaNV(){
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("MaNV", "%"+nThem+"%");
        List<NhanVien> nhanViens;
        try {
            nhanViens = nhanVienImp.timKiem(conditions);
        } catch (Exception e) {
            return 1;
        }
        if(nhanViens.size()==0){
            return 1;
        }
        NhanVien nv = nhanViens.get(nhanViens.size()-1);
        int soHD = Integer.parseInt(nv.getMaNV().substring(nv.getMaNV().length()-3));
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
    public  String getInitials(String input) {
        String[] words = input.split("\\s+"); // Sử dụng "\\s+" để tách các từ dựa trên khoảng trắng

        // Lấy chữ cái đầu của từng từ ghép
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }

        return initials.toString();
    }


    //Hàm đổi Date thành LocalDate
    public LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }
    @Override
    public void mouseClicked(MouseEvent event) {
        if(trangThaiNutThemNV==1){
            JOptionPane.showMessageDialog(null, "Đang thực hiện chức năng thêm, không được click!!");
        }else {
            int row = nhanVienUI.getTbDanhSachNhanVien().getSelectedRow();
            String ma = nhanVienUI.getTbDanhSachNhanVien().getValueAt(row, 0).toString();
            NhanVien nvHienThuc = null;
            try {
                nvHienThuc = nhanVienImp.timKiem(ma);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            nhanVienUI.getTxtMaNhanVien().setText(nvHienThuc.getMaNV());
            nhanVienUI.getTxtHoTenNhanVien().setText(nvHienThuc.getHoTen());
            nhanVienUI.getTxtSoDienThoaiNhanVien().setText(nvHienThuc.getSoDienThoai());
            nhanVienUI.getTxtDiaChiNhanVien().setText(nvHienThuc.getDiaChi());
            nhanVienUI.getTxtEmail().setText(nvHienThuc.getEmail());
            nhanVienUI.getTxtTaiKhoanNhanVien().setText(nvHienThuc.getTenTaiKhoan());
            nhanVienUI.getTxtMatKhauNhanVien().setText(nvHienThuc.getMatKhau());

            //Xử lý ngày
            String date = String.valueOf(nvHienThuc.getNgaySinh());
            Date date2 = null;
            try {
                date2 = new SimpleDateFormat("yyyy-mm-dd").parse(date);
            } catch (ParseException e) {throw new RuntimeException(e);

            }
            nhanVienUI.getjDateChooserNgaySinhNhanVien().setDate(date2);

            nhanVienUI.getCbCaLamViecNhanVien().setSelectedItem(nvHienThuc.getCaLamViec().toString());
            nhanVienUI.getCbTinhTrangNhanVien().setSelectedItem(nvHienThuc.getTinhTrang().toString());
            nhanVienUI.getCbGioiTinh().setSelectedItem(nvHienThuc.isGioiTinh() ? "NAM" : "NỮ");
            nhanVienUI.getCbChucVu().setSelectedItem(nvHienThuc.getChucVu().toString());

            URL path = getClass().getResource("/img/imgSanPham/Image_not_available.png");
            if(path==null){
                path = getClass().getResource("/img/imgSanPham/Image_not_available.png");
            }

            ImageIcon iconGoc = new ImageIcon(path);
            Image anh = iconGoc.getImage();
            Image tinhChinhAnh = anh.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(tinhChinhAnh);
            JLabel picLabel = new JLabel();
            nhanVienUI.getPnImgNhanVien().removeAll();
            nhanVienUI.getPnImgNhanVien().add(picLabel);
            picLabel.setSize(new Dimension(240,320));
            picLabel.setIcon(icon);
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
        if(!nhanVienUI.getTxtTuKhoaTimKiem().getText().equals("")){
            Map<String, Object> conditions = new HashMap<>();
            conditions.put("CONCAT(MaNV, HoTen, SoDienThoai, GioiTinh, NgaySinh, Email, DiaChi, ChucVu, CaLamViec, TenTaiKhoan, MatKhau, TinhTrang)", nhanVienUI.getTxtTuKhoaTimKiem().getText());
            String[] col = {"MaNV", "HoTen", "SoDienThoai", "GioiTinh", "NgaySinh", "Email", "DiaChi", "ChucVu", "CaLamViec", "TenTaiKhoan", "MatKhau", "TinhTrang"};
            List<Map<String, Object>> nv;
            try {
                nv = nhanVienImp.timKiem(conditions, false, col);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            DefaultTableModel clearTable = (DefaultTableModel) nhanVienUI.getTbDanhSachNhanVien().getModel();
            clearTable.setRowCount(0);
            nhanVienUI.getTbDanhSachNhanVien().setModel(clearTable);
            try {
                DefaultTableModel tmKhachHang = (DefaultTableModel) nhanVienUI.getTbDanhSachNhanVien().getModel();
                for(int i=0; i<nv.size(); i++){
                    String row[] = {nv.get(i).get("MaNV")+"", nv.get(i).get("HoTen")+"",  nv.get(i).get("SoDienThoai")+"",
                            (nv.get(i).get("GioiTinh").equals("0") ? "NỮ" : "NAM"), (nv.get(i).get("NgaySinh")+"").substring(0, 10), nv.get(i).get("Email")+"",
                            nv.get(i).get("DiaChi")+"", nv.get(i).get("ChucVu")+"", (nv.get(i).get("CaLamViec").equals("1") ? "CA_1" : "CA_2")+"", nv.get(i).get("TenTaiKhoan")+"",
                            nv.get(i).get("MatKhau")+"", nv.get(i).get("TinhTrang")+""};
                    tmKhachHang.addRow(row);
                }
            } catch (Exception o) {
                throw new RuntimeException(o);
            }
        }else {
            System.out.println(1);
            DefaultTableModel clearTable = (DefaultTableModel) nhanVienUI.getTbDanhSachNhanVien().getModel();
            clearTable.setRowCount(0);
            nhanVienUI.getTbDanhSachNhanVien().setModel(clearTable);
            try {
                dsNhanVien = nhanVienImp.timKiem();
                DefaultTableModel tmNhanVien = (DefaultTableModel) nhanVienUI.getTbDanhSachNhanVien().getModel();
                for(NhanVien nv : dsNhanVien){
                    String row[] = {nv.getMaNV(), nv.getHoTen(), nv.getSoDienThoai(), nv.isGioiTinh() ? "NAM" : "NỮ", nv.getNgaySinh()+"", nv.getEmail(), nv.getDiaChi(), nv.getChucVu()+"", nv.getCaLamViec()+"", nv.getTenTaiKhoan(), nv.getMatKhau(), nv.getTinhTrang()+""};
                    tmNhanVien.addRow(row);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
