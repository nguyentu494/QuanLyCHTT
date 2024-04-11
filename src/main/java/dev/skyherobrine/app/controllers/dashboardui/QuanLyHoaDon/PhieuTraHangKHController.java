//package dev.skyherobrine.app.controllers.dashboardui.QuanLyHoaDon;
//
//import dev.skyherobrine.app.daos.order.ChiTietPhieuTraKhachHangDAO;
//import dev.skyherobrine.app.daos.order.HoaDonDAO;
//import dev.skyherobrine.app.daos.order.PhieuTraKhachHangDAO;
//import dev.skyherobrine.app.daos.person.NhaCungCapDAO;
//import dev.skyherobrine.app.daos.product.SanPhamDAO;
//import dev.skyherobrine.app.entities.order.ChiTietPhieuTraKhachHang;
//import dev.skyherobrine.app.entities.order.HoaDon;
//import dev.skyherobrine.app.entities.order.PhieuNhapHang;
//import dev.skyherobrine.app.entities.order.PhieuTraKhachHang;
//import dev.skyherobrine.app.entities.person.NhaCungCap;
//import dev.skyherobrine.app.entities.product.DanhMucSanPham;
//import dev.skyherobrine.app.entities.product.LoaiSanPham;
//import dev.skyherobrine.app.entities.product.SanPham;
//import dev.skyherobrine.app.entities.product.ThuongHieu;
//import dev.skyherobrine.app.enums.*;
//import dev.skyherobrine.app.views.dashboard.component.QuanLyPhieuTraHangChoKhachHang;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.event.*;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//public class PhieuTraHangKHController  implements ActionListener, FocusListener, KeyListener,MouseListener {
//    private static int trangThaiNutThem = 0;
//    private static int trangThaiNutIn = 0;
//    private static ChiTietPhieuTraKhachHangDAO chiTietPhieuTraKhachHangDAO;
//    private static QuanLyPhieuTraHangChoKhachHang quanLyPhieuTraHangChoKhachHang;
//    private static PhieuTraKhachHangDAO phieuTraKhachHangDAO;
//    private NhaCungCapDAO nhaCungCapDAO;
//    private List<NhaCungCap> dsDanhMucNhaCungCap;
//    private static HoaDonDAO hoaDonDAO;
//    private SanPhamDAO sanPhamDAO;
//    private List<PhieuTraKhachHang> dsPhieuTra;
//    private List<ChiTietPhieuTraKhachHang> dsChiTietPhieuTra;
//
//    public PhieuTraHangKHController(QuanLyPhieuTraHangChoKhachHang quanLyPhieuTraHangChoKhachHang) {
//        try {
//            phieuTraKhachHangDAO = new PhieuTraKhachHangDAO();
//            nhaCungCapDAO = new NhaCungCapDAO();
//            hoaDonDAO = new HoaDonDAO();
//            sanPhamDAO = new SanPhamDAO();
//            chiTietPhieuTraKhachHangDAO = new ChiTietPhieuTraKhachHangDAO();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        this.quanLyPhieuTraHangChoKhachHang = quanLyPhieuTraHangChoKhachHang;
//    }
//    public void loadDsPhieuTraHang() {
//        DefaultTableModel clearTable = (DefaultTableModel) quanLyPhieuTraHangChoKhachHang.getTbDanhSachPhieuTraHangChoKhachHang().getModel();
//        clearTable.setRowCount(0);
//        quanLyPhieuTraHangChoKhachHang.getTbDanhSachPhieuTraHangChoKhachHang().setModel(clearTable);
//        try {
//            dsPhieuTra = phieuTraKhachHangDAO.timKiem();
//            DefaultTableModel tmHoaDon= (DefaultTableModel) quanLyPhieuTraHangChoKhachHang.getTbDanhSachPhieuTraHangChoKhachHang().getModel();
//            for(PhieuTraKhachHang pt : dsPhieuTra){
//                String row[] = {pt.getMaPhieuTraKhachHang(), pt.getNgayLap()+"", pt.getHoaDon().getMaHD(), chiTietPhieuTraKhachHangDAO.timKiem(pt.getMaPhieuTraKhachHang()).get().getSanPham().getTenSP(), chiTietPhieuTraKhachHangDAO.timKiem(pt.getMaPhieuTraKhachHang()).get().getSoLuongTra()+""};
//                tmHoaDon.addRow(row);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//    public LocalDate dateToLocalDate(Date date) {
//        Instant instant = date.toInstant();
//        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
//    }
//    private Boolean layDataThem() throws Exception {
//        PhieuTraKhachHang phieuTH;
//        ChiTietPhieuTraKhachHang phieuTraKhachHang;
////        String ten = quanLyPhieuTraHangChoKhachHang.getTxtTenSanPham().getText();
////        // Lấy ngày từ JDateChooser
////        Date selectedDate =  quanLyPhieuTraHangChoKhachHang.getjDateChooserNgayHenLay().getDate();
//        Date selectedDate1 =  quanLyPhieuTraHangChoKhachHang.getjDateChooserNgayLapPhieu().getDate();
//
//        // Chuyển đổi từ Date sang LocalDate
////        LocalDate localDate = dateToLocalDate(selectedDate);
//        LocalDate localDate1 = dateToLocalDate(selectedDate1);
//
//        TinhTrangTraHang TT = TinhTrangTraHang.layGiaTri(TinhTrangTraHang.CHUA_NHAN_HANG.toString());
//        String lyDoTraHang = quanLyPhieuTraHangChoKhachHang.getTxtLyDoTraHang().getText();
//        String maPhieuTra = quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuTra().getText();
//        String maPhieuNhap = quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap().getText();
////        String tinhTrang = quanLyPhieuTraHangChoKhachHang.getTxtTinhTrang().getText();
////        String tenNhaCungCap = quanLyPhieuTraHangChoKhachHang.getTxtTenNhaCungCapSanPham().getText();
//
//        TinhTrangTraHang tt = TinhTrangTraHang.layGiaTri(TinhTrangTraHang.CHUA_NHAN_HANG.toString());
////        int soLuong = Integer.parseInt(quanLyPhieuTraHangChoKhachHang.getTxtSoLuongTra().getText());
//
//        //Mã sản phẩm
//        String ma = quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuTra().getText();
////        try {
////            phieuTH = new PhieuTraKhachHang(ma, localDate1.atStartOfDay(), hoaDonDAO.timKiem(maPhieuNhap).get());
//////            phieuTraKhachHang = new ChiTietPhieuTraKhachHang(phieuTH, laySanPham(), soLuong, lyDoTraHang);
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
////        if(phieuTraKhachHangDAO.them(phieuTH)&&chiTietPhieuTraKhachHangDAO.them(phieuTraKhachHang)){
////            return true;
////        }
////        return false;
////    }
////    public SanPham laySanPham() throws Exception {
//////        String ten = quanLyPhieuTraHangChoKhachHang.getTxtTenSanPham().getText();
////        Map<String, Object> conditions = new HashMap<>();
//////        conditions.put("TenSP", ten);
////        List<SanPham> sp = sanPhamDAO.timKiem(conditions);
////        return sp.get(0);
////    }
////    public static void loadPTH() throws Exception {
////        String maPTH = "PTH-";
////        String nl = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
////        maPTH = maPTH+nl+"-";
////        Optional<HoaDon> hd = hoaDonDAO.timKiem(quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap().getText());
////        String SDT = hd.get().getKhachHang().getSoDienThoai();
////        SDT = SDT.substring(SDT.length()-3);
////        maPTH = maPTH + SDT + "-";
////        maPTH = maPTH + formatNumber(laySoPhieuNhap());
////        quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuTra().setText(maPTH);
////    }
//
////    public static int laySoPhieuNhap(){
////        String nlap = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
////
////        Map<String, Object> conditions = new HashMap<>();
////        conditions.put("MaPhieuTraKH", "%"+nlap+"%");
////        List<PhieuTraKhachHang> dsPhieuTra = new ArrayList<>();
////        try {
////            dsPhieuTra  = phieuTraKhachHangDAO.timKiem(conditions);
////        } catch (Exception e) {
////            return 1;
////        }
////        if(dsPhieuTra.size()==0){
////            return 1;
////        }
////        PhieuTraKhachHang ctpt = dsPhieuTra.get(dsPhieuTra.size()-1);
////        int soHD = Integer.parseInt(ctpt.getMaPhieuTraKhachHang().substring(ctpt.getMaPhieuTraKhachHang().length()-3));
////        return soHD+1;
////    }
////    public static String formatNumber(int number) {
////        if(number < 10)
////            return String.format("00%d", number);
////        else if((number >= 10) && (number < 100))
////            return String.format("0%d", number);
////        else
////            return String.format("%d", number);
////    }
////    public void loadComboBoxPhanTimKiem(){
////        //Load cbDanhMuc
////        try {
////            dsDanhMucNhaCungCap = nhaCungCapDAO.timKiem();
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
////        String[] NhaCungCapArray = dsDanhMucNhaCungCap.stream().map(NhaCungCap::getTenNCC).toArray(String[]::new);
////        String[] itemsNhaCungCap = new String[NhaCungCapArray.length + 1];
////        itemsNhaCungCap[0] = "--Nhà cung cấp--";
////        System.arraycopy(NhaCungCapArray, 0, itemsNhaCungCap, 1, NhaCungCapArray.length);
////        DefaultComboBoxModel<String> danhMucComboBoxModel = new DefaultComboBoxModel<>(itemsNhaCungCap);
//////        quanLyPhieuTraHangChoKhachHang.getCbTkNhaCungCap().setModel(danhMucComboBoxModel);
////
////
////        //Lấy tình trạng từ enum
////        TinhTrangTraHang[] dsTinhTrang = TinhTrangTraHang.values();
////        String[] itemsTinhTrang = new String[dsTinhTrang.length + 1];
////        itemsTinhTrang[0] = "--Tình trạng--";
////        for (int i = 0; i < dsTinhTrang.length; i++) {
////            itemsTinhTrang[i + 1] = dsTinhTrang[i].toString();
////        }
////        DefaultComboBoxModel<String> tinhTrangCb = new DefaultComboBoxModel<>(itemsTinhTrang);
//////        quanLyPhieuTraHangChoKhachHang.getCbTkTinhTrang().setModel(tinhTrangCb);
////
////    }
////    public void tuongTac(boolean c){
////       quanLyPhieuTraHangChoKhachHang.getTxtLyDoTraHang().setEnabled(c);
////       quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuTra().setEnabled(false);
//////       quanLyPhieuTraHangChoKhachHang.getTxtSoLuongTra().setEnabled(c);
////       quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap().setEnabled(c);
//////       quanLyPhieuTraHangChoKhachHang.getTxtTenSanPham().setEnabled(c);
//////       quanLyPhieuTraHangChoKhachHang.getTxtTinhTrang().setEnabled(c);
//////       quanLyPhieuTraHangChoKhachHang.getTxtTenNhaCungCapSanPham().setEnabled(c);
////    }
//    //Hàm đóng/mở tương tác tìm kiếm
//    public void tuongTacTimKiem(boolean o){
////        quanLyPhieuTraHangChoKhachHang.getCbTkNhaCungCap().setEnabled(o);
////        quanLyPhieuTraHangChoKhachHang.getCbTkTinhTrang().setEnabled(o);
//        quanLyPhieuTraHangChoKhachHang.getTxtTuKhoaTimKiem().setEnabled(o);
//    }
//    public void xoaTrangAll(){
//        quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuTra().setText("");
////        quanLyPhieuTraHangChoKhachHang.getTxtTenSanPham().setText("");
//        quanLyPhieuTraHangChoKhachHang.getjDateChooserNgayLapPhieu().setDate(null);
////        quanLyPhieuTraHangChoKhachHang.getjDateChooserNgayHenLay().setDate(null);
////        quanLyPhieuTraHangChoKhachHang.getTxtSoLuongTra().setText("");
//        quanLyPhieuTraHangChoKhachHang.getTxtLyDoTraHang().setText("");
////        quanLyPhieuTraHangChoKhachHang.getTxtTinhTrang().setText("");
////        quanLyPhieuTraHangChoKhachHang.getTxtTenNhaCungCapSanPham().setText("");
//        quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap().setText("");
//    }
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        Object op = e.getSource();
//        /*NÚT THÊM*/
//        if(op.equals(quanLyPhieuTraHangChoKhachHang.getBtnThemPhieu())){
//            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ thêm sản phẩm
//            if (trangThaiNutThem==0) {
//                quanLyPhieuTraHangChoKhachHang.getBtnThemPhieu().setText("Xác nhận thêm");
//                quanLyPhieuTraHangChoKhachHang.getBtnInPhieu().setText("Thoát thêm");
//                trangThaiNutThem = 1;
//
//                //Mở tương tác với thông tin
//                tuongTac(true);
//                tuongTacTimKiem(false);
//
//                //Xóa trắng dữ liệu
//                xoaTrangAll();
//            }
//            // Thực hiện chức năng nghiệp vụ thêm sản phẩm
//            else if(trangThaiNutThem==1) {
//                if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thêm sản phẩm mới", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
//                    try {
//                        if(layDataThem()){
//                            JOptionPane.showMessageDialog(null, "Thêm thành công");
//                            quanLyPhieuTraHangChoKhachHang.getBtnThemPhieu().setText("Thêm phiếu");
//                            quanLyPhieuTraHangChoKhachHang.getBtnInPhieu().setText("In phiếu");
//                            trangThaiNutThem = 0;
//                            tuongTac(false);
//                            tuongTacTimKiem(true);
//                            xoaTrangAll();
//                        }
//                        else{
//                            JOptionPane.showMessageDialog(null, "Thêm thất bại");
//                        }
//                    } catch (Exception ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }
//
//            }
//        }
//
//    }
//
//    @Override
//    public void focusGained(FocusEvent e) {
//
//    }
//
//    @Override
//    public void focusLost(FocusEvent e) {
//        if(e.getSource().equals(quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap())){
//            try {
//                loadPTH();
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//
//
//    }
//
//    @Override
//    public void keyTyped(KeyEvent e) {
//
//    }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//        if(e.getKeyCode()==KeyEvent.VK_ENTER){
//            if(e.getSource().equals(quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap())){
//                quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap().setFocusable(false);
//                quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap().setFocusable(true);
//            }
//        }
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//
//    }
//
//    @Override
//    public void mouseClicked(MouseEvent e) {
//        DefaultTableModel tmHoaDon = (DefaultTableModel) quanLyPhieuTraHangChoKhachHang.getTbDanhSachPhieuTraHangChoKhachHang().getModel();
//        int row = quanLyPhieuTraHangChoKhachHang.getTbDanhSachPhieuTraHangChoKhachHang().getSelectedRow();
//        tuongTac(false);
//        tuongTacTimKiem(true);
//        quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuTra().setText(tmHoaDon.getValueAt(row, 0).toString());
////        quanLyPhieuTraHangChoKhachHang.getTxtTenSanPham().setText(tmHoaDon.getValueAt(row, 3).toString());
////        quanLyPhieuTraHangChoKhachHang.getTxtSoLuongTra().setText(tmHoaDon.getValueAt(row, 4).toString());
//        quanLyPhieuTraHangChoKhachHang.getTxtMaPhieuNhap().setText(tmHoaDon.getValueAt(row, 2).toString());
//        String date = tmHoaDon.getValueAt(row, 1).toString();
//        Date date2 = null;
//        try {
//            date2 = new SimpleDateFormat("yyyy-mm-dd").parse(date);
//        } catch (ParseException ea) {
//            throw new RuntimeException(ea);
//
//        }
//        quanLyPhieuTraHangChoKhachHang.getjDateChooserNgayLapPhieu().setDate(date2);
////        quanLyPhieuTraHangChoKhachHang.getTxtTinhTrang().setText(TinhTrangTraHang.CHUA_NHAN_HANG.toString());
//        try {
////            quanLyPhieuTraHangChoKhachHang.getTxtTenNhaCungCapSanPham().setText(hoaDonDAO.timKiem(tmHoaDon.getValueAt(row, 2).toString()).get().getKhachHang().getHoTen());
//            quanLyPhieuTraHangChoKhachHang.getTxtLyDoTraHang().setText(chiTietPhieuTraKhachHangDAO.timKiem(tmHoaDon.getValueAt(row, 0).toString()).get().getNoiDungTra());
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) {
//
//    }
//
//    @Override
//    public void mouseExited(MouseEvent e) {
//
//    }
//}
