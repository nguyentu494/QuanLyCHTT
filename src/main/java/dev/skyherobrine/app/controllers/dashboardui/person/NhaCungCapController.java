package dev.skyherobrine.app.controllers.dashboardui.person;

import dev.skyherobrine.app.daos.person.NhaCungCapImp;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.enums.TinhTrangNhaCungCap;
import dev.skyherobrine.app.views.dashboard.component.FrmNhaCungCap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NhaCungCapController implements MouseListener, ActionListener, KeyListener {
    private FrmNhaCungCap frmNhaCungCap;
    private NhaCungCapImp nhaCungCapImp;
    private List<NhaCungCap> dsNhaCungCap;


    private static int trangThaiNutXoaNCC = 0;
    private static int trangThaiNutThemNCC = 0;
    private static int trangThaiNutSuaNCC = 0;


    public NhaCungCapController(FrmNhaCungCap frmNhaCungCap) {
        try {
            nhaCungCapImp = new NhaCungCapImp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.frmNhaCungCap = frmNhaCungCap;
    }

    //load danh sách lên table
    public void loaddsNhaCungCap() {
        DefaultTableModel clearTable = (DefaultTableModel) frmNhaCungCap.getTbDanhSachNhaCungCap().getModel();
        clearTable.setRowCount(0);
        frmNhaCungCap.getTbDanhSachNhaCungCap().setModel(clearTable);
        try {
            dsNhaCungCap = nhaCungCapImp.timKiem();
            DefaultTableModel tmNhaCungCap = (DefaultTableModel) frmNhaCungCap.getTbDanhSachNhaCungCap().getModel();
            for(NhaCungCap ncc : dsNhaCungCap){
                String row[] = {ncc.getMaNCC(), ncc.getTenNCC(), ncc.getDiaChiNCC(), ncc.getEmail(), ncc.getTinhTrang()+""};
                tmNhaCungCap.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        trangThaiNutThemNCC = 0;
        trangThaiNutSuaNCC = 0;
        trangThaiNutXoaNCC = 0;
    }
    private List<NhaCungCap> dsLoc;
    private List<NhaCungCap> dsTam = new ArrayList<NhaCungCap>();
    @Override
    public void actionPerformed(ActionEvent event) {
        Object op = event.getSource();
        /*NÚT THÊM*/
        if(op.equals(frmNhaCungCap.getButtonThemNhaCungCap())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ thêm nhà cung cấp
            if (trangThaiNutThemNCC==0) {
                frmNhaCungCap.getButtonThemNhaCungCap().setText("Xác nhận thêm");
                frmNhaCungCap.getButtonSuaNhaCungCap().setText("Xóa trắng");
                frmNhaCungCap.getButtonXoaNhaCungCap().setText("Thoát thêm");
                trangThaiNutXoaNCC = 1;
                trangThaiNutThemNCC = 1;
                trangThaiNutSuaNCC = 1;

                //Mở tương tác với thông tin
                tuongTac(true);
                tuongTacTimKiem(false);

                //Xóa trắng dữ liệu
                xoaTrangAll();
                //load sẵn mã nhà cung cấp
                frmNhaCungCap.getTxtMaNhaCungCap().setText(laymaNCC());
                frmNhaCungCap.getTxtMaNhaCungCap().setEnabled(false);
            }
            // Thực hiện chức năng nghiệp vụ thêm nhà cung cấp
            else if(trangThaiNutThemNCC==1) {
                NhaCungCap ncc = layDataThem();
                if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thêm nhà cung cấp mới", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
                    try {
                        if(nhaCungCapImp.them(ncc)){
                            loaddsNhaCungCap();
                            xoaTrangAll();
                            JOptionPane.showMessageDialog(null, "Thêm thành công!");
                            trangThaiNutThemNCC = 1;
                            trangThaiNutXoaNCC = 1;
                        }else{
                            JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
            //Thực hiện chức năng nghiệp vụ sửa nhà cung cấp
            else if(trangThaiNutThemNCC==2){
                if (frmNhaCungCap.getTxtMaNhaCungCap().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp cần sửa!");
                }else {
                    NhaCungCap nccSua = layDataSua();
                    if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn sửa nhà cung cấp có mã " +nccSua.getMaNCC()+" không?", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
                        try {
                            if(nhaCungCapImp.capNhat(nccSua)){
                                loaddsNhaCungCap();
                                xoaTrangAll();
                                JOptionPane.showMessageDialog(null, "Sửa thành công!");
                                trangThaiNutThemNCC = 2;
                            }else{
                                JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                trangThaiNutXoaNCC = 1;
            }
        }

        /*NÚT SỬA*/
        if(op.equals(frmNhaCungCap.getButtonSuaNhaCungCap())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ sửa nhà cung cấp
            if (trangThaiNutSuaNCC==0) {
                //Mở tương tác với thông tin
                tuongTac(true);

                frmNhaCungCap.getButtonThemNhaCungCap().setText("Xác nhận sửa");
                frmNhaCungCap.getButtonSuaNhaCungCap().setText("Xóa trắng");
                frmNhaCungCap.getButtonXoaNhaCungCap().setText("Thoát sửa");
                trangThaiNutThemNCC = 2;
                trangThaiNutSuaNCC = 1;
                trangThaiNutXoaNCC = 1;
                frmNhaCungCap.getTxtMaNhaCungCap().setEnabled(false);
            }
            // Thực hiện xóa trắng dữ liệu ở nghiệp vụ sửa thông tín nhà cung cấp
            else if(trangThaiNutSuaNCC==1) {
                xoaTrangSua();
            }
        }

        /*NÚT XÓA*/
        if (op.equals(frmNhaCungCap.getButtonXoaNhaCungCap())) {
            // Thực hiện chức năng nghiệp vụ xóa nhà cung cấp
            if (trangThaiNutXoaNCC==0) {
                if (frmNhaCungCap.getTxtMaNhaCungCap().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp cần xóa!");
                } else {
                    String ma = frmNhaCungCap.getTxtMaNhaCungCap().getText();
                    if ((JOptionPane.showConfirmDialog(null,
                            "Bạn có chắc muốn ngừng bán nhà cung cấp có mã " + frmNhaCungCap.getTxtMaNhaCungCap().getText() + " không?", "Lựa chọn",
                            JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION) {
                        try {
                            if (nhaCungCapImp.xoa(ma)){
                                loaddsNhaCungCap();
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
            // Thực hiện trả các nút về giao diện quản lý nhà cung cấp
            else if(trangThaiNutXoaNCC==1) {
                tuongTac(false);
                tuongTacTimKiem(true);
                xoaTrangAll();
                frmNhaCungCap.getButtonThemNhaCungCap().setText("Thêm nhà cung cấp");
                frmNhaCungCap.getButtonSuaNhaCungCap().setText("Sửa nhà cung cấp");
                frmNhaCungCap.getButtonXoaNhaCungCap().setText("Xóa nhà cung cấp");
                trangThaiNutXoaNCC = 0;
                trangThaiNutThemNCC = 0;
                trangThaiNutSuaNCC = 0;
            }
        }

        /*LỌC KHÁCH HÀNG*/
        if(op.equals(frmNhaCungCap.getCbTkTinhTrangNhaCungCap())){
            List<NhaCungCap> dsLoc = new ArrayList<>();
            List<NhaCungCap> dsTam = new ArrayList<>();
            if(!frmNhaCungCap.getCbTkTinhTrangNhaCungCap().getSelectedItem().equals("--Tình trạng--")){
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("TinhTrang", frmNhaCungCap.getCbTkTinhTrangNhaCungCap().getSelectedItem().toString());
                try {
                    dsLoc = nhaCungCapImp.timKiem(conditions);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            else{
                try {
                    dsLoc = nhaCungCapImp.timKiem();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            DefaultTableModel clearTable = (DefaultTableModel) frmNhaCungCap.getTbDanhSachNhaCungCap().getModel();
            clearTable.setRowCount(0);
            frmNhaCungCap.getTbDanhSachNhaCungCap().setModel(clearTable);
            try {
                DefaultTableModel tmNhaCungCap = (DefaultTableModel) frmNhaCungCap.getTbDanhSachNhaCungCap().getModel();
                for(NhaCungCap ncc : dsLoc){
                    String row[] = {ncc.getMaNCC(), ncc.getTenNCC(), ncc.getDiaChiNCC(), ncc.getEmail(), ncc.getTinhTrang()+""};
                    tmNhaCungCap.addRow(row);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Hàm đóng/mở tương tác
    public void tuongTac(boolean c){
        frmNhaCungCap.getTxtMaNhaCungCap().setEnabled(c);
        frmNhaCungCap.getTxtEmailNhaCungCap().setEnabled(c);
        frmNhaCungCap.getTxtDiaChiNhaCungCap().setEnabled(c);
        frmNhaCungCap.getTxtTenNhaCungCap().setEnabled(c);
        frmNhaCungCap.getCbTinhTrangNhaCungCap().setEnabled(c);
    }
    //Hàm đóng/mở tương tác tìm kiếm
    public void tuongTacTimKiem(boolean o){
        frmNhaCungCap.getTxtTuKhoaTimKiem().setEnabled(o);
        frmNhaCungCap.getCbTkTinhTrangNhaCungCap().setEnabled(o);
    }

    //Hàm xóa trắng sửa
    public void xoaTrangSua(){
        loadComboBoxPhanThongTinNCC();
        frmNhaCungCap.getTxtTenNhaCungCap().setText("");
        frmNhaCungCap.getTxtDiaChiNhaCungCap().setText("");
        frmNhaCungCap.getTxtMaNhaCungCap().setText("");
        frmNhaCungCap.getCbTinhTrangNhaCungCap().setSelectedIndex(0);
    }

    //Hàm xóa trắng dữ liệu nhập
    public void xoaTrangAll(){
        loadComboBoxPhanThongTinNCC();
        frmNhaCungCap.getTxtMaNhaCungCap().setText("");
        frmNhaCungCap.getTxtDiaChiNhaCungCap().setText("");
        frmNhaCungCap.getTxtMaNhaCungCap().setText("");
        frmNhaCungCap.getTxtTenNhaCungCap().setText("");
    }

    //Load các comboBox phần thông tin
    public void loadComboBoxPhanThongTinNCC(){
        TinhTrangNhaCungCap[] dsTinhTrang = TinhTrangNhaCungCap.values();
        String[] itemsTinhTrang = new String[dsTinhTrang.length + 1];
        itemsTinhTrang[0] = "--Select--";
        for (int i = 0; i < dsTinhTrang.length; i++) {
            itemsTinhTrang[i + 1] = dsTinhTrang[i].toString();
        }
        DefaultComboBoxModel<String> tinhTrangCb = new DefaultComboBoxModel<>(itemsTinhTrang);
        frmNhaCungCap.getCbTinhTrangNhaCungCap().setModel(tinhTrangCb);

    }

    //Load các comboBox phần tìm kiếm
    public void loadComboBoxPhanTimKiem(){
        TinhTrangNhaCungCap[] dsTinhTrang = TinhTrangNhaCungCap.values();
        String[] itemsTinhTrang = new String[dsTinhTrang.length + 1];
        itemsTinhTrang[0] = "--Tình trạng--";
        for (int i = 0; i < dsTinhTrang.length; i++) {
            itemsTinhTrang[i + 1] = dsTinhTrang[i].toString();
        }
        DefaultComboBoxModel<String> tinhTrangCb = new DefaultComboBoxModel<>(itemsTinhTrang);
        frmNhaCungCap.getCbTkTinhTrangNhaCungCap().setModel(tinhTrangCb);
    }

    //Hàm lấy nhà cung cấp từ phần thông tin
    private NhaCungCap layDataThem() {
        NhaCungCap ncc;
        String dc = frmNhaCungCap.getTxtDiaChiNhaCungCap().getText();
        String ten = frmNhaCungCap.getTxtTenNhaCungCap().getText();
        String email = frmNhaCungCap.getTxtEmailNhaCungCap().getText();
        TinhTrangNhaCungCap tt = TinhTrangNhaCungCap.layGiaTri(frmNhaCungCap.getCbTinhTrangNhaCungCap().getSelectedItem().toString());

        //Mã nhà cung cấp
        String ma = frmNhaCungCap.getTxtMaNhaCungCap().getText();
        try {
            ncc = new NhaCungCap(ma,ten,dc,email,tt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ncc;
    }

    //Hàm lấy nhà cung cấp để update
    private NhaCungCap layDataSua() {
        NhaCungCap ncc;
        String dc = frmNhaCungCap.getTxtDiaChiNhaCungCap().getText();
        String ten = frmNhaCungCap.getTxtTenNhaCungCap().getText();
        String email = frmNhaCungCap.getTxtEmailNhaCungCap().getText();
        TinhTrangNhaCungCap tt = TinhTrangNhaCungCap.layGiaTri(frmNhaCungCap.getCbTinhTrangNhaCungCap().getSelectedItem().toString());

        //Mã nhà cung cấp
        String ma = frmNhaCungCap.getTxtMaNhaCungCap().getText();
        try {
            ncc = new NhaCungCap(ma,ten,dc,email,tt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ncc;
    }

    //Hàm sinh mã nhà cung cấp
    private String laymaNCC() {
        String ma = "NCC-";
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        ma = ma+nThem;
        ma = ma+"-"+formatNumber(laysoDuoiMaKH());
        return ma;
    }

    //Hàm lấy số đuôi
    public int laysoDuoiMaKH(){
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("MaNCC", "%"+nThem+"%");
        List<NhaCungCap> nhaCC;
        try {
            nhaCC = nhaCungCapImp.timKiem(conditions);
        } catch (Exception e) {
            return 1;
        }
        if(nhaCC.size()==0){
            return 1;
        }
        NhaCungCap ncc = nhaCC.get(nhaCC.size()-1);
        int soHD = Integer.parseInt(ncc.getMaNCC().substring(ncc.getMaNCC().length()-3));
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
//    public LocalDate dateToLocalDate(Date date) {
//        Instant instant = date.toInstant();
//        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
//    }
    @Override
    public void mouseClicked(MouseEvent event) {
        if(trangThaiNutThemNCC==1){
            JOptionPane.showMessageDialog(null, "Đang thực hiện chức năng thêm, không được click!!");
        }else {
            int row = frmNhaCungCap.getTbDanhSachNhaCungCap().getSelectedRow();
            String ma = frmNhaCungCap.getTbDanhSachNhaCungCap().getValueAt(row, 0).toString();
            NhaCungCap nccHienThuc = new NhaCungCap();
            try {
                nccHienThuc = nhaCungCapImp.timKiem(ma);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            frmNhaCungCap.getTxtMaNhaCungCap().setText(nccHienThuc.getMaNCC());
            frmNhaCungCap.getTxtTenNhaCungCap().setText(nccHienThuc.getTenNCC());
            frmNhaCungCap.getTxtEmailNhaCungCap().setText(nccHienThuc.getEmail());
            frmNhaCungCap.getTxtDiaChiNhaCungCap().setText(nccHienThuc.getDiaChiNCC());
            frmNhaCungCap.getCbTinhTrangNhaCungCap().setSelectedItem(nccHienThuc.getTinhTrang().toString());
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
