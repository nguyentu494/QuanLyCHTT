package dev.skyherobrine.app.controllers.dashboardui.order;

import com.toedter.calendar.JDateChooser;
import dev.skyherobrine.app.daos.order.ChiTietPhieuNhapHangImp;
import dev.skyherobrine.app.daos.order.ChiTietPhieuNhapHangPhienBanSPImp;
import dev.skyherobrine.app.daos.order.PhieuNhapHangImp;
import dev.skyherobrine.app.daos.person.NhaCungCapImp;
import dev.skyherobrine.app.daos.product.ChiTietPhienBanSanPhamImp;
import dev.skyherobrine.app.daos.product.SanPhamImp;
import dev.skyherobrine.app.entities.Key.ChiTietPhieuNhapHangPhienBanSPId;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHangPhienBanSP;
import dev.skyherobrine.app.entities.order.PhieuNhapHang;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import dev.skyherobrine.app.entities.product.SanPham;
import dev.skyherobrine.app.enums.TinhTrangNhapHang;
import dev.skyherobrine.app.views.dashboard.component.QuanLyNhapHang;
import dev.skyherobrine.app.views.dashboard.component.nutDuyetVaNutXoaDongTb.*;
import dev.skyherobrine.app.views.dashboard.component.nutXoaDongTb.TableActionEvent;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class NhapHangController implements MouseListener, KeyListener, TableModelListener, ActionListener, PropertyChangeListener, TableActionEvent, TableActionEvent1, dev.skyherobrine.app.views.dashboard.component.nutChon.TableActionEvent {
    private QuanLyNhapHang nhapHangUI;
    private PhieuNhapHangImp phieuNhapHangImp;
    private List<PhieuNhapHang> dsPhieuNhap;
    private NhaCungCapImp nhaCungCapImp;
    private DefaultListModel<String> modelNCC;
    private ChiTietPhienBanSanPhamImp chiTietPhienBanSanPhamImp;
    private SanPhamImp sanPhamImp;
    private ChiTietPhieuNhapHangImp chiTietPhieuNhapHangImp;
    private ChiTietPhieuNhapHangPhienBanSPImp chiTietPhieuNhapHangPhienBanSPImp;


    private static Map<String, Integer> dsSPLuuTam = new HashMap<>();
    private static int trangThaiNutXuatFilePN = 0;
    private static int trangThaiNutThemPN = 0;
    private static int trangThaiNutSuaPN = 0;

    private static List<PhieuNhapHang> dsLoc;
    private List<PhieuNhapHang> dsTam = new ArrayList<>();
    private List<ChiTietPhieuNhapHang> dsCTPNH;
    private Map<String, Object> listSPDaChon = new HashMap<>();
    private Map<String, String> listMaCTPN = new HashMap<>();
    private Map<String, String> listMaCTPNXem = new HashMap<>();
    private List<ChiTietPhieuNhapHangPhienBanSP> dsCTPNHPBSP;
    private ChiTietPhieuNhapHang ctPNHTam;
    private PanelAction1 pnA1;

    public NhapHangController(QuanLyNhapHang nhapHangUI) {

        try {
            phieuNhapHangImp = new PhieuNhapHangImp();
            chiTietPhienBanSanPhamImp = new ChiTietPhienBanSanPhamImp();
            nhaCungCapImp = new NhaCungCapImp();
            sanPhamImp = new SanPhamImp();
            chiTietPhieuNhapHangImp = new ChiTietPhieuNhapHangImp();
            chiTietPhieuNhapHangPhienBanSPImp = new ChiTietPhieuNhapHangPhienBanSPImp();
            this.pnA1 =  new PanelAction1();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.nhapHangUI = nhapHangUI;
    }

    //Ham load phieu nhap hang
    public void loadPhieuNhap() {
        DefaultTableModel clearTable = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
        clearTable.setRowCount(0);
        nhapHangUI.getTbDanhSachPheiNhap().setModel(clearTable);
        try {
            dsPhieuNhap = phieuNhapHangImp.timKiem();
            DefaultTableModel tmKhachHang = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
            for(PhieuNhapHang pn : dsPhieuNhap){
                String row[] = {pn.getMaPhieuNhap(), pn.getNhaCungCap().getTenNCC(), pn.getNgayLapPhieu().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), pn.getNgayHenGiao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), pn.getTinhTrang().toString(), };
                tmKhachHang.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tuongTac(false);
    }

    //Su kien click tren table phieu nhap
    @Override
    public void mouseClicked(MouseEvent e) {
        Object ob = e.getSource();
        //Table phiếu nhập
        if(ob.equals(nhapHangUI.getTbDanhSachPheiNhap())){
            if(trangThaiNutThemPN==1){
                JOptionPane.showMessageDialog(null, "Đang thực hiện chức năng thêm, không được click!!");
            }else {
                //Mặc định do hiện tại chỉ có một cửa hàng nên không luu trữ dữ liệu cửa hàng vào phiếu nhập
                nhapHangUI.getTxtDiaChiCuaHang().setText("12 Nguyễn Văn Bảo, Gò Vấp, Hồ Chi Minh");
                nhapHangUI.getTxtDienThoaiCuaHang().setText("0273629111");
                nhapHangUI.getTxtEmailCUaHang().setText("thoitrangNTTT08@gmail.com");

                int row = nhapHangUI.getTbDanhSachPheiNhap().getSelectedRow();
                String ma = nhapHangUI.getTbDanhSachPheiNhap().getValueAt(row, 0).toString();
                PhieuNhapHang pnHienThuc = new PhieuNhapHang();
                try {
                    pnHienThuc = phieuNhapHangImp.timKiem(ma);
                } catch (Exception event) {
                    throw new RuntimeException(event);
                }

                nhapHangUI.getTxtMaPhieuNhap().setText(pnHienThuc.getMaPhieuNhap());
                nhapHangUI.getTxtNhaCungCap().setText(pnHienThuc.getNhaCungCap().getTenNCC());
                nhapHangUI.getTxtDiaChi().setText(pnHienThuc.getNhaCungCap().getDiaChiNCC());
                nhapHangUI.getTxtDiaChi().setCaretPosition(0);
                nhapHangUI.getTxtEmailNhaCungCap().setText(pnHienThuc.getNhaCungCap().getEmail());
                nhapHangUI.getTxtNgayLapPhieu().setText(pnHienThuc.getNgayLapPhieu().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                //Xử lý ngày
                String date = String.valueOf(pnHienThuc.getNgayHenGiao());
                Date ngayHenGiao = null;
                try {
                    ngayHenGiao = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                nhapHangUI.getjDateChooserNgayHenGiao().setDate(ngayHenGiao);

                List<ChiTietPhieuNhapHang> ct;
                List<ChiTietPhieuNhapHangPhienBanSP> ctPNHPBSP;
                Map<String,Object> conditions = new HashMap<>();
                conditions.put("MaPhieuNhap", pnHienThuc.getMaPhieuNhap());
                try {
                    ct = chiTietPhieuNhapHangImp.timKiem(conditions);

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                DefaultTableModel clear = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
                clear.setRowCount(0);
                nhapHangUI.getTbDanhSachSpTrongGioHang().setModel(clear);
                listMaCTPNXem = new HashMap<>();
                for(int i=0; i<ct.size(); i++){
                    Map<String , Object> conditions1 = new HashMap<>();
                    conditions1.put("MaChiTietPhieuNhap", ct.get(i).getMaChiTietPhieuNhap());

                    listMaCTPNXem.put(ct.get(i).getMaChiTietPhieuNhap(), ct.get(i).getSanPham().getMaSP());
                    try {
                        ctPNHPBSP = chiTietPhieuNhapHangPhienBanSPImp.timKiem(conditions1);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    int sl = 0;
                    for(int j=0; j<ctPNHPBSP.size(); j++){
                        sl+=ctPNHPBSP.get(j).getSoLuongNhap();
                    }
                    String row1[] = {(i+1)+"", ct.get(i).getSanPham().getMaSP(), ct.get(i).getSanPham().getTenSP(), null, sl+"", ct.get(i).getGiaNhap()+"", thanhTien(sl,ct.get(i).getGiaNhap())+"", null};
                    clear.addRow(row1);
                }

                nhapHangUI.getTxtTongTienSanPham().setText(String.valueOf(tongTienSanPhamNhap()));
                nhapHangUI.getTxtTinhTrangPhieuNhap().setText(pnHienThuc.getTinhTrang().toString());
                nhapHangUI.getTxtGhiChu().setText(pnHienThuc.getGhiChu());
                if (pnHienThuc.getTinhTrang().toString().equalsIgnoreCase("CHO_DUYET")){
                    nhapHangUI.getBtnTrangThaiPhieu().setText("Duyệt");
                    nhapHangUI.getBtnTrangThaiPhieu().setVisible(true);
                    nhapHangUI.getBtnTrangThaiPhieu().setEnabled(true);
                    nhapHangUI.getCbkDuyet().setSelected(false);
                    nhapHangUI.getCbkDangLayHang().setSelected(false);
                    nhapHangUI.getCbkHoanThanh().setSelected(false);
                } else if (pnHienThuc.getTinhTrang().toString().equalsIgnoreCase("DANG_CHUYEN")) {
                    nhapHangUI.getCbkDuyet().setSelected(true);
                    nhapHangUI.getCbkDangLayHang().setSelected(false);
                    nhapHangUI.getCbkHoanThanh().setSelected(false);
                    nhapHangUI.getBtnTrangThaiPhieu().setText("Nhập vào kho");
                    nhapHangUI.getBtnTrangThaiPhieu().setVisible(true);
                    nhapHangUI.getBtnTrangThaiPhieu().setEnabled(true);
                } else if (pnHienThuc.getTinhTrang().toString().equalsIgnoreCase("DA_NHAN")) {
                    nhapHangUI.getCbkDuyet().setSelected(true);
                    nhapHangUI.getCbkDangLayHang().setSelected(true);
                    nhapHangUI.getCbkHoanThanh().setSelected(true);
                    nhapHangUI.getBtnTrangThaiPhieu().setVisible(false);
                }
            }
        }

        //Table ChonPBSP
        if(ob.equals(nhapHangUI.getTblChonPBSP())){
            int row = nhapHangUI.getTblChonPBSP().getSelectedRow();
            boolean isChecked = (Boolean) nhapHangUI.getTblChonPBSP().getValueAt(row, 4);

            // Nếu ô kiểm tra được tích, di chuyển con trỏ nhập văn bản đến ô trong cùng một hàng, nhưng ở cột trước đó
            if (isChecked) {
                nhapHangUI.getTblChonPBSP().editCellAt(row, 3);
                Component editor = nhapHangUI.getTblChonPBSP().getEditorComponent();
                if (editor instanceof JTextComponent) {
                    JTextComponent textComponent = (JTextComponent) editor;
                    textComponent.requestFocusInWindow();
                }
            }
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

    //Tim kiem san pham de nhap
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            if(e.getSource().equals(nhapHangUI.getTxtTimKiemSanPhamNhap())){
                nhapHangUI.getListSPNhap().setSelectedIndex(nhapHangUI.getListSPNhap().getSelectedIndex()+1);
                nhapHangUI.getListSPNhap().requestFocus();
            }else if(e.getSource().equals(nhapHangUI.getTxtNhaCungCap())){
                nhapHangUI.getListNCCNhap().setSelectedIndex(nhapHangUI.getListNCCNhap().getSelectedIndex()+1);
                nhapHangUI.getListNCCNhap().requestFocus();
            }
        }else if(e.getKeyCode()==KeyEvent.VK_ENTER) {
            if (e.getSource().equals(nhapHangUI.getListSPNhap())) {
                String sp = nhapHangUI.getListSPNhap().getSelectedValue().toString();
                if(themSP(sp)){
                    nhapHangUI.getTxtTimKiemSanPhamNhap().setText("");
                    nhapHangUI.getTxtTimKiemSanPhamNhap().requestFocus(true);
                    nhapHangUI.getMenuSPNhap().setVisible(false);
                }else{
                    nhapHangUI.getTxtTimKiemSanPhamNhap().requestFocus(true);
                }
            }else{
                loadTTNCC(nhapHangUI.getListNCCNhap().getSelectedValue().toString());
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String textSP = nhapHangUI.getTxtTimKiemSanPhamNhap().getText().trim();
        String textNCC = nhapHangUI.getTxtNhaCungCap().getText().trim();
        if(e.getKeyCode()!=KeyEvent.VK_DOWN && e.getKeyCode()!=KeyEvent.VK_UP && e.getKeyCode()!=KeyEvent.VK_ENTER ){
            if(e.getSource().equals(nhapHangUI.getTxtTimKiemSanPhamNhap())){
                if(!textSP.equalsIgnoreCase("")) {
                    searchSuggestSP(textSP);
                }else{
                    nhapHangUI.getMenuSPNhap().setVisible(false);
                }
            }
            else{
                if(!textNCC.equalsIgnoreCase("")){
                    searchSuggestNCC(textNCC);
                }else{
                    nhapHangUI.getMenuNCCNhap().setVisible(false);
                }
            }
        }
    }

    //Tìm sản phẩm
    public void searchSuggestSP(String textSP){
        DefaultListModel<String> listModelSP = new DefaultListModel<>();
        nhapHangUI.getListSPNhap().setModel(listModelSP);
        listModelSP.removeAllElements();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("MaSP", textSP);
        String []colNames= {"MaSP", "TenSP"};
        try {
            List<Map<String, Object>> listSP = sanPhamImp.timKiem(conditions, false, colNames);
            DefaultTableModel tmSP = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
            if(listSP.size()==0){
                nhapHangUI.getMenuSPNhap().setVisible(false);
            }else{
                for(int i = 0; i < listSP.size(); i++){
                    if(!listSPDaChon.containsKey(listSP.get(i).get("MaSP").toString())){
                        listModelSP.addElement(listSP.get(i).get("MaSP").toString()+" || "+listSP.get(i).get("TenSP").toString());
                    }
                }
                nhapHangUI.getMenuSPNhap().show(nhapHangUI.getTxtTimKiemSanPhamNhap(), 0, nhapHangUI.getTxtTimKiemSanPhamNhap().getHeight());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //Tìm nhà cung cấp
    public void searchSuggestNCC(String textNCC){
        List<NhaCungCap> listNCC;
        DefaultListModel<String> listModelNCC = new DefaultListModel<>();
        nhapHangUI.getListNCCNhap().setModel(listModelNCC);
        listModelNCC.removeAllElements();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("TenNCC", textNCC);
        try {
            listNCC = nhaCungCapImp.timKiem(conditions);
            if(listNCC.size()==0){
                nhapHangUI.getMenuNCCNhap().setVisible(false);
            }else{
                for(int i = 0; i < listNCC.size(); i++){
                    listModelNCC.addElement(listNCC.get(i).getTenNCC());
                }
                nhapHangUI.getMenuNCCNhap().show(nhapHangUI.getTxtNhaCungCap(), 0, nhapHangUI.getTxtNhaCungCap().getHeight());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //load thông tin nhà cung cấp
    public boolean loadTTNCC(String tenNCC) {
        // TODO implement here
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("TenNCC", tenNCC);
        try {
            List<NhaCungCap> ncc = nhaCungCapImp.timKiem(conditions);
            if(ncc.size()!=0) {
                nhapHangUI.getTxtNhaCungCap().setText(ncc.get(0).getTenNCC());
                nhapHangUI.getTxtDiaChi().setText(ncc.get(0).getDiaChiNCC());
                nhapHangUI.getTxtEmailNhaCungCap().setText(ncc.get(0).getEmail());
                nhapHangUI.getMenuNCCNhap().setVisible(false);
                return true;
            }
//            nhapHangUI.getTxtMaPhieuNhap().setText(layMaPhieuNhap());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //Thêm sản phẩm vào giỏ hàng
    public boolean themSP(String maSP){
        DefaultTableModel tmGioHang = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
        String maSanPham = "";
        maSanPham = maSP.substring(0, maSP.indexOf(" || "));
        SanPham sp  = new SanPham();
        try {
            sp = sanPhamImp.timKiem(maSanPham);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int stt = tmGioHang.getRowCount() + 1;
        int sl = 0;
        double gia = 0;
        String []row = {stt+"", maSanPham, sp.getTenSP(),null, sl+"", gia+"", thanhTien(sl, gia)+"", null};
        listSPDaChon.put(maSanPham,"");
        tmGioHang.addRow(row);
        int lastRowIndex = tmGioHang.getRowCount() - 1;
        nhapHangUI.getTbDanhSachSpTrongGioHang().changeSelection(lastRowIndex, 0, false, false);
        nhapHangUI.getTxtTongTienSanPham().setText(tinhTT()+"");
        listMaCTPN.put(maCTPN(), maSanPham);
        return true;
    }

    //Tính thành tiền
    private String thanhTien(int sl, Double gia) {
        double tt = 0;
        tt = sl*gia;
        return new DecimalFormat("0.00").format(tt);
    }

    //Ham tinh tong tien san pham
    private String tongTienSanPhamNhap() {
        DefaultTableModel tmGioHang = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
        double tongTien = 0;
        for(int i = 0; i < tmGioHang.getRowCount(); i++){
            tongTien+=(Double.parseDouble(tmGioHang.getValueAt(i, 6).toString()));
        }
        return new DecimalFormat("0.00").format(tongTien);
    }

    //Tính tổng tiền sản phẩm
    public double tinhTT(){
        DefaultTableModel tmGioHang = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
        double tt = 0;
        for(int i = 0; i < tmGioHang.getRowCount(); i++){
            tt+=(Double.parseDouble(tmGioHang.getValueAt(i, 6).toString()));
        }
        return tt;
    }

    //Load các PBSP vào bảng chọn
    private void loadPBSP(String maSP) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("MaSP" , maSP);
        List<ChiTietPhienBanSanPham> pbsp;
        DefaultTableModel clearTable = (DefaultTableModel) nhapHangUI.getTblChonPBSP().getModel();
        clearTable.setRowCount(0);
        nhapHangUI.getTblChonPBSP().setModel(clearTable);
        try {
            pbsp = chiTietPhienBanSanPhamImp.timKiem(conditions);
            DefaultTableModel tmPBSP = (DefaultTableModel) nhapHangUI.getTblChonPBSP().getModel();
            for(ChiTietPhienBanSanPham pp : pbsp){
                Object row[] = {pp.getMaPhienBanSP(), pp.getMauSac()+"", pp.getKichThuoc(), "", false};
                tmPBSP.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, String> listSPDaCo = new HashMap<>();
        DefaultTableModel tmPBSP = (DefaultTableModel) nhapHangUI.getTblChonPBSP().getModel();
        if(listSPDaChon.containsKey(maSP)){
            if(!listSPDaChon.get(maSP).toString().equalsIgnoreCase("")){
                listSPDaCo = (Map<String, String>) listSPDaChon.get(maSP);
                for(int i = 0; i < tmPBSP.getRowCount(); i++){
                    if(listSPDaCo.containsKey(tmPBSP.getValueAt(i,0).toString())){
                        tmPBSP.setValueAt(listSPDaCo.get(tmPBSP.getValueAt(i,0).toString()), i, 3);
                        tmPBSP.setValueAt(true, i,4);
                    }
                }
            }
        }
    }

    //Bắt sự kiện cho table khi nhập giá để tính thành tiền và tổng tiền
    @Override
    public void tableChanged(TableModelEvent e) {
        if(e.getColumn()==5 || e.getColumn()==4){
            DefaultTableModel tmGioHang = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
            int row = e.getFirstRow();
            int sl = Integer.parseInt(tmGioHang.getValueAt(row, 4).toString());
            double gia = Double.parseDouble(tmGioHang.getValueAt(row, 5).toString());
            tmGioHang.setValueAt(sl*gia, row, 6);
            double tt = 0;
            for(int i = 0; i < tmGioHang.getRowCount(); i++){
                tt += Double.parseDouble(tmGioHang.getValueAt(i, 6).toString());
            }
            nhapHangUI.getTxtTongTienSanPham().setText(tt+"");
        }
    }

    //Hàm chỉ định vị trí con trỏ nhập
    private void setCursorPositionCMP(Component component) {
        component.requestFocusInWindow();
    }

    private void setCursorPositionTable(JTable table, int row, int column) {
        if (table.editCellAt(row, column)) {
            Component editor = table.getEditorComponent();
            editor.requestFocusInWindow();

            // Clear the content of the cell
            if (editor instanceof JTextComponent) {
                JTextComponent textComponent = (JTextComponent) editor;
                textComponent.setText("");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object op = e.getSource();
        /*NÚT THÊM*/
        if(op.equals(nhapHangUI.getBtnThemPhieuNhap())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ thêm phieu nhap

            if (trangThaiNutThemPN==0) {
                nhapHangUI.getTbDanhSachPheiNhap().clearSelection();
                nhapHangUI.getBtnThemPhieuNhap().setText("Thêm và duyệt");
                nhapHangUI.getBtnSuaPhieuNhap().setText("Thêm phiếu");
                nhapHangUI.getBtnXuatFile().setText("Thoát thêm");
                trangThaiNutXuatFilePN = 1;
                trangThaiNutThemPN = 1;
                trangThaiNutSuaPN = 2   ;

                //Mở tương tác với thông tin
                tuongTac(true);
                tuongTacTimKiem(false);
                //Xóa trắng dữ liệu
                xoaTrangAll();
                nhapHangUI.getTxtDiaChiCuaHang().setText("12 Nguyễn Văn Bảo, Gò Vấp, Hồ Chi Minh");
                nhapHangUI.getTxtDienThoaiCuaHang().setText("0273629111");
                nhapHangUI.getTxtEmailCUaHang().setText("thoitrangNTTT08@gmail.com");
                nhapHangUI.getTxtMaPhieuNhap().setEnabled(false);
                nhapHangUI.getTxtMaPhieuNhap().setText(layMaPN());
                nhapHangUI.getTxtNgayLapPhieu().setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString());
            }
            // Thực hiện chức năng nghiệp vụ thêm phiếu nhập và duyệt
            else if(trangThaiNutThemPN==1) {
                DefaultTableModel tmSP = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
                if(tmSP.getRowCount()==0){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần nhập", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(nhapHangUI.getTxtNhaCungCap().getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(nhapHangUI.getjDateChooserNgayHenGiao().getDate() == null){
                    JOptionPane.showMessageDialog(null, "Ngày hẹn giao không được để trống", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                PhieuNhapHang pnh = layDataPhieuNhap("DANG_CHUYEN");
                try {
                    if(phieuNhapHangImp.them(pnh)){
                        ChiTietPhieuNhapHang ctpn;
                        ChiTietPhieuNhapHangPhienBanSP ctpnhPBSP;
                        double gia = 0;
                        for(Map.Entry<String, String> entry : listMaCTPN.entrySet()){
                            SanPham sp = sanPhamImp.timKiem(entry.getValue());
                            for(int i = 0; i < tmSP.getRowCount(); i++){
                                if(tmSP.getValueAt(i, 1).toString().equalsIgnoreCase(entry.getValue())){
                                    gia = Double.parseDouble(tmSP.getValueAt(i, 5).toString());
                                }
                            }
                            ctpn = new ChiTietPhieuNhapHang(entry.getKey(), pnh, sp, gia);
                            chiTietPhieuNhapHangImp.them(ctpn);
                            Map<String, String> listPBSP = (Map<String, String>) listSPDaChon.get(entry.getValue());
                            for(Map.Entry<String, String> entry1 : listPBSP.entrySet()){
                                ChiTietPhienBanSanPham pbsp = chiTietPhienBanSanPhamImp.timKiem(entry1.getKey());
                                ctpnhPBSP = new ChiTietPhieuNhapHangPhienBanSP(new ChiTietPhieuNhapHangPhienBanSPId(ctpn, pbsp), Integer.parseInt(entry1.getValue()));

//                                ctpnhPBSP = new ChiTietPhieuNhapHangPhienBanSP();

                                chiTietPhieuNhapHangPhienBanSPImp.them(ctpnhPBSP);
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Thêm phiếu nhập và duyệt phiếu thành công!");
                        listMaCTPN = new HashMap<>();
                        listSPDaChon = new HashMap<>();
                        loadPhieuNhap();
                        nhapHangUI.getBtnThemPhieuNhap().setText("Thêm phiếu nhập");
                        nhapHangUI.getBtnSuaPhieuNhap().setText("Sửa phiếu nhập");
                        nhapHangUI.getBtnXuatFile().setText("Xóa phiếu chờ");
                        trangThaiNutXuatFilePN = 0;
                        trangThaiNutThemPN = 0;
                        trangThaiNutSuaPN = 0;
                        tuongTac(false);
                        tuongTacTimKiem(true);
                        tmSP.setRowCount(0);
                    }else {
                        JOptionPane.showMessageDialog(null, "Thêm phiếu nhập thất bại!");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //Thực hiện chức năng nghiệp vụ sửa phiếu nhập
            else if(trangThaiNutThemPN==2){
                PhieuNhapHang phieuNhapHang = layDataPhieuNhap("CHO_DUYET");
                Map<String, String> listPBSP = new HashMap<>();
                Map<String, Object> listPBSPDaCo = new HashMap<>();
                DefaultTableModel tmGioHang = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
                double gia = 0;
                try {
                    if(phieuNhapHangImp.capNhat(phieuNhapHang)){

                        System.out.println(listMaCTPNXem);
                        for(Map.Entry<String, String> entry : listMaCTPNXem.entrySet()){
                            listPBSP = (Map<String, String>) listSPDaChon.get(entry.getValue());
                            Optional<ChiTietPhieuNhapHang> ctpnh = chiTietPhieuNhapHangImp.timKiem(phieuNhapHang.getMaPhieuNhap(), entry.getValue());
                            for(int i = 0; i < tmGioHang.getRowCount(); i++){
                                if(tmGioHang.getValueAt(i, 1).toString().equalsIgnoreCase(entry.getValue())){
                                    gia = Double.parseDouble(tmGioHang.getValueAt(i, 5).toString());
                                }
                            }
                            ctpnh.get().setGiaNhap(gia);
                            chiTietPhieuNhapHangImp.capNhat(ctpnh.get());
                            if(listSPDaChon.get(entry.getValue())!=null){
                                for(Map.Entry<String, String> entry1 : listPBSP.entrySet()){
                                    listPBSPDaCo.put("MaChiTietPhieuNhap", entry.getKey());
                                    listPBSPDaCo.put("MaPhienBanSP", entry1.getKey());
                                    List<ChiTietPhieuNhapHangPhienBanSP> ctpnhPBSP = chiTietPhieuNhapHangPhienBanSPImp.timKiem(listPBSPDaCo);
                                    System.out.println(ctpnhPBSP.get(0));
                                    ctpnhPBSP.get(0).setSoLuongNhap(Integer.parseInt(entry1.getValue()));
                                    chiTietPhieuNhapHangPhienBanSPImp.capNhat(ctpnhPBSP.get(0));
                                }
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Sửa phiếu nhập thành công!");
                        listMaCTPN = new HashMap<>();
                        listSPDaChon = new HashMap<>();
                        loadPhieuNhap();
                        xoaTrangAll();
                        nhapHangUI.getBtnThemPhieuNhap().setText("Thêm phiếu nhập");
                        nhapHangUI.getBtnSuaPhieuNhap().setText("Sửa phiếu nhập");
                        nhapHangUI.getBtnXuatFile().setText("Xóa phiếu chờ");
                        trangThaiNutXuatFilePN = 0;
                        trangThaiNutThemPN = 0;
                        trangThaiNutSuaPN = 0;
                        tuongTac(false);
                        tuongTacTimKiem(true);
                        tmGioHang.setRowCount(0);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        /*NÚT SỬA*/
        if(op.equals(nhapHangUI.getBtnSuaPhieuNhap())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ sửa nhân viên
            if (trangThaiNutSuaPN==0) {
                //Mở tương tác với thông tin
                DefaultTableModel tmPhieu = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                int row = nhapHangUI.getTbDanhSachPheiNhap().getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu nhập cần sửa", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }else{
                    String tinhTrang = tmPhieu.getValueAt(row, 4).toString();
                    if(!tinhTrang.equalsIgnoreCase("CHO_DUYET")){
                        JOptionPane.showMessageDialog(null, "Phiếu nhập đã nhận không thể sửa", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                tuongTac(false);
                tuongTacTimKiem(false);
                nhapHangUI.getTxtNhaCungCap().setEnabled(true);
                nhapHangUI.getjDateChooserNgayHenGiao().setEnabled(true);
                nhapHangUI.getTxtGhiChu().setEnabled(true);
                nhapHangUI.getBtnThemPhieuNhap().setText("Xác nhận sửa");
                nhapHangUI.getBtnSuaPhieuNhap().setText("Xóa trắng");
                nhapHangUI.getBtnXuatFile().setText("Thoát sửa");
                trangThaiNutThemPN = 2;
                trangThaiNutSuaPN = 1;
                trangThaiNutXuatFilePN = 1;
            }
            // Thực hiện xóa trắng dữ liệu ở nghiệp vụ sửa thông tín nhân viên
            else if(trangThaiNutSuaPN==1) {
                return;
            }
            //Thực hiện chức năng chỉ thêm phiếu nhập và phiếu đó sẽ ở trạng thái chờ duyệt
            else if (trangThaiNutSuaPN==2) {
                DefaultTableModel tmSP = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
                if(tmSP.getRowCount()==0){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần nhập", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(nhapHangUI.getTxtNhaCungCap().getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(nhapHangUI.getjDateChooserNgayHenGiao().getDate() == null){
                    JOptionPane.showMessageDialog(null, "Ngày hẹn giao không được để trống", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                PhieuNhapHang pnh = layDataPhieuNhap("CHO_DUYET");

                try {
                    if(phieuNhapHangImp.them(pnh)){
                        ChiTietPhieuNhapHang ctpn;
                        ChiTietPhieuNhapHangPhienBanSP ctpnhPBSP;
                        double gia = 0;
                        for(Map.Entry<String, String> entry : listMaCTPN.entrySet()){
                            SanPham sp = sanPhamImp.timKiem(entry.getValue());
                            for(int i = 0; i < tmSP.getRowCount(); i++){
                                if(tmSP.getValueAt(i, 1).toString().equalsIgnoreCase(entry.getValue())){
                                    gia = Double.parseDouble(tmSP.getValueAt(i, 5).toString());
                                }
                            }
                            ctpn = new ChiTietPhieuNhapHang(entry.getKey(), pnh, sp, gia);
                            chiTietPhieuNhapHangImp.them(ctpn);
                            Map<String, String> listPBSP = (Map<String, String>) listSPDaChon.get(entry.getValue());
                            for(Map.Entry<String, String> entry1 : listPBSP.entrySet()){
                                ChiTietPhienBanSanPham pbsp = chiTietPhienBanSanPhamImp.timKiem(entry1.getKey());
                                ctpnhPBSP = new ChiTietPhieuNhapHangPhienBanSP(new ChiTietPhieuNhapHangPhienBanSPId(ctpn, pbsp), Integer.parseInt(entry1.getValue()));
//                                ctpnhPBSP = new ChiTietPhieuNhapHangPhienBanSP();
                                chiTietPhieuNhapHangPhienBanSPImp.them(ctpnhPBSP);
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Thêm phiếu nhập và duyệt phiếu thành công!");
                        loadPhieuNhap();
                        listMaCTPN = new HashMap<>();
                        listSPDaChon = new HashMap<>();
                        nhapHangUI.getBtnThemPhieuNhap().setText("Thêm phiếu nhập");
                        nhapHangUI.getBtnSuaPhieuNhap().setText("Sửa phiếu nhập");
                        nhapHangUI.getBtnXuatFile().setText("Xóa phiếu chờ");
                        trangThaiNutXuatFilePN = 0;
                        trangThaiNutThemPN = 0;
                        trangThaiNutSuaPN = 0;
                        tuongTac(false);
                        tuongTacTimKiem(true);
                        tmSP.setRowCount(0);
                    }else {
                        JOptionPane.showMessageDialog(null, "Thêm phiếu nhập thất bại!");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        /*NÚT XÓA PHIẾU CHỜ*/
        if (op.equals(nhapHangUI.getBtnXuatFile())) {
            // Thực hiện chức năng nghiệp vụ xóa nhân viên
            if (trangThaiNutXuatFilePN==0) {
                DefaultTableModel tmPhieuNhap = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                DefaultTableModel tmGioHang = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
                int row = nhapHangUI.getTbDanhSachPheiNhap().getSelectedRow();
                if(row == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu nhập cần xóa", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String tinhTrang = tmPhieuNhap.getValueAt(row, 4).toString();
                if(!tinhTrang.equalsIgnoreCase("CHO_DUYET")){
                    JOptionPane.showMessageDialog(null, "Phiếu nhập đã duyệt không thể xóa", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }else{
                    String MaPN = tmPhieuNhap.getValueAt(row, 0).toString();
                    int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa phiếu nhập có mã " + MaPN + " không?", "Lựa chọn", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION){
                        try {
                            for(Map.Entry entry : listMaCTPNXem.entrySet()){
                                chiTietPhieuNhapHangPhienBanSPImp.xoa(entry.getKey().toString());
                            }
                            chiTietPhieuNhapHangImp.xoa(MaPN);
                            phieuNhapHangImp.xoa(MaPN);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        JOptionPane.showMessageDialog(null, "Xóa phiếu nhập thành công!");
                        loadPhieuNhap();
                        tmGioHang.setRowCount(0);
                    }
                }
//                if (nhapHangUI.getTxtMatKhauNhanVien().getText().equals("")) {
//                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xóa!");
//                } else {
//                    String ma = nhapHangUI.getTxtMaNhanVien().getText();
//                    if ((JOptionPane.showConfirmDialog(null,
//                            "Bạn có chắc muốn ngừng bán nhân viên có mã " + nhapHangUI.getTxtMaNhanVien().getText() + " không?", "Lựa chọn",
//                            JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION) {
//                        try {
//                            if (nhanVienDAO.xoa(ma)){
//                                loadDsNhanVien();
//                                xoaTrangAll();
//                                JOptionPane.showMessageDialog(null, "Xóa thành công!");
//                            }else {
//                                JOptionPane.showMessageDialog(null, "Xóa thất bại!");
//                            }
//                        } catch (Exception e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
            }
            // Thực hiện trả các nút về giao diện quản lý nhân viên
            else if(trangThaiNutXuatFilePN==1) {
                tuongTac(false);
                tuongTacTimKiem(true);
                xoaTrangAll();
                nhapHangUI.getBtnThemPhieuNhap().setText("Thêm phiếu nhập");
                nhapHangUI.getBtnSuaPhieuNhap().setText("Sửa phiếu nhập");
                nhapHangUI.getBtnXuatFile().setText("Xóa phiếu chờ");
                trangThaiNutXuatFilePN = 0;
                trangThaiNutThemPN = 0;
                trangThaiNutSuaPN = 0;
            }
        }

        /*LỌC PHIẾU NHẬP*/
        //Lọc phiếu nhập từ ComboBox
        if(op.equals(nhapHangUI.getCbTkNhaCungCap()) || op.equals(nhapHangUI.getCbTkTinhTrang())){
            if(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate() == null && nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() == null){
                if(!nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--")){
                    List<NhaCungCap> ncc;
                    Map<String, Object> conditions = new HashMap<>();
                    conditions.put("TenNCC", nhapHangUI.getCbTkNhaCungCap().getSelectedItem().toString());
                    try {
                        ncc = nhaCungCapImp.timKiem(conditions);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    Map<String, Object> conditions1 = new HashMap<>();
                    conditions1.put("MaNCC", ncc.get(0).getMaNCC());
                    try {
                        dsLoc = phieuNhapHangImp.timKiem(conditions1);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    if(!nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                        for(int i=0; i<dsLoc.size(); i++){
                            if(nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if (!nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")) {
                    Map<String, Object> conditions = new HashMap<>();
                    conditions.put("TinhTrang", nhapHangUI.getCbTkTinhTrang().getSelectedItem().toString());
                    try {
                        dsLoc = phieuNhapHangImp.timKiem(conditions);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    try {
                        dsLoc = phieuNhapHangImp.timKiem();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            else if (nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() != null) {
                Map<String,Object> conditions = new HashMap<>();
                conditions.put("NgayHenGiao", dateToLocalDate(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate()));
                try {
                    dsLoc = phieuNhapHangImp.timKiem(conditions);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                if(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate() != null){
                    for (int i=0; i<dsLoc.size(); i++){
                        if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayLapPhieu()))){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                    if (!nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                        for (int i=0; i<dsLoc.size(); i++){
                            if(nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                        if(!nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--")){
                            for (int i=0; i<dsLoc.size(); i++){
                                if(nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals(dsLoc.get(i).getNhaCungCap().getTenNCC())){
                                    dsTam.add(dsLoc.get(i));
                                }
                            }
                            dsLoc = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if(!nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--")) {
                        for (int i=0; i<dsLoc.size(); i++){
                            if(nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals(dsLoc.get(i).getNhaCungCap().getTenNCC())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else{
                    if (!nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")) {
                        for (int i=0; i<dsLoc.size(); i++){
                            if(nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                        if(!nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--")){
                            for (int i=0; i<dsLoc.size(); i++){
                                if(nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals(dsLoc.get(i).getNhaCungCap().getTenNCC())){
                                    dsTam.add(dsLoc.get(i));
                                }
                            }
                            dsLoc = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }else if(!nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--")) {
                        for (int i=0; i<dsLoc.size(); i++){
                            if(nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals(dsLoc.get(i).getNhaCungCap().getTenNCC())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
            }
            else if (nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate() != null) {
                Map<String,Object> conditions = new HashMap<>();
                conditions.put("NgayLapPhieu", dateToLocalDate(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate()));
                try {
                    dsLoc = phieuNhapHangImp.timKiem(conditions);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                if (!nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")) {
                    for (int i=0; i<dsLoc.size(); i++){
                        if(nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                    if(!nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--")){
                        for (int i=0; i<dsLoc.size(); i++){
                            if(nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals(dsLoc.get(i).getNhaCungCap().getTenNCC())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }else if(!nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--")) {
                    for (int i=0; i<dsLoc.size(); i++){
                        if(nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals(dsLoc.get(i).getNhaCungCap().getTenNCC())){
                            dsTam.add(dsLoc.get(i));
                        }
                    }
                    dsLoc = dsTam;
                    dsTam = new ArrayList<>();
                }
            }

            DefaultTableModel clearTable = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
            clearTable.setRowCount(0);
            nhapHangUI.getTbDanhSachPheiNhap().setModel(clearTable);
            try {
                DefaultTableModel tmKhachHang = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                for(PhieuNhapHang pn : dsLoc){
                    String row[] = {pn.getMaPhieuNhap(), pn.getNhaCungCap().getTenNCC(), pn.getNgayLapPhieu().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), pn.getNgayHenGiao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), pn.getTinhTrang().toString()};
                    tmKhachHang.addRow(row);
                }
            } catch (Exception ee) {
                throw new RuntimeException(ee);
            }
        }

        /*XÁC NHẬP THÊM PBSP*/
        if(op.equals(nhapHangUI.getBtnChonXongPBSP())){
            if(nhapHangUI.getTblChonPBSP().isEditing()){
                nhapHangUI.getTblChonPBSP().getCellEditor().stopCellEditing();
            }
            String maSP = nhapHangUI.getTxtMaSPBangChonPBSP().getText();
            DefaultTableModel tmPBSP = (DefaultTableModel) nhapHangUI.getTblChonPBSP().getModel();
            DefaultTableModel tmSP = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
            int row = nhapHangUI.getTbDanhSachSpTrongGioHang().getSelectedRow();
            nhapHangUI.getTblChonPBSP().setFocusable(false);
            Map<String, String> listCTPBSPChon = new HashMap<>();
            int tong = 0;
            for(int i = 0; i < tmPBSP.getRowCount(); i++){
                if(tmPBSP.getValueAt(i,4).toString().equalsIgnoreCase("true")){
                    listCTPBSPChon.put(tmPBSP.getValueAt(i, 0).toString(), tmPBSP.getValueAt(i, 3).toString());
                    tong += Integer.parseInt(tmPBSP.getValueAt(i,3).toString());
                }
            }
            listSPDaChon.put(maSP, listCTPBSPChon);
            tmSP.setValueAt(tong, row, 4);
            anHienWinPBSP(false);
            if(nhapHangUI.getTbDanhSachSpTrongGioHang().getValueAt(row, 5).equals("0.0")){
                setCursorPositionTable(nhapHangUI.getTbDanhSachSpTrongGioHang(), row, 5);
            }
        }

        /*NUT TRANG THAI PHIEU NHAP*/
        if(op.equals(nhapHangUI.getBtnTrangThaiPhieu())){
            //Thực hiện duyệt phiếu
            if(nhapHangUI.getBtnTrangThaiPhieu().getText().equalsIgnoreCase("Duyệt")){
                nhapHangUI.getBtnTrangThaiPhieu().setText("Nhập vào kho");
                nhapHangUI.getCbkDuyet().setSelected(true);
                int row = nhapHangUI.getTbDanhSachPheiNhap().getSelectedRow();
                DefaultTableModel tmPhieu = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                PhieuNhapHang pnh = layDataPhieuNhap("DANG_CHUYEN");
                try {
                    phieuNhapHangImp.capNhat(pnh);
                    tmPhieu.setValueAt("DANG_CHUYEN", row, 4);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            //Thực hiện nhập kho
            else if (nhapHangUI.getBtnTrangThaiPhieu().getText().equalsIgnoreCase("Nhập vào kho")) {
                nhapHangUI.getBtnTrangThaiPhieu().setVisible(false);
                nhapHangUI.getCbkDangLayHang().setSelected(true);
                nhapHangUI.getCbkHoanThanh().setSelected(true);
                String []columnNames = {"MaPhienBanSP","SoLuong"};
                System.out.println(listMaCTPNXem);
                for(Map.Entry<String, String> entry : listMaCTPNXem.entrySet()){
                    System.out.println(entry.getKey());
                    Map<String, Object> listCTPN = new HashMap<>();
                    listCTPN.put("MaChiTietPhieuNhap", entry.getKey());
                    try {
                        List<Map<String, Object>> ctpnhPBSP = chiTietPhieuNhapHangPhienBanSPImp.timKiem(listCTPN,false,columnNames);
                        for(Map<String, Object> entry1 : ctpnhPBSP){
                            ChiTietPhienBanSanPham pbsp = chiTietPhienBanSanPhamImp.timKiem(entry1.get("MaPhienBanSP").toString());
                            System.out.println(pbsp);
                            pbsp.setSoLuong(pbsp.getSoLuong()+Integer.parseInt(entry1.get("SoLuong").toString()));
                            System.out.println(pbsp);
                            chiTietPhienBanSanPhamImp.capNhat(pbsp);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                int row = nhapHangUI.getTbDanhSachPheiNhap().getSelectedRow();
                DefaultTableModel tmPhieu = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                PhieuNhapHang pnh = layDataPhieuNhap("DA_NHAN");
                try {
                    phieuNhapHangImp.capNhat(pnh);
                    tmPhieu.setValueAt("DA_NHAN", row, 4);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    //Phát sinh mã phiếu nhập
    private String layMaPN() {
        String ma = "PNH-";
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        ma = ma+nThem;
        ma = ma+"-"+formatNumber(laysoDuoiMaPN());
        return ma;
    }

    //Hàm lấy số đuôi
    public int laysoDuoiMaPN(){
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("MaPhieuNhap", "%"+nThem+"%");
        List<PhieuNhapHang> phieuNhapHangs;
        try {
            phieuNhapHangs = phieuNhapHangImp.timKiem(conditions);
        } catch (Exception e) {
            return 1;
        }
        if(phieuNhapHangs.size()==0){
            return 1;
        }
        ArrayList<Integer> soHD = new ArrayList<Integer>();
        for (int i = 0; i < phieuNhapHangs.size(); i++){
            int index = phieuNhapHangs.get(i).getMaPhieuNhap().lastIndexOf("-");
            soHD.add(Integer.parseInt(phieuNhapHangs.get(i).getMaPhieuNhap().substring(index+1)));
        }
        int max = 0;
        for(int number : soHD){
            if(number >= max){
                max = number;
            }
        }
        return max+1;
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

    //Phát sinh mã ChiTietPhieuNhap
    public String maCTPN(){
        String ma = "CTPN-";
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        ma = ma+nThem;
        String stt = "0";
        if(!(listMaCTPN.size() ==0)){
            for(Map.Entry<String, String> entry : listMaCTPN.entrySet()){
                stt = entry.getKey();
            }
            int index = stt.lastIndexOf("-");
            stt = stt.substring(index+1);
            int maso =  Integer.parseInt(stt) + 1;
            ma = ma+"-"+ formatNumber(maso);
            return ma;
        }
        ma = ma+"-"+formatNumber(laysoDuoiMaCTPN());
        return ma;
    }

    //Hàm lấy số đuôi
    public int laysoDuoiMaCTPN(){
        String nThem = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("MaChiTietPhieuNhap", "%"+nThem+"%");
        List<ChiTietPhieuNhapHang> ctpnh;
        try {
            ctpnh = chiTietPhieuNhapHangImp.timKiem(conditions);
        } catch (Exception e) {
            return 1;
        }
        if(ctpnh.size()==0){
            return 1;
        }
        ChiTietPhieuNhapHang ctPNH = ctpnh.get(ctpnh.size()-1);
        int soPN = Integer.parseInt(ctPNH.getMaChiTietPhieuNhap().substring(ctPNH.getMaChiTietPhieuNhap().length()-3));
        return soPN+1;
    }


    //Lọc phiếu nhập từ JDateChooser
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        handlePropertyChange(evt);
    }

    private void handlePropertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        String propertyName = evt.getPropertyName();
        nhapHangUI.getjDateChooserTkNgayLapPhieu().setName("NgayLapTK");
        nhapHangUI.getjDateChooserTkNgayHenGiao().setName("NgayHenTK");
        if ("date".equals(propertyName) && source instanceof JDateChooser) {
            JDateChooser dateChooser = (JDateChooser) source;
            String dateChooserName = dateChooser.getName();

            if ("NgayLapTK".equals(dateChooserName) || "NgayHenTK".equals(dateChooserName)) {
                if(nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--") && nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                    if (nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() != null){
                        Map<String, Object> conditions = new HashMap<>();
                        conditions.put("NgayHenGiao", dateToLocalDate(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate()));
                        try {
                            dsLoc = phieuNhapHangImp.timKiem(conditions);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate() != null){
                            for (int i=0; i<dsLoc.size(); i++){
                                if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayLapPhieu()))){
                                    dsTam.add(dsLoc.get(i));
                                }
                            }
                            dsLoc = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate() != null){
                        Map<String, Object> conditions = new HashMap<>();
                        conditions.put("NgayLapPhieu", dateToLocalDate(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate()));
                        try {
                            dsLoc = phieuNhapHangImp.timKiem(conditions);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                        try {
                            dsLoc = phieuNhapHangImp.timKiem();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                else if(!nhapHangUI.getCbTkNhaCungCap().getSelectedItem().equals("--Nhà cung cấp--")) {
                    List<NhaCungCap> ncc;
                    Map<String, Object> conditions = new HashMap<>();
                    conditions.put("TenNCC", nhapHangUI.getCbTkNhaCungCap().getSelectedItem().toString());
                    try {
                        ncc = nhaCungCapImp.timKiem(conditions);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    Map<String, Object> conditions1 = new HashMap<>();
                    conditions1.put("MaNCC", ncc.get(0).getMaNCC());
                    try {
                        dsLoc = phieuNhapHangImp.timKiem(conditions1);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    if(!nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                        for(int i=0; i<dsLoc.size(); i++){
                            if(nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals(dsLoc.get(i).getTinhTrang().toString())){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                        if (nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate() != null){
                            for(int i=0; i<dsLoc.size(); i++){
                                if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayLapPhieu()))){
                                    dsTam.add(dsLoc.get(i));
                                }
                            }
                            dsLoc = dsTam;
                            dsTam = new ArrayList<>();
                            if(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() != null){
                                for (int i=0; i<dsLoc.size(); i++){
                                    if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayHenGiao()))){
                                        dsTam.add(dsLoc.get(i));
                                    }
                                }
                                dsLoc = dsTam;
                                dsTam = new ArrayList<>();
                            }
                        }else if(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() != null){
                            for (int i=0; i<dsLoc.size(); i++){
                                if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayHenGiao()))){
                                    dsTam.add(dsLoc.get(i));
                                }
                            }
                            dsLoc = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate() != null){
                        for(int i=0; i<dsLoc.size(); i++){
                            if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayLapPhieu()))){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                        if(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() != null){
                            for (int i=0; i<dsLoc.size(); i++){
                                if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayHenGiao()))){
                                    dsTam.add(dsLoc.get(i));
                                }
                            }
                            dsLoc = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() != null) {
                        for (int i=0; i<dsLoc.size(); i++){
                            if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayHenGiao()))){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if (!nhapHangUI.getCbTkTinhTrang().getSelectedItem().equals("--Tình trạng--")){
                    Map<String, Object> conditions = new HashMap<>();
                    conditions.put("TinhTrang", nhapHangUI.getCbTkTinhTrang().getSelectedItem().toString());
                    try {
                        dsLoc = phieuNhapHangImp.timKiem(conditions);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate() != null){
                        for(int i=0; i<dsLoc.size(); i++){
                            if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayLapPhieu().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayLapPhieu()))){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                        if(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() != null){
                            for (int i=0; i<dsLoc.size(); i++){
                                if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayHenGiao()))){
                                    dsTam.add(dsLoc.get(i));
                                }
                            }
                            dsLoc = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (nhapHangUI.getjDateChooserTkNgayHenGiao().getDate() != null) {
                        for (int i=0; i<dsLoc.size(); i++){
                            if(dateToLocalDate(nhapHangUI.getjDateChooserTkNgayHenGiao().getDate()).isEqual(ChronoLocalDate.from(dsLoc.get(i).getNgayHenGiao()))){
                                dsTam.add(dsLoc.get(i));
                            }
                        }
                        dsLoc = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }

                DefaultTableModel clearTable = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                clearTable.setRowCount(0);
                nhapHangUI.getTbDanhSachPheiNhap().setModel(clearTable);
                try {
                    DefaultTableModel tmKhachHang = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                    for(PhieuNhapHang pn : dsLoc){
                        String row[] = {pn.getMaPhieuNhap(), pn.getNhaCungCap().getTenNCC(), pn.getNgayLapPhieu().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), pn.getNgayHenGiao().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), pn.getTinhTrang().toString()};
                        tmKhachHang.addRow(row);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //Chuyển đổi kiểu Date thành LocalDate
    public LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    //Đóng hoặc mở các tương tác với phần tìm kiếm
    private void tuongTacTimKiem(boolean b) {
        nhapHangUI.getCbTkNhaCungCap().setEnabled(b);
        nhapHangUI.getCbTkTinhTrang().setEnabled(b);
        nhapHangUI.getjDateChooserTkNgayLapPhieu().setEnabled(b);
        nhapHangUI.getjDateChooserTkNgayHenGiao().setEnabled(b);
        nhapHangUI.getTxtTkPhieuNhap().setEnabled(b);
    }

    //Đóng hoặc mở tương tác với phần thông tin
    private void tuongTac(boolean b) {
        nhapHangUI.getTxtNhaCungCap().setEnabled(b);

        nhapHangUI.getTxtMaPhieuNhap().setEnabled(b);
        nhapHangUI.getjDateChooserNgayHenGiao().setEnabled(b);
        nhapHangUI.getTxtTimKiemSanPhamNhap().setEnabled(b);
        nhapHangUI.getTxtGhiChu().setEnabled(b);
    }

    private void anHienWinPBSP(boolean o){
        nhapHangUI.getWinPBSP().setLocationRelativeTo(null);
        nhapHangUI.getWinPBSP().setVisible(o);
    }

    //Ham xoa trang dữ liệu
    public void xoaTrangAll(){
        nhapHangUI.getTxtEmailCUaHang().setText("");
        nhapHangUI.getTxtDienThoaiCuaHang().setText("");
        nhapHangUI.getTxtDiaChiCuaHang().setText("");
        nhapHangUI.getBtnTrangThaiPhieu().setVisible(false);
        nhapHangUI.getTxtNhaCungCap().setText("");
        nhapHangUI.getTxtGhiChu().setText("");
        nhapHangUI.getTxtEmailNhaCungCap().setText("");
        nhapHangUI.getTxtDiaChi().setText("");
        nhapHangUI.getTxtMaPhieuNhap().setText("");
        nhapHangUI.getTxtNgayLapPhieu().setText("");
        nhapHangUI.getjDateChooserNgayHenGiao().setDate(null);
        nhapHangUI.getTxtTongTienSanPham().setText("");
        nhapHangUI.getTxtTinhTrangPhieuNhap().setText("");
        nhapHangUI.getCbkDangLayHang().setSelected(false);
        nhapHangUI.getCbkDuyet().setSelected(false);
        nhapHangUI.getCbkHoanThanh().setSelected(false);
        DefaultTableModel clearTable = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
        clearTable.setRowCount(0);
        nhapHangUI.getTbDanhSachSpTrongGioHang().setModel(clearTable);
    }

    //Load combobox tìm kiếm
    public void loadComboBoxPhanTimKiem(){
        //Tình trạng
        TinhTrangNhapHang[] dsTinhTrang = TinhTrangNhapHang.values();
        String[] itemsTinhTrang = new String[dsTinhTrang.length + 1];
        itemsTinhTrang[0] = "--Tình trạng--";
        for (int i = 0; i < dsTinhTrang.length; i++) {
            itemsTinhTrang[i + 1] = dsTinhTrang[i].toString();
        }
        DefaultComboBoxModel<String> tinhTrangCb = new DefaultComboBoxModel<>(itemsTinhTrang);
        nhapHangUI.getCbTkTinhTrang().setModel(tinhTrangCb);

        //Nhà cung cấp
        List<NhaCungCap> ncc;
        try {
            ncc = nhaCungCapImp.timKiem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String[] itemsNCC = new String[ncc.size() + 1];
        itemsNCC[0] = "--Nhà cung cấp--";
        for(int i=0; i<ncc.size(); i++){
            itemsNCC[i+1] = ncc.get(i).getTenNCC().toString();
        }
        DefaultComboBoxModel<String> nccCb = new DefaultComboBoxModel<>(itemsNCC);
        nhapHangUI.getCbTkNhaCungCap().setModel(nccCb);
    }

    //Lấy data để thêm
    private PhieuNhapHang layDataPhieuNhap(String tinhTrang) {

        PhieuNhapHang phieuNhapHang;
        String mapn = nhapHangUI.getTxtMaPhieuNhap().getText();
        List<NhaCungCap> nhaCC = new ArrayList<>();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("TenNCC", nhapHangUI.getTxtNhaCungCap().getText());
        try {
            nhaCC = nhaCungCapImp.timKiem(conditions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LocalDateTime ngayLapPhieu = LocalDateTime.parse(nhapHangUI.getTxtNgayLapPhieu().getText()+" 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime ngayHenGiao = LocalDateTime.of((nhapHangUI.getjDateChooserNgayHenGiao().getDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.of(0, 0, 0));

        String ghiChu = nhapHangUI.getTxtGhiChu().getText();

        TinhTrangNhapHang tt = TinhTrangNhapHang.layGiaTri(tinhTrang);
        phieuNhapHang = new PhieuNhapHang(mapn,nhaCC.get(0),ngayLapPhieu,ngayHenGiao,ghiChu,tt);
        return phieuNhapHang;
    }


    @Override
    public void onDelete(int row) {
        if(nhapHangUI.getTbDanhSachSpTrongGioHang().isEditing()){
            nhapHangUI.getTbDanhSachSpTrongGioHang().getCellEditor().stopCellEditing();
        }
        if(!(trangThaiNutThemPN==0)){
            if(trangThaiNutThemPN==2){
                DefaultTableModel tmGioHang = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
                if(tmGioHang.getRowCount()>1){
                    tmGioHang.removeRow(row);
                    for(int i = 0; i < tmGioHang.getRowCount(); i++){
                        tmGioHang.setValueAt((i+1), i, 0);
                    }
                    nhapHangUI.getTxtTongTienSanPham().setText(tongTienSanPhamNhap());
                }else {
                    JOptionPane.showMessageDialog(nhapHangUI, "Bạn không thể xóa sản phẩm cuối cùng trong phiếu nhập");
                }
            }else{
                DefaultTableModel tmGioHang = (DefaultTableModel) nhapHangUI.getTbDanhSachSpTrongGioHang().getModel();
                tmGioHang.removeRow(row);
                for(int i = 0; i < tmGioHang.getRowCount(); i++){
                    tmGioHang.setValueAt((i+1), i, 0);
                }
                nhapHangUI.getTxtTongTienSanPham().setText(tongTienSanPhamNhap());
            }

        } else{
            JOptionPane.showMessageDialog(nhapHangUI, "Bạn không thể xóa sản phẩm trong chế độ xem");

        }

    }

    @Override
    public void onDuyet(int row) {
        if(nhapHangUI.getTbDanhSachPheiNhap().isEditing()){
            nhapHangUI.getTbDanhSachPheiNhap().getCellEditor().stopCellEditing();
        }
        String tt = nhapHangUI.getTbDanhSachPheiNhap().getModel().getValueAt(row, 4).toString();
        if(tt.equals("DA_NHAN")){
            DefaultTableModel tmHD = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
            String maPNH =  nhapHangUI.getTbDanhSachPheiNhap().getValueAt(row, 0).toString();
            tmHD.removeRow(row);
            nhapHangUI.getTbDanhSachPheiNhap().getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender1());
            nhapHangUI.getTbDanhSachPheiNhap().getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor1(this));
            pnA1.getCmdDuyet().setVisible(false);
            String []row1 = {tmHD.getValueAt(row, 0).toString(), tmHD.getValueAt(row,1).toString(), tmHD.getValueAt(row,2).toString(), tmHD.getValueAt(row, 3).toString(), tmHD.getValueAt(row, 4).toString(),};
            tmHD.addRow(row1);
        }

    }

    @Override
    public void onHuy(int row) {

    }

    @Override
    public void onChon(int row) {
        if(nhapHangUI.getTbDanhSachSpTrongGioHang().isEditing()){
            nhapHangUI.getTbDanhSachSpTrongGioHang().getCellEditor().stopCellEditing();
        }
        String maSP = nhapHangUI.getTbDanhSachSpTrongGioHang().getModel().getValueAt(row, 1).toString();

            if(trangThaiNutThemPN==0){
                nhapHangUI.getTxtMaPhieuNhap().getText();
                DefaultTableModel tmPNH = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                int rowSelected = nhapHangUI.getTbDanhSachPheiNhap().getSelectedRow();
                String maPN = tmPNH.getValueAt(rowSelected, 0).toString();
                Optional<ChiTietPhieuNhapHang> ctpnh;
                try {
                    ctpnh = chiTietPhieuNhapHangImp.timKiem(maPN, maSP);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                List<ChiTietPhieuNhapHangPhienBanSP> ct;
                Map<String, Object> conditons = new HashMap<>();
                conditons.put("MaChiTietPhieuNhap", ctpnh.get().getMaChiTietPhieuNhap());
                try {
                    ct = chiTietPhieuNhapHangPhienBanSPImp.timKiem(conditons);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                DefaultTableModel tmPBSP = (DefaultTableModel) nhapHangUI.getTblChonPBSP().getModel();
                tmPBSP.setRowCount(0);
                nhapHangUI.getTblChonPBSP().setModel(tmPBSP);
                for(ChiTietPhieuNhapHangPhienBanSP ctPNHPhienBanSP : ct){
//                    tmPBSP.addRow(new Object[]{ctPNHPhienBanSP.getChiTietPhienBanSanPham().getMaPhienBanSP(), ctPNHPhienBanSP.getChiTietPhienBanSanPham().getMauSac(), ctPNHPhienBanSP.getChiTietPhienBanSanPham().getKichThuoc(), ctPNHPhienBanSP.getSoLuongNhap(), true});
                }
                nhapHangUI.getTxtMaSPBangChonPBSP().setText(maSP);
                nhapHangUI.getTblChonPBSP().setEnabled(false);
                anHienWinPBSP(true);

            }
            else if(trangThaiNutThemPN == 1){
                nhapHangUI.getTxtMaSPBangChonPBSP().setText(maSP);
                loadPBSP(maSP);
                nhapHangUI.getTblChonPBSP().setEnabled(true);
                anHienWinPBSP(true);
            }else if(trangThaiNutThemPN==2){
                nhapHangUI.getTxtMaPhieuNhap().getText();
                DefaultTableModel tmPNH = (DefaultTableModel) nhapHangUI.getTbDanhSachPheiNhap().getModel();
                int rowSelected = nhapHangUI.getTbDanhSachPheiNhap().getSelectedRow();
                String maPN = tmPNH.getValueAt(rowSelected, 0).toString();
                Optional<ChiTietPhieuNhapHang> ctpnh;
                try {
                    ctpnh = chiTietPhieuNhapHangImp.timKiem(maPN, maSP);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                List<ChiTietPhieuNhapHangPhienBanSP> ct;
                Map<String, Object> conditons = new HashMap<>();
                conditons.put("MaChiTietPhieuNhap", ctpnh.get().getMaChiTietPhieuNhap());
                try {
                    ct = chiTietPhieuNhapHangPhienBanSPImp.timKiem(conditons);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                DefaultTableModel tmPBSP = (DefaultTableModel) nhapHangUI.getTblChonPBSP().getModel();
                tmPBSP.setRowCount(0);
                nhapHangUI.getTblChonPBSP().setModel(tmPBSP);
                for(ChiTietPhieuNhapHangPhienBanSP ctPNHPhienBanSP : ct){
//                    tmPBSP.addRow(new Object[]{ctPNHPhienBanSP.getChiTietPhienBanSanPham().getMaPhienBanSP(), ctPNHPhienBanSP.getChiTietPhienBanSanPham().getMauSac(), ctPNHPhienBanSP.getChiTietPhienBanSanPham().getKichThuoc(), ctPNHPhienBanSP.getSoLuongNhap(), true});
                }
                nhapHangUI.getTxtMaSPBangChonPBSP().setText(maSP);
                nhapHangUI.getTblChonPBSP().setEnabled(true);
                anHienWinPBSP(true);
            }
    }
}
