package dev.skyherobrine.app.controllers.dashboardui.person;

import dev.skyherobrine.app.controllers.loginui.mainLogin.LoginController;
import dev.skyherobrine.app.daos.person.NhanVienDAO;
import dev.skyherobrine.app.entities.person.NhanVien;
import dev.skyherobrine.app.enums.CaLamViec;
import dev.skyherobrine.app.enums.ChucVu;
import dev.skyherobrine.app.enums.DoTuoi;
import dev.skyherobrine.app.enums.TinhTrangNhanVien;
import dev.skyherobrine.app.views.dashboard.component.FormTHongTinCaNhan;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongTinCaNhanController implements ActionListener {
    private FormTHongTinCaNhan formTHongTinCaNhan;
    private NhanVienDAO nhanVienDAO;
    private String fileAnh = "";
    private int trangThaiChinhSua = 0;
    public ThongTinCaNhanController(FormTHongTinCaNhan formTHongTinCaNhan) {
        this.formTHongTinCaNhan = formTHongTinCaNhan;
        try {
            this.nhanVienDAO = new NhanVienDAO();
            loadComboboxTTNV();
            loadTTNV();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadTTNV(){
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("TenTaiKhoan", LoginController.luuTkNhanVien());
        try {
            tuongTac(false);
            List<NhanVien> nhanViens = nhanVienDAO.timKiem(conditions);
            NhanVien nhanVien = nhanViens.get(0);
            formTHongTinCaNhan.getTxtHoVaTen().setText(nhanVien.getHoTen());
            String date = String.valueOf(nhanVien.getNgaySinh());
            Date ngayHenGiao = null;
            try {
                ngayHenGiao = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            formTHongTinCaNhan.getjDateChooserNgaySinh().setDate(ngayHenGiao);
            formTHongTinCaNhan.getTxtDiaChi().setText(nhanVien.getDiaChi());
            formTHongTinCaNhan.getTxtSoDienThoai().setText(nhanVien.getSoDienThoai());
            formTHongTinCaNhan.getTxtEmail().setText(nhanVien.getEmail());
            formTHongTinCaNhan.getTxtTenTaiKhoan().setText(nhanVien.getTenTaiKhoan());
            formTHongTinCaNhan.getTxtMatKhau().setText(nhanVien.getMatKhau());
//            formTHongTinCaNhan
            importAnh(nhanVien.getHinhAnh());
            if (nhanVien.isGioiTinh()) {
                formTHongTinCaNhan.getCbGioiTinh().setSelectedIndex(0);
            } else {
                formTHongTinCaNhan.getCbGioiTinh().setSelectedIndex(1);
            }
            formTHongTinCaNhan.getTxtMaNv().setText(nhanVien.getMaNV());
            formTHongTinCaNhan.getCbChucVu().setSelectedItem(ChucVu.layGiaTri(nhanVien.getChucVu().toString()).toString());
            formTHongTinCaNhan.getCbCaLamViec().setSelectedItem(CaLamViec.layGiaTri(nhanVien.getCaLamViec().toString()).toString());
            formTHongTinCaNhan.getCbTinhTrang().setSelectedItem(TinhTrangNhanVien.layGiaTri(nhanVien.getTinhTrang().toString()).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void importAnh(String url){
        URL path = getClass().getResource("/img/imgSanPham/"+url);
        if(path==null){
            path = getClass().getResource("/img/imgSanPham/Image_not_available.png");
        }

        ImageIcon iconGoc = new ImageIcon(path);
        Image anh = iconGoc.getImage();
        Image tinhChinhAnh = anh.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(tinhChinhAnh);
        JLabel picLabel = new JLabel();
        formTHongTinCaNhan.getPnAnhThongTInNhanVien().removeAll();
        formTHongTinCaNhan.getPnAnhThongTInNhanVien().add(picLabel);
        picLabel.setSize(new Dimension(240,320));
        picLabel.setIcon(icon);
    }
    public void tuongTac(boolean c){
        formTHongTinCaNhan.getTxtHoVaTen().setEnabled(c);
        formTHongTinCaNhan.getjDateChooserNgaySinh().setEnabled(c);
        formTHongTinCaNhan.getTxtDiaChi().setEnabled(c);
        formTHongTinCaNhan.getTxtSoDienThoai().setEnabled(c);
        formTHongTinCaNhan.getTxtEmail().setEnabled(c);
        formTHongTinCaNhan.getTxtTenTaiKhoan().setEnabled(c);
        formTHongTinCaNhan.getTxtMatKhau().setEnabled(c);
        formTHongTinCaNhan.getCbGioiTinh().setEnabled(c);
        formTHongTinCaNhan.getTxtMaNv().setEnabled(c);
        formTHongTinCaNhan.getCbChucVu().setEnabled(c);
        formTHongTinCaNhan.getCbCaLamViec().setEnabled(c);
        formTHongTinCaNhan.getCbTinhTrang().setEnabled(c);
        formTHongTinCaNhan.getBtnSuaAnh().setEnabled(c);
    }
    public void loadComboboxTTNV(){
        formTHongTinCaNhan.getCbGioiTinh().addItem("Nam");
        formTHongTinCaNhan.getCbGioiTinh().addItem("Nữ");
        ChucVu[] dsChucVu = ChucVu.values();
        String[] itemsChucVu = new String[dsChucVu.length + 1];
        itemsChucVu[0] = "--Select--";
        for (int i = 0; i < dsChucVu.length; i++) {
            itemsChucVu[i + 1] = dsChucVu[i].toString();
        }
        DefaultComboBoxModel<String> chucVuCb = new DefaultComboBoxModel<>(itemsChucVu);
        formTHongTinCaNhan.getCbChucVu().setModel(chucVuCb);

        CaLamViec[] dsCaLamViec = CaLamViec.values();
        String[] itemsCaLamViec = new String[dsCaLamViec.length + 1];
        itemsCaLamViec[0] = "--Select--";
        for (int i = 0; i < dsCaLamViec.length; i++) {
            itemsCaLamViec[i + 1] = dsCaLamViec[i].toString();
        }
        DefaultComboBoxModel<String> caLamViecCb = new DefaultComboBoxModel<>(itemsCaLamViec);
        formTHongTinCaNhan.getCbCaLamViec().setModel(caLamViecCb);

        TinhTrangNhanVien[] dsTinhTrangNhanVien = TinhTrangNhanVien.values();
        String[] itemsTinhTrangNhanVien = new String[dsTinhTrangNhanVien.length + 1];
        itemsTinhTrangNhanVien[0] = "--Select--";
        for (int i = 0; i < dsCaLamViec.length; i++) {
            itemsTinhTrangNhanVien[i + 1] = dsTinhTrangNhanVien[i].toString();
        }
        DefaultComboBoxModel<String> TinhTrangNhanVienCb = new DefaultComboBoxModel<>(itemsTinhTrangNhanVien);
        formTHongTinCaNhan.getCbTinhTrang().setModel(TinhTrangNhanVienCb);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(formTHongTinCaNhan.getBtnChinhSuaThongTin())){
            if(trangThaiChinhSua == 0){
                tuongTac(true);
                formTHongTinCaNhan.getTxtMaNv().setEnabled(false);
                formTHongTinCaNhan.getTxtTenTaiKhoan().setEnabled(false);
                trangThaiChinhSua = 1;
                formTHongTinCaNhan.getBtnChinhSuaThongTin().setText("Xác nhận");
            }else{
                String HoVaTen = formTHongTinCaNhan.getTxtHoVaTen().getText();
                String NgaySinh = new SimpleDateFormat("yyyy-MM-dd").format(formTHongTinCaNhan.getjDateChooserNgaySinh().getDate());
                LocalDate NgaySinh1 = LocalDate.parse(NgaySinh);
                String DiaChi = formTHongTinCaNhan.getTxtDiaChi().getText();
                String SoDienThoai = formTHongTinCaNhan.getTxtSoDienThoai().getText();
                String Email = formTHongTinCaNhan.getTxtEmail().getText();
                String TenTaiKhoan = formTHongTinCaNhan.getTxtTenTaiKhoan().getText();
                String MatKhau = formTHongTinCaNhan.getTxtMatKhau().getText();
                boolean GioiTinh = formTHongTinCaNhan.getCbGioiTinh().getSelectedItem().toString().equals("Nam");
                String MaNv = formTHongTinCaNhan.getTxtMaNv().getText();
                if(fileAnh.equals("")){
                    fileAnh = "Image_not_available.png";
                }
                try {
                    int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn cập nhật thông tin nhân viên này không?", "Thông báo", JOptionPane.YES_NO_OPTION);
                    if(result==JOptionPane.YES_OPTION){
                        NhanVien nhanVien = new NhanVien(MaNv, HoVaTen, SoDienThoai, GioiTinh, NgaySinh1, Email, DiaChi, ChucVu.layGiaTri(formTHongTinCaNhan.getCbChucVu().getSelectedItem().toString()), CaLamViec.layGiaTri(formTHongTinCaNhan.getCbCaLamViec().getSelectedItem().toString()), TenTaiKhoan, MatKhau, fileAnh, TinhTrangNhanVien.layGiaTri(formTHongTinCaNhan.getCbTinhTrang().getSelectedItem().toString()));
                        nhanVienDAO.capNhat(nhanVien);
                    }

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                trangThaiChinhSua = 0;
                formTHongTinCaNhan.getBtnChinhSuaThongTin().setText("Chỉnh sửa");
                tuongTac(false);
            }



        }
        if(e.getSource().equals(formTHongTinCaNhan.getBtnSuaAnh())){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png"));
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // Lấy tên file và đuôi file
                fileAnh = selectedFile.getName();
                importAnh(fileAnh);
            }
        }
    }

}
