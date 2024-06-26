/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package dev.skyherobrine.app.views.dashboard.component;

import com.toedter.calendar.JDateChooser;
import dev.skyherobrine.app.controllers.dashboardui.person.NhanVienController;


import javax.swing.*;

/**
 *
 * @author Virtue Nguyen
 */
public class FrmNhanVien extends javax.swing.JPanel {

    /**
     * Creates new form ThongTinCaNhan
     */
    public FrmNhanVien() {
        initComponents();
        NhanVienController nhanVienController = new NhanVienController(this);
        nhanVienController.loadDsNhanVien();
        nhanVienController.loadComboBoxPhanThongTinNV();
        nhanVienController.loadComboBoxPhanTimKiem();
        tbDanhSachNhanVien.addMouseListener(nhanVienController);
        btnThemNhanVien.addActionListener(nhanVienController);
        btnSuaNhanVien.addActionListener(nhanVienController);
        btnXoaNhanVien.addActionListener(nhanVienController);

        cbTkGioiTinhNv.addActionListener(nhanVienController);
        cbTkCaLamViecNv.addActionListener(nhanVienController);
        cbTkChucVuNv.addActionListener(nhanVienController);
        cbTkTinhTrangNv.addActionListener(nhanVienController);

        txtTuKhoaTimKiem.addKeyListener(nhanVienController);
        btnThemAnhNhanVien.addActionListener(nhanVienController);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnBGThongTinCaNhan = new javax.swing.JPanel();
        pnTHongTinNhanVien = new javax.swing.JPanel();
        lbMaNhanVien = new javax.swing.JLabel();
        txtMaNhanVien = new javax.swing.JTextField();
        lbHoTenNhanVien = new javax.swing.JLabel();
        txtHoTenNhanVien = new javax.swing.JTextField();
        lbGIoiTinhNhanVien = new javax.swing.JLabel();
        lbSoDienThoaiNhanVien = new javax.swing.JLabel();
        txtSoDienThoaiNhanVien = new javax.swing.JTextField();
        lbEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lbChucVu = new javax.swing.JLabel();
        lbNgaySinhNhanVien = new javax.swing.JLabel();
        lbDiaChiNhanVien = new javax.swing.JLabel();
        txtDiaChiNhanVien = new javax.swing.JTextField();
        lbTinhTrangNhanVien = new javax.swing.JLabel();
        lbCaLamViecNhanVien = new javax.swing.JLabel();
        txtTaiKhoanNhanVien = new javax.swing.JTextField();
        lbTaiKhoaNhanVien = new javax.swing.JLabel();
        txtMatKhauNhanVien = new javax.swing.JTextField();
        lbTinhTrangQuanLySanPham1 = new javax.swing.JLabel();
        pnImgNhanVien = new javax.swing.JPanel();
        btnSuaNhanVien = new javax.swing.JButton();
        btnThemNhanVien = new javax.swing.JButton();
        btnXoaNhanVien = new javax.swing.JButton();
        btnThemAnhNhanVien = new javax.swing.JButton();
        cbCaLmViec = new javax.swing.JComboBox<>();
        cbTinhTrang = new javax.swing.JComboBox<>();
        cbGioiTinh = new javax.swing.JComboBox<>();
        cbChucVu = new javax.swing.JComboBox<>();
        jDateChooserNgaySinh = new com.toedter.calendar.JDateChooser();
        pnDanhSachNhanVien = new javax.swing.JPanel();
        spDanhSachNhanVien = new javax.swing.JScrollPane();
        tbDanhSachNhanVien = new javax.swing.JTable();
        pnTimKiemNhanVien = new javax.swing.JPanel();
        cbTkTinhTrangNv = new javax.swing.JComboBox<>();
        cbTkGioiTinhNv = new javax.swing.JComboBox<>();
        cbTkChucVuNv = new javax.swing.JComboBox<>();
        cbTkCaLamViecNv = new javax.swing.JComboBox<>();
        lbTkTenNhanVien = new javax.swing.JLabel();
        txtTuKhoaTimKiem = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1651, 1000));

        pnBGThongTinCaNhan.setBackground(new java.awt.Color(255, 255, 255));
        pnBGThongTinCaNhan.setPreferredSize(new java.awt.Dimension(1651, 1000));

        pnTHongTinNhanVien.setBackground(new java.awt.Color(255, 255, 255));
        pnTHongTinNhanVien.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin nhân viên", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 24))); // NOI18N
        pnTHongTinNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        lbMaNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbMaNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbMaNhanVien.setText("Mã nhân viên:");

        txtMaNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaNhanVien.setEnabled(false);
        txtMaNhanVien.setPreferredSize(new java.awt.Dimension(64, 25));

        lbHoTenNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbHoTenNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbHoTenNhanVien.setText("Họ tên:");

        txtHoTenNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtHoTenNhanVien.setEnabled(false);
        txtHoTenNhanVien.setPreferredSize(new java.awt.Dimension(64, 25));

        lbGIoiTinhNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbGIoiTinhNhanVien.setText("Giới tính:");

        lbSoDienThoaiNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbSoDienThoaiNhanVien.setText("Số điện thoại:");

        txtSoDienThoaiNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoDienThoaiNhanVien.setEnabled(false);
        txtSoDienThoaiNhanVien.setPreferredSize(new java.awt.Dimension(79, 33));

        lbEmail.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbEmail.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmail.setEnabled(false);

        lbChucVu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbChucVu.setText("Chức vụ:");

        lbNgaySinhNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbNgaySinhNhanVien.setText("Ngày Sinh:");

        lbDiaChiNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbDiaChiNhanVien.setText("Địa chỉ:");

        txtDiaChiNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDiaChiNhanVien.setEnabled(false);

        lbTinhTrangNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbTinhTrangNhanVien.setText("Tình trạng:");

        lbCaLamViecNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbCaLamViecNhanVien.setText("Ca làm việc:");

        txtTaiKhoanNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTaiKhoanNhanVien.setEnabled(false);

        lbTaiKhoaNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbTaiKhoaNhanVien.setText("Tài khoản:");

        txtMatKhauNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMatKhauNhanVien.setEnabled(false);

        lbTinhTrangQuanLySanPham1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbTinhTrangQuanLySanPham1.setText("Mật Khẩu:");

        pnImgNhanVien.setBackground(new java.awt.Color(255, 51, 0));
        pnImgNhanVien.setPreferredSize(new java.awt.Dimension(240, 320));

        javax.swing.GroupLayout pnImgNhanVienLayout = new javax.swing.GroupLayout(pnImgNhanVien);
        pnImgNhanVien.setLayout(pnImgNhanVienLayout);
        pnImgNhanVienLayout.setHorizontalGroup(
            pnImgNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 195, Short.MAX_VALUE)
        );
        pnImgNhanVienLayout.setVerticalGroup(
            pnImgNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        btnSuaNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSuaNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconButtonChungChoCacForm/edit.png"))); // NOI18N
        btnSuaNhanVien.setText("Sửa nhân viên");

        btnThemNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnThemNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconButtonChungChoCacForm/plus.png"))); // NOI18N
        btnThemNhanVien.setText("Thêm nhân viên");

        btnXoaNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnXoaNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconButtonChungChoCacForm/remove.png"))); // NOI18N
        btnXoaNhanVien.setText("Xoá nhân viên");

        btnThemAnhNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThemAnhNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iconButtonChungChoCacForm/plus.png"))); // NOI18N
        btnThemAnhNhanVien.setText("Thêm ảnh");

        cbCaLmViec.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbTinhTrang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnTHongTinNhanVienLayout = new javax.swing.GroupLayout(pnTHongTinNhanVien);
        pnTHongTinNhanVien.setLayout(pnTHongTinNhanVienLayout);
        pnTHongTinNhanVienLayout.setHorizontalGroup(
            pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                        .addComponent(lbMaNhanVien)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lbDiaChiNhanVien)
                        .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                            .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                                    .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbCaLamViecNhanVien)
                                        .addComponent(lbNgaySinhNhanVien)
                                        .addComponent(lbTinhTrangNhanVien))
                                    .addGap(35, 35, 35))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTHongTinNhanVienLayout.createSequentialGroup()
                                    .addComponent(lbSoDienThoaiNhanVien)
                                    .addGap(18, 18, 18)))
                            .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtSoDienThoaiNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                .addComponent(txtDiaChiNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                .addComponent(cbCaLmViec, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbTinhTrang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDateChooserNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                        .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbTinhTrangQuanLySanPham1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lbTaiKhoaNhanVien, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lbChucVu))
                            .addComponent(lbEmail)
                            .addComponent(lbGIoiTinhNhanVien))
                        .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cbGioiTinh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                    .addComponent(txtTaiKhoanNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                    .addComponent(txtMatKhauNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)))
                            .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cbChucVu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                        .addComponent(lbHoTenNhanVien)
                        .addGap(44, 44, 44)
                        .addComponent(txtHoTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTHongTinNhanVienLayout.createSequentialGroup()
                        .addComponent(pnImgNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(133, 133, 133))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTHongTinNhanVienLayout.createSequentialGroup()
                        .addComponent(btnThemAnhNhanVien)
                        .addGap(173, 173, 173))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTHongTinNhanVienLayout.createSequentialGroup()
                .addGap(279, 279, 279)
                .addComponent(btnThemNhanVien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSuaNhanVien)
                .addGap(250, 250, 250)
                .addComponent(btnXoaNhanVien)
                .addGap(285, 285, 285))
        );
        pnTHongTinNhanVienLayout.setVerticalGroup(
            pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTHongTinNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHoTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbHoTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbGIoiTinhNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoDienThoaiNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbSoDienThoaiNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbNgaySinhNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbEmail))
                            .addComponent(jDateChooserNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDiaChiNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbDiaChiNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbCaLamViecNhanVien)
                            .addComponent(txtTaiKhoanNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbCaLmViec, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbTaiKhoaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbTinhTrangNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMatKhauNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbTinhTrangQuanLySanPham1)
                            .addComponent(cbTinhTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnTHongTinNhanVienLayout.createSequentialGroup()
                        .addComponent(pnImgNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThemAnhNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(27, 27, 27)
                .addGroup(pnTHongTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnDanhSachNhanVien.setBackground(new java.awt.Color(255, 255, 255));
        pnDanhSachNhanVien.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh Sách Nhân Viên", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 24))); // NOI18N

        tbDanhSachNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Họ tên", "Số điện thoại", "Giới tính", "Ngày sinh", "Email", "Địa chỉ", "Chức vụ", "Ca làm việc", "Tài khoản", "Mật khẩu", "Tình trạng"
            }
        ));
        spDanhSachNhanVien.setViewportView(tbDanhSachNhanVien);

        javax.swing.GroupLayout pnDanhSachNhanVienLayout = new javax.swing.GroupLayout(pnDanhSachNhanVien);
        pnDanhSachNhanVien.setLayout(pnDanhSachNhanVienLayout);
        pnDanhSachNhanVienLayout.setHorizontalGroup(
            pnDanhSachNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spDanhSachNhanVien)
        );
        pnDanhSachNhanVienLayout.setVerticalGroup(
            pnDanhSachNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spDanhSachNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
        );

        pnTimKiemNhanVien.setBackground(new java.awt.Color(255, 255, 255));
        pnTimKiemNhanVien.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm Kiếm nhân viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18))); // NOI18N

        cbTkTinhTrangNv.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cbTkTinhTrangNv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tình trạng", " " }));

        cbTkGioiTinhNv.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cbTkGioiTinhNv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Giới tính", "Nam", "Nữ", "Gay", "Les", " " }));

        cbTkChucVuNv.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cbTkChucVuNv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chức vụ", " ", " " }));

        cbTkCaLamViecNv.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cbTkCaLamViecNv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ca làm việc", "Sáng", "Chiều", "Đêm", " " }));

        lbTkTenNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbTkTenNhanVien.setText("Từ khoá tìm kiếm:");

        txtTuKhoaTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout pnTimKiemNhanVienLayout = new javax.swing.GroupLayout(pnTimKiemNhanVien);
        pnTimKiemNhanVien.setLayout(pnTimKiemNhanVienLayout);
        pnTimKiemNhanVienLayout.setHorizontalGroup(
            pnTimKiemNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTimKiemNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbTkGioiTinhNv, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117)
                .addComponent(cbTkChucVuNv, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(149, 149, 149)
                .addComponent(cbTkCaLamViecNv, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(172, 172, 172)
                .addComponent(cbTkTinhTrangNv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127)
                .addComponent(lbTkTenNhanVien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTuKhoaTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
        );
        pnTimKiemNhanVienLayout.setVerticalGroup(
            pnTimKiemNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTimKiemNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnTimKiemNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbTkGioiTinhNv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTkChucVuNv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTkCaLamViecNv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTkTinhTrangNv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTkTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTuKhoaTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnBGThongTinCaNhanLayout = new javax.swing.GroupLayout(pnBGThongTinCaNhan);
        pnBGThongTinCaNhan.setLayout(pnBGThongTinCaNhanLayout);
        pnBGThongTinCaNhanLayout.setHorizontalGroup(
            pnBGThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTHongTinNhanVien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnDanhSachNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnTimKiemNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnBGThongTinCaNhanLayout.setVerticalGroup(
            pnBGThongTinCaNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBGThongTinCaNhanLayout.createSequentialGroup()
                .addComponent(pnDanhSachNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnTimKiemNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnTHongTinNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnBGThongTinCaNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnBGThongTinCaNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSuaNhanVien;
    private javax.swing.JButton btnThemAnhNhanVien;
    private javax.swing.JButton btnThemNhanVien;
    private javax.swing.JButton btnXoaNhanVien;
    private javax.swing.JComboBox<String> cbCaLmViec;
    private javax.swing.JComboBox<String> cbChucVu;
    private javax.swing.JComboBox<String> cbGioiTinh;
    private javax.swing.JComboBox<String> cbTinhTrang;
    private javax.swing.JComboBox<String> cbTkCaLamViecNv;
    private javax.swing.JComboBox<String> cbTkChucVuNv;
    private javax.swing.JComboBox<String> cbTkGioiTinhNv;
    private javax.swing.JComboBox<String> cbTkTinhTrangNv;
    private com.toedter.calendar.JDateChooser jDateChooserNgaySinh;
    private javax.swing.JLabel lbCaLamViecNhanVien;
    private javax.swing.JLabel lbChucVu;
    private javax.swing.JLabel lbDiaChiNhanVien;
    private javax.swing.JLabel lbEmail;
    private javax.swing.JLabel lbGIoiTinhNhanVien;
    private javax.swing.JLabel lbHoTenNhanVien;
    private javax.swing.JLabel lbMaNhanVien;
    private javax.swing.JLabel lbNgaySinhNhanVien;
    private javax.swing.JLabel lbSoDienThoaiNhanVien;
    private javax.swing.JLabel lbTaiKhoaNhanVien;
    private javax.swing.JLabel lbTinhTrangNhanVien;
    private javax.swing.JLabel lbTinhTrangQuanLySanPham1;
    private javax.swing.JLabel lbTkTenNhanVien;
    private javax.swing.JPanel pnBGThongTinCaNhan;
    private javax.swing.JPanel pnDanhSachNhanVien;
    private javax.swing.JPanel pnImgNhanVien;
    private javax.swing.JPanel pnTHongTinNhanVien;
    private javax.swing.JPanel pnTimKiemNhanVien;
    private javax.swing.JScrollPane spDanhSachNhanVien;
    private javax.swing.JTable tbDanhSachNhanVien;
    private javax.swing.JTextField txtDiaChiNhanVien;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHoTenNhanVien;
    private javax.swing.JTextField txtMaNhanVien;
    private javax.swing.JTextField txtMatKhauNhanVien;
    private javax.swing.JTextField txtSoDienThoaiNhanVien;
    private javax.swing.JTextField txtTaiKhoanNhanVien;
    private javax.swing.JTextField txtTuKhoaTimKiem;
    // End of variables declaration//GEN-END:variables


    public JTable getTbDanhSachNhanVien() {
        return tbDanhSachNhanVien;
    }
    public JButton getButtonThemNhanVien(){return btnThemNhanVien;}
    public JButton getButtonSuaNhanVien(){return btnSuaNhanVien;}
    public JButton getButtonXoaNhanVien(){return btnXoaNhanVien;}
    public JTextField getTxtMaNhanVien(){return txtMaNhanVien;}
    public JTextField getTxtSoDienThoaiNhanVien(){return txtSoDienThoaiNhanVien;}
    public JDateChooser getjDateChooserNgaySinhNhanVien(){return jDateChooserNgaySinh;}
    public JTextField getTxtDiaChiNhanVien(){return  txtDiaChiNhanVien;}
    public JComboBox getCbCaLamViecNhanVien(){return  cbCaLmViec;}
    public JComboBox getCbTinhTrangNhanVien(){return cbTinhTrang;}
    public JTextField getTxtHoTenNhanVien(){return txtHoTenNhanVien;}
    public JComboBox getCbGioiTinh(){return  cbGioiTinh;}
    public JTextField getTxtEmail(){return  txtEmail;}
    public JComboBox getCbChucVu(){return cbChucVu;}
    public JTextField getTxtTaiKhoanNhanVien(){return txtTaiKhoanNhanVien;}
    public JTextField getTxtMatKhauNhanVien(){return txtMatKhauNhanVien;}
    public JButton getButtonThemHinhAnh(){return btnThemAnhNhanVien;}
    public JPanel getPnImgNhanVien(){return pnImgNhanVien;}
    public JComboBox getCbTkGioiTinh(){return cbTkGioiTinhNv;}
    public JComboBox getCbTkChucVu(){return cbTkChucVuNv;}
    public JComboBox getCbTkCaLamViec(){return cbTkCaLamViecNv;}
    public JComboBox getCbTkTinhTrang(){return cbTkTinhTrangNv;}
    public JTextField getTxtTuKhoaTimKiem(){return txtTuKhoaTimKiem;}
}
