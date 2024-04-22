package dev.skyherobrine.app.controllers.dashboardui.product;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import dev.skyherobrine.app.daos.product.*;
import dev.skyherobrine.app.daos.sale.ThueImp;
import dev.skyherobrine.app.entities.product.*;
import dev.skyherobrine.app.entities.sale.Thue;
import dev.skyherobrine.app.enums.*;
import dev.skyherobrine.app.views.dashboard.component.QuanLySanPham;
import dev.skyherobrine.app.views.dashboard.component.nutXoaDongTb.TableActionEvent;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class ProductController implements ActionListener, MouseListener, KeyListener, TableActionEvent, TableModelListener {
    private final QuanLySanPham sanPhamUI;
    private  List<SanPham> dsSanPham;
    private final SanPhamImp sanPhamImp;
    private List<LoaiSanPham> dsLoaiSanPham;
    private final LoaiSanPhamImp loaiSanPhamImp;
    private final ThuongHieuImp thuongHieuImp;
    private List<ThuongHieu> dsThuongHieu;
    private List<DanhMucSanPham> dsDanhMucSanPham;
    private final DanhMucSanPhamImp danhMucSanPhamImp;
    private final ChiTietPhienBanSanPhamImp chiTietPhienBanSanPhamImp;
    private final ThueImp thueImp;

    private static int trangThaiNutXoa = 0;
    private static int trangThaiNutThem = 0;
    private static int trangThaiNutSua = 0;
    private static int trangThaiNutThemPBSP = 0;

    private static String fileAnh = "";
    private final ArrayList<ChiTietPhienBanSanPham> listCTPBSP = new ArrayList<>();
    private String maLoai = "";

    public ProductController(QuanLySanPham sanPhamUI) {
        try {
            this.sanPhamUI = sanPhamUI;
            sanPhamImp = new SanPhamImp();
            loaiSanPhamImp = new LoaiSanPhamImp();
            thuongHieuImp = new ThuongHieuImp();
            danhMucSanPhamImp = new DanhMucSanPhamImp();
            chiTietPhienBanSanPhamImp = new ChiTietPhienBanSanPhamImp();
            thueImp = new ThueImp();
            loadDsSanPham();
            loadComboBoxPhanThongTinSP();
            loadComboBoxPhanTimKiem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//  Load data từ CSDL lên table
    public void loadDsSanPham() {
        DefaultTableModel clearTable = (DefaultTableModel) sanPhamUI.getTbDanhSachSanPham().getModel();

        clearTable.setRowCount(0);
        sanPhamUI.getTbDanhSachSanPham().setModel(clearTable);
        try {
            dsSanPham = sanPhamImp.timKiem();

            DefaultTableModel tmSanPham = (DefaultTableModel) sanPhamUI.getTbDanhSachSanPham().getModel();
            for(SanPham sp : dsSanPham){
                String[] row = {sp.getMaSP(), sp.getTenSP(), sp.getLoaiSanPham().getDanhMucSanPham().getTenDM(), sp.getLoaiSanPham().getTenLoai(), sp.getPhongCachMac()+"", sp.getThuongHieu().getTenTH(),sp.getDoTuoi().toString() , sp.getTinhTrang()+""};
                tmSanPham.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        trangThaiNutThem = 0;
        trangThaiNutSua = 0;
        trangThaiNutXoa = 0;
    }

    private List<SanPham> dsTim;
    private List<SanPham> dsTam = new ArrayList<>();

    @Override
    public void actionPerformed(ActionEvent e){
        Object op = e.getSource();
        /*NÚT THÊM*/
        if(op.equals(sanPhamUI.getButtonThem())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ thêm sản phẩm
            if (trangThaiNutThem==0) {
                sanPhamUI.getButtonThem().setText("Xác nhận thêm");
                sanPhamUI.getButtonSua().setText("Xóa trắng");
                sanPhamUI.getButtonXoa().setText("Thoát thêm");
                trangThaiNutXoa = 1;
                trangThaiNutThem = 1;
                trangThaiNutSua = 1;

                //Mở tương tác với thông tin
                tuongTac(true);
                tuongTacTimKiem(false);
                sanPhamUI.getCbKieuNguoiMac().setEnabled(false);

                //Xóa trắng dữ liệu
                xoaTrangAll();
                //load sẵn tình trang còn bán
                sanPhamUI.getCbTinhTrang().setSelectedItem("CON_BAN");
            }
            // Thực hiện chức năng nghiệp vụ thêm sản phẩm
            else if(trangThaiNutThem==1) {
                SanPham sp = layDataThem();
                if(sp != null){
                    if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thêm sản phẩm mới", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
                        try {
                            if(sanPhamImp.them(sp)){

                                if(listCTPBSP.size()>0){
                                    for(ChiTietPhienBanSanPham ct : listCTPBSP){
                                        chiTietPhienBanSanPhamImp.them(ct);
                                        String data = ct.getMaPhienBanSP();
                                        String path = "src/main/resources/MaQRSanPham/"+ct.getMaPhienBanSP()+".jpg";

                                        BitMatrix matrix = new MultiFormatWriter()
                                                .encode(data, BarcodeFormat.QR_CODE, 500, 500);

                                        MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(path));
                                    }
                                }
                                loadDsSanPham();
                                xoaTrangAll();
                                JOptionPane.showMessageDialog(null, "Thêm thành công!");
                                trangThaiNutThem = 0;
                                trangThaiNutXoa = 0;
                                listCTPBSP.clear();
                            }else{
                                JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
            //Thực hiện chức năng nghiệp vụ sửa sản phẩm
            else if(trangThaiNutThem==2){
                if (sanPhamUI.getTxtMaSanPham().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần sửa!");
                }else {
                    SanPham spSua = layDataSua();
                    if ((JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn sửa sản phẩm có mã " +spSua.getMaSP()+" không?", "Lựa chọn", JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION){
                        try {
                            if(sanPhamImp.capNhat(spSua)){
                                if(listCTPBSP.size()>0){
                                    for(ChiTietPhienBanSanPham ct : listCTPBSP){
                                        chiTietPhienBanSanPhamImp.capNhat(ct);
                                    }
                                }
                                loadDsSanPham();
                                xoaTrangAll();
                                JOptionPane.showMessageDialog(null, "Sửa thành công!");
                                trangThaiNutThem = 0;
                                trangThaiNutXoa = 0;
                                sanPhamUI.getButtonThem().setText("Thêm sản phẩm");
                                sanPhamUI.getButtonSua().setText("Sửa sản phẩm");
                                sanPhamUI.getButtonXoa().setText("Xóa sản phẩm");
                                listCTPBSP.clear();
                            }else{
                                JOptionPane.showMessageDialog(null, "Sửa thất bại!");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                trangThaiNutXoa = 1;
            }
        }
        /*NÚT PBSP*/
        if (e.getSource().equals(sanPhamUI.getBtnThemPBSP())) {
            trangThaiNutThemPBSP = 0;
            if(trangThaiNutThem==0){
                if(sanPhamUI.getTxtMaSanPham().getText().equalsIgnoreCase("")){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xem phiên bản!");
                    return;
                }
                sanPhamUI.getTxtDialogMaSanPham().setEnabled(false);

                loadTTPBSP();
                loadComboBoxPhanThongTinPhienBanSP();
                xoaTrangPBSP(false);
                anHienWinPBSP(true);
            }else if(trangThaiNutThem==1){
                SanPham sp = layDataThem();
                if(sp==null) {
                    return;
                }

                sanPhamUI.getTxtDialogMaSanPham().setText(sanPhamUI.getTxtMaSanPham().getText());
                loadComboBoxPhanThongTinPhienBanSP();
                xoaTrangPBSP(false);
                anHienWinPBSP(true);
            }else{
                sanPhamUI.getTxtDialogMaSanPham().setEnabled(false);
                loadTTPBSP();
                loadComboBoxPhanThongTinPhienBanSP();
                xoaTrangPBSP(false);
                anHienWinPBSP(true);
            }

        }
        /*NÚT SỬA*/
        if(op.equals(sanPhamUI.getButtonSua())){
            // Thực hiện biến đổi các nút thành nút chức năng của nghiệp vụ sửa sản phẩm
            if (trangThaiNutSua==0) {
                //Mở tương tác với thông tin
                tuongTac(true);

                sanPhamUI.getButtonThem().setText("Xác nhận sửa");
                sanPhamUI.getButtonSua().setText("Xóa trắng");
                sanPhamUI.getButtonXoa().setText("Thoát sửa");
                trangThaiNutThem = 2;
                trangThaiNutSua = 1;
                trangThaiNutXoa = 1;
            }
            // Thực hiện xóa trắng dữ liệu ở nghiệp vụ sửa thông tín sản phẩm
            else if(trangThaiNutSua==1) {
                xoaTrangSua();
            }
        }
        if(e.getSource().equals(sanPhamUI.getCbLoai()) && trangThaiNutThem == 1){
            if(!(sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("--Select--"))){
                if(sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo sơ mi nam")||sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo sơ mi nữ")) {
                    maLoai = "ASM";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nam");
                }else if(sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo thun nam")||sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo thun nữ")){
                    maLoai = "ATN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nam");
                }else if(sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo khoác nam")||sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo khoác nam")) {
                    maLoai = "AKN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nam");
                }else if(sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo Vest")) {
                    maLoai = "AV";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nam");
                }else if(sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Quần jeans")) {
                    maLoai = "QJ";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nam");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Quần short")) {
                    maLoai = "QS";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nam");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Đầm")) {
                    maLoai = "DM";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nữ");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Váy")) {
                    maLoai = "VY";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nữ");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo len")) {
                    maLoai = "AL";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nữ");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Chân váy")) {
                    maLoai = "CV";
                    sanPhamUI.getDanhMuc().setSelectedItem("Quần áo nữ");
                }  else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Quần áo trẻ em")) {
                    maLoai = "QATE";
                    sanPhamUI.getDanhMuc().setSelectedItem("Trang phục trẻ em");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Giày trẻ em")) {
                    maLoai = "GTE";
                    sanPhamUI.getDanhMuc().setSelectedItem("Trang phục trẻ em");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Đồ lót trẻ em")) {
                    maLoai = "DLTE";
                    sanPhamUI.getDanhMuc().setSelectedItem("Trang phục trẻ em");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo lót nam")) {
                    maLoai = "ALN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Đồ lót");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Quần lót nam")) {
                    maLoai = "QLN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Đồ lót");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Áo ngực")) {
                    maLoai = "ANG";
                    sanPhamUI.getDanhMuc().setSelectedItem("Đồ lót");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Quần lót nữ")) {
                    maLoai = "QLN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Đồ lót");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Nón")) {
                    maLoai = "N";
                    sanPhamUI.getDanhMuc().setSelectedItem("Phụ kiên");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Cà vạt")) {
                    maLoai = "CV";
                    sanPhamUI.getDanhMuc().setSelectedItem("Phụ kiên");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Thắt lưng")) {
                    maLoai = "TL";
                    sanPhamUI.getDanhMuc().setSelectedItem("Phụ kiên");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Tất")) {
                    maLoai = "T";
                    sanPhamUI.getDanhMuc().setSelectedItem("Phụ kiên");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Quần áo thể thao")) {
                    maLoai = "QATT";
                    sanPhamUI.getDanhMuc().setSelectedItem("Đồ thể thao");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Giày thể thao")) {
                    maLoai = "GTT";
                    sanPhamUI.getDanhMuc().setSelectedItem("Đồ thể thao");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Balo thể thao")) {
                    maLoai = "BTT";
                    sanPhamUI.getDanhMuc().setSelectedItem("Đồ thể thao");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Dụng cụ thể thao")) {
                    maLoai = "DCTT";
                    sanPhamUI.getDanhMuc().setSelectedItem("Đồ thể thao");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Giày cao gót")) {
                    maLoai = "GCG";
                    sanPhamUI.getDanhMuc().setSelectedItem("Giày");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Giày sandal")) {
                    maLoai = "GS";
                    sanPhamUI.getDanhMuc().setSelectedItem("Giày");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Giày sneakers")) {
                    maLoai = "GSN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Giày");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Giày thời trang nam và nữ")) {
                    maLoai = "GTTNN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Giày");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Dép đi trong nhà")) {
                    maLoai = "DDTN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Dép");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Dép sục cross")) {
                    maLoai = "DSC";
                    sanPhamUI.getDanhMuc().setSelectedItem("Dép");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Dép kẹp nam, nữ")) {
                    maLoai = "DKN";
                    sanPhamUI.getDanhMuc().setSelectedItem("Dép");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Dép lê")) {
                    maLoai = "DL";
                    sanPhamUI.getDanhMuc().setSelectedItem("Dép");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Nhẫn")) {
                    maLoai = "N";
                    sanPhamUI.getDanhMuc().setSelectedItem("Trang sức");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Dây chuyền")) {
                    maLoai = "DC";
                    sanPhamUI.getDanhMuc().setSelectedItem("Trang sức");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Vòng tay")) {
                    maLoai = "VT";
                    sanPhamUI.getDanhMuc().setSelectedItem("Trang sức");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Bông tai")) {
                    maLoai = "BT";
                    sanPhamUI.getDanhMuc().setSelectedItem("Trang sức");
                } else if (sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("Đồng hồ")) {
                    maLoai = "DH";
                    sanPhamUI.getDanhMuc().setSelectedItem("Trang sức");
                }
                sanPhamUI.getCbKieuNguoiMac().setEnabled(true);

            }
        }
        if(e.getSource().equals(sanPhamUI.getCbKieuNguoiMac())&&trangThaiNutThem==1){
            if(!sanPhamUI.getCbKieuNguoiMac().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
                if(sanPhamUI.getCbKieuNguoiMac().getSelectedItem().toString().equalsIgnoreCase("NAM")) {
                    maLoai+="1";
                }else if(sanPhamUI.getCbKieuNguoiMac().getSelectedItem().toString().equalsIgnoreCase("NU")) {
                    maLoai += "0";
                }else{
                    maLoai += "2";
                }
                String nl = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                maLoai = maLoai+nl;
                maLoai = maLoai+laymaSP();
                sanPhamUI.getTxtMaSanPham().setText(maLoai);
            }
        }
        /*NÚT THÊM PHIÊN BẢN SẢN PHẨM*/
        if (e.getSource().equals(sanPhamUI.getBtnDialogThemXuong())) {
            if(trangThaiNutThem == 0){
                if(trangThaiNutThemPBSP==0) {
                    sanPhamUI.getTbDialogDanhSachCacSanPham().clearSelection();
                    xoaTrangPBSP(true);
                    trangThaiNutThemPBSP = 1;
                    sanPhamUI.getBtnDialogThemXuong().setText("Xác nhận thêm");
                }else{
                    if(sanPhamUI.getCbDialogMauSac().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn màu sắc!");
                        return;
                    }
                    if(sanPhamUI.getTxtDialogSoLuong().getText().equalsIgnoreCase("")){
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng!");
                        return;
                    }
                    if(sanPhamUI.getCbDialogKichThuoc().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn kích thước!");
                        return;
                    }
                    if(fileAnh.equalsIgnoreCase("")){
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn hình ảnh!");
                        return;
                    }
                    if(!sanPhamUI.getTxtDialogSoLuong().getText().matches("\\d+")){
                        if(Integer.parseInt(sanPhamUI.getTxtDialogSoLuong().getText())<0){
                            JOptionPane.showMessageDialog(null, "Số lượng phải là số dương!");
                            return;
                        }
                        JOptionPane.showMessageDialog(null, "Số lượng phải là số!");
                        return;
                    }
                    int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thêm phiên bản sản phẩm này không?", "Lựa chọn", JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION){
                        try {
                            String maSP = sanPhamUI.getTxtDialogMaSanPham().getText();
                            String mauSac = sanPhamUI.getCbDialogMauSac().getSelectedItem().toString();
                            String kichThuoc = sanPhamUI.getCbDialogKichThuoc().getSelectedItem().toString();
                            String maPhienBan = phatSinhMaPBSP(maSP, mauSac, kichThuoc);
                            ChiTietPhienBanSanPham chiTietPhienBanSanPham = new ChiTietPhienBanSanPham(maPhienBan, sanPhamImp.timKiem(maSP), MauSac.layGiaTri(mauSac), kichThuoc, Integer.parseInt(sanPhamUI.getTxtDialogSoLuong().getText()), fileAnh);
                            if(chiTietPhienBanSanPhamImp.them(chiTietPhienBanSanPham)){
                                JOptionPane.showMessageDialog(null, "Thêm thành công!");
                                loadTTPBSP();
                                xoaTrangPBSP(false);
                                trangThaiNutThemPBSP = 0;
                                sanPhamUI.getBtnDialogThemXuong().setText("Thêm xuống");
                                loadTTPBSP();

                            }else{
                                JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }else if(trangThaiNutThem == 1){
                SanPham sp = layDataThem();
                if(trangThaiNutThemPBSP==0) {
                    xoaTrangPBSP(true);
                    trangThaiNutThemPBSP = 1;
                    sanPhamUI.getBtnDialogThemXuong().setText("Xác nhận thêm");
                }else {
                    if (sanPhamUI.getCbDialogMauSac().getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn màu sắc!");
                        return;
                    }
                    if (sanPhamUI.getTxtDialogSoLuong().getText().equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng!");
                        return;
                    }
                    if (sanPhamUI.getCbDialogKichThuoc().getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn kích thước!");
                        return;
                    }
                    if (fileAnh.equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Vui lòng chọn hình ảnh!");
                        return;
                    }
                    if (!sanPhamUI.getTxtDialogSoLuong().getText().matches("\\d+")) {
                        if (Integer.parseInt(sanPhamUI.getTxtDialogSoLuong().getText()) < 0) {
                            JOptionPane.showMessageDialog(null, "Số lượng phải là số dương!");
                            return;
                        }
                        JOptionPane.showMessageDialog(null, "Số lượng phải là số!");
                        return;
                    }
                    int result = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thêm phiên bản sản phẩm này không?", "Lựa chọn", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            DefaultTableModel tmPBSP = (DefaultTableModel) sanPhamUI.getTbDialogDanhSachCacSanPham().getModel();
                            String maSP = sanPhamUI.getTxtDialogMaSanPham().getText();
                            String mauSac = sanPhamUI.getCbDialogMauSac().getSelectedItem().toString();
                            String kichThuoc = sanPhamUI.getCbDialogKichThuoc().getSelectedItem().toString();
                            String maPhienBan = phatSinhMaPBSP(maSP, mauSac, kichThuoc);
                            ChiTietPhienBanSanPham chiTietPhienBanSanPham = new ChiTietPhienBanSanPham(maPhienBan, sp, MauSac.layGiaTri(mauSac), kichThuoc, Integer.parseInt(sanPhamUI.getTxtDialogSoLuong().getText()), fileAnh);
                            listCTPBSP.add(chiTietPhienBanSanPham);
                            tmPBSP.addRow(new String[]{mauSac, kichThuoc, sanPhamUI.getTxtDialogSoLuong().getText(), null});
                            trangThaiNutThemPBSP = 0;
                            sanPhamUI.getBtnDialogThemXuong().setText("Thêm xuống");
                            xoaTrangPBSP(false);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }

        }
        if(op.equals(sanPhamUI.getBtnDialogXong())){

            anHienWinPBSP(false);
        }
        /*NÚT XÓA*/
        if (op == sanPhamUI.getButtonXoa()) {
            // Thực hiện chức năng nghiệp vụ xóa sản phẩm
            if (trangThaiNutXoa==0) {
                if (sanPhamUI.getTxtMaSanPham().getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xóa!");
                } else {
                    String ma = sanPhamUI.getTxtMaSanPham().getText();
                    if ((JOptionPane.showConfirmDialog(null,
                            "Bạn có chắc muốn ngừng bán sản phẩm có mã " + sanPhamUI.getTxtMaSanPham().getText() + " không?", "Lựa chọn",
                            JOptionPane.YES_NO_OPTION)) == JOptionPane.YES_OPTION) {
                        try {
                            if (sanPhamImp.xoa(ma)){
                                loadDsSanPham();
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
            // Thực hiện trả các nút về giao diện quản lý sản phẩm
            else if(trangThaiNutXoa==1) {
                tuongTac(false);
                tuongTacTimKiem(true);
                xoaTrangAll();
                sanPhamUI.getButtonThem().setText("Thêm sản phẩm");
                sanPhamUI.getButtonSua().setText("Sửa sản phẩm");
                sanPhamUI.getButtonXoa().setText("Xóa sản phẩm");
                trangThaiNutXoa = 0;
                trangThaiNutThem = 0;
                trangThaiNutSua = 0;
            }
        }
        /*LỌC SẢN PHẨM*/
        if (op.equals(sanPhamUI.getCbTkDanhMuc()) ||
                op.equals(sanPhamUI.getCbTkLoaiSanPham()) || op.equals(sanPhamUI.getCbTkTinhTrang()) ||
                op.equals(sanPhamUI.getCbTkThuongHieu()) || op.equals(sanPhamUI.getCbTkDoTuoi()) ||
                op.equals(sanPhamUI.getCbTkPhongCachMac())){
            dsTim = new ArrayList<>();
            List<DanhMucSanPham> dsDanhMucTim;
            List<LoaiSanPham> dsLoaiTim;
            List<ThuongHieu> dsThuongHieuTim;
            //Lọc tuần tự từ trái sang phải
            if(!sanPhamUI.getCbTkDanhMuc().getSelectedItem().toString().equals("--Danh mục--")){
                Map<String, Object> conditionsDanhMuc = new HashMap<>();
                conditionsDanhMuc.put("tenDM", sanPhamUI.getCbTkDanhMuc().getSelectedItem().toString());
                try {
                    dsDanhMucTim = danhMucSanPhamImp.timKiem(conditionsDanhMuc);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                //
                Map<String, Object> conditionsLoai = new HashMap<>();
                conditionsLoai.put("danhMucSanPham.maDM", dsDanhMucTim.get(0).getMaDM());
                try {
                    dsLoaiTim = loaiSanPhamImp.timKiem(conditionsLoai);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                //
                for(int i=0; i<dsLoaiTim.size(); i++){
                    Map<String, Object> conditionsSp = new HashMap<>();
                    conditionsSp.put("loaiSanPham.maLoai", dsLoaiTim.get(i).getMaLoai());
                    try {
                        dsTim.addAll(sanPhamImp.timKiem(conditionsSp));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if(!sanPhamUI.getCbTkLoaiSanPham().getSelectedItem().toString().equals("--Loại--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkLoaiSanPham().getSelectedItem().toString().equals(dsTim.get(i).getLoaiSanPham().getTenLoai())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if (!sanPhamUI.getCbTkTinhTrang().getSelectedItem().toString().equals("--Tình trạng--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkTinhTrang().getSelectedItem().toString().equals(dsTim.get(i).getTinhTrang().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals("--Thương hiệu--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals(dsTim.get(i).getThuongHieu().getTenTH())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                            if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                                for(int i=0; i<dsTim.size(); i++){
                                    if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                        dsTam.add(dsTim.get(i));
                                    }
                                }
                                dsTim = dsTam;
                                dsTam = new ArrayList<>();
                                if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                                    for(int i=0; i<dsTim.size(); i++){
                                        if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                            dsTam.add(dsTim.get(i));
                                        }
                                    }
                                    dsTim = dsTam;
                                    dsTam = new ArrayList<>();
                                }
                            }
                            else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                                for(int i=0; i<dsTim.size(); i++){
                                    if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                        dsTam.add(dsTim.get(i));
                                    }
                                }
                                dsTim = dsTam;
                                dsTam = new ArrayList<>();
                            }
                        }
                        else if (!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")) {
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                            if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                                for(int i=0; i<dsTim.size(); i++){
                                    if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                        dsTam.add(dsTim.get(i));
                                    }
                                }
                                dsTim = dsTam;
                                dsTam = new ArrayList<>();
                            }
                        }
                        else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (!sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals("--Thương hiệu--")) {
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals(dsTim.get(i).getThuongHieu().getTenTH())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                            if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                                for(int i=0; i<dsTim.size(); i++){
                                    if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                        dsTam.add(dsTim.get(i));
                                    }
                                }
                                dsTim = dsTam;
                                dsTam = new ArrayList<>();
                            }
                        }
                        else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")) {
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!sanPhamUI.getCbTkTinhTrang().getSelectedItem().toString().equals("--Tình trạng--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkTinhTrang().getSelectedItem().toString().equals(dsTim.get(i).getTinhTrang().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals("--Thương hiệu--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals(dsTim.get(i).getThuongHieu().getTenTH())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                            if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                                for(int i=0; i<dsTim.size(); i++){
                                    if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                        dsTam.add(dsTim.get(i));
                                    }
                                }
                                dsTim = dsTam;
                                dsTam = new ArrayList<>();
                            }
                        }
                    }
                    else if (!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")) {
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals("--Thương hiệu--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals(dsTim.get(i).getThuongHieu().getTenTH())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            else if(!sanPhamUI.getCbTkLoaiSanPham().getSelectedItem().toString().equals("--Loại--")){
                Map<String, Object> conditionsLoai = new HashMap<>();
                conditionsLoai.put("tenLoai", sanPhamUI.getCbTkLoaiSanPham().getSelectedItem().toString());
                try {
                    dsLoaiTim = loaiSanPhamImp.timKiem(conditionsLoai);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                //
                for(int i=0; i<dsLoaiTim.size(); i++){
                    Map<String, Object> conditionsSp = new HashMap<>();
                    conditionsSp.put("loaiSanPham.maLoai", dsLoaiTim.get(i).getMaLoai());
                    try {
                        dsTim.addAll(sanPhamImp.timKiem(conditionsSp));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (!sanPhamUI.getCbTkTinhTrang().getSelectedItem().toString().equals("--Tình trạng--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkTinhTrang().getSelectedItem().toString().equals(dsTim.get(i).getTinhTrang().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals("--Thương hiệu--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals(dsTim.get(i).getThuongHieu().getTenTH())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                            if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                                for(int i=0; i<dsTim.size(); i++){
                                    if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                        dsTam.add(dsTim.get(i));
                                    }
                                }
                                dsTim = dsTam;
                                dsTam = new ArrayList<>();
                            }
                        }
                        else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")) {
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals("--Thương hiệu--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals(dsTim.get(i).getThuongHieu().getTenTH())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                    else if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            else if (!sanPhamUI.getCbTkTinhTrang().getSelectedItem().toString().equals("--Tình trạng--")) {
                Map<String, Object> conditionsTinhTrang = new HashMap<>();
                String tt = sanPhamUI.getCbTkTinhTrang().getSelectedItem().toString();
                TinhTrangSanPham value = TinhTrangSanPham.valueOf(tt);
                conditionsTinhTrang.put("tinhTrang", value);
                try {
                    dsTim = sanPhamImp.timKiem(conditionsTinhTrang);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                if(!sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals("--Thương hiệu--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals(dsTim.get(i).getThuongHieu().getTenTH())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                        if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                            for(int i=0; i<dsTim.size(); i++){
                                if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                    dsTam.add(dsTim.get(i));
                                }
                            }
                            dsTim = dsTam;
                            dsTam = new ArrayList<>();
                        }
                    }
                    else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if (!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")) {
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            else if(!sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString().equals("--Thương hiệu--")){
                Map<String, Object> conditionsTenThuongHieu = new HashMap<>();
                conditionsTenThuongHieu.put("tenTH", sanPhamUI.getCbTkThuongHieu().getSelectedItem().toString());
                try {
                    dsThuongHieuTim = thuongHieuImp.timKiem(conditionsTenThuongHieu);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                //
                Map<String, Object> conditionsSanPham = new HashMap<>();
                conditionsSanPham.put("maTH", dsThuongHieuTim.get(0).getMaTH());
                try {
                    dsTim = sanPhamImp.timKiem(conditionsSanPham);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals(dsTim.get(i).getDoTuoi().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                    if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                        for(int i=0; i<dsTim.size(); i++){
                            if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                                dsTam.add(dsTim.get(i));
                            }
                        }
                        dsTim = dsTam;
                        dsTam = new ArrayList<>();
                    }
                }
                else if (!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")) {
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            else if(!sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString().equals("--Độ tuổi--")){
                Map<String, Object> conditionsDoTuoi = new HashMap<>();
                conditionsDoTuoi.put("doTuoi", sanPhamUI.getCbTkDoTuoi().getSelectedItem().toString());
                try {
                    dsTim = sanPhamImp.timKiem(conditionsDoTuoi);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){
                    for(int i=0; i<dsTim.size(); i++){
                        if(sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals(dsTim.get(i).getPhongCachMac().toString())){
                            dsTam.add(dsTim.get(i));
                        }
                    }
                    dsTim = dsTam;
                    dsTam = new ArrayList<>();
                }
            }
            else if(!sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString().equals("--Phong cách--")){

                Map<String, Object> conditionsPhongCach = new HashMap<>();
                conditionsPhongCach.put("phongCachMac", sanPhamUI.getCbTkPhongCachMac().getSelectedItem().toString());
                try {
                    dsTim = sanPhamImp.timKiem(conditionsPhongCach);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                try {
                    dsTim = sanPhamImp.timKiem();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            //Load lọc
            DefaultTableModel clearTable = (DefaultTableModel) sanPhamUI.getTbDanhSachSanPham().getModel();
            clearTable.setRowCount(0);
            sanPhamUI.getTbDanhSachSanPham().setModel(clearTable);

            DefaultTableModel tmSanPham = (DefaultTableModel) sanPhamUI.getTbDanhSachSanPham().getModel();
            for(SanPham sp : dsTim){
                String[] row = {sp.getMaSP(), sp.getTenSP(), sp.getLoaiSanPham().getDanhMucSanPham().getTenDM(), sp.getLoaiSanPham().getTenLoai(), sp.getPhongCachMac()+"", sp.getThuongHieu().getTenTH(),sp.getDoTuoi().toString() , sp.getTinhTrang()+""};
                tmSanPham.addRow(row);
            }
        }

        /*NÚT LẤY ẢNH TỪ MÁY*/
        if(op.equals(sanPhamUI.getBtnDialogAnh())){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh", "jpg", "jpeg", "png"));
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // Lấy tên file và đuôi file
                fileAnh = selectedFile.getName();
                URL path = getClass().getResource("/img/imgSanPham/"+fileAnh);
                if(path==null){
                    path = getClass().getResource("/img/imgSanPham/Image_not_available.png");
                }

                ImageIcon iconGoc = new ImageIcon(path);
                Image anh = iconGoc.getImage();
                Image tinhChinhAnh = anh.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(tinhChinhAnh);
                JLabel picLabel = new JLabel();
                sanPhamUI.getPnDialogAnh().removeAll();
                sanPhamUI.getPnDialogAnh().add(picLabel);
                picLabel.setSize(new Dimension(240,320));
                picLabel.setIcon(icon);
            }
        }
    }
    public void xoaTrangPBSP(boolean o){
        sanPhamUI.getCbDialogKichThuoc().setEnabled(o);
        sanPhamUI.getCbDialogKichThuoc().setSelectedItem("--Select--");
        sanPhamUI.getCbDialogMauSac().setEnabled(o);
        sanPhamUI.getCbDialogMauSac().setSelectedItem("--Select--");
        sanPhamUI.getTxtDialogSoLuong().setEnabled(o);
        sanPhamUI.getBtnDialogAnh().setEnabled(o);
        sanPhamUI.getTxtDialogSoLuong().setText("");
        sanPhamUI.getPnDialogAnh().repaint();
        sanPhamUI.getPnDialogAnh().revalidate();
        sanPhamUI.getPnDialogAnh().removeAll();
    }
    public String phatSinhMaPBSP(String maSP, String mauSac, String kichThuoc){
        String ma = "";

        ma = maSP+"-"+mauSac+"-"+kichThuoc;
        return ma;
    }
    private void anHienWinPBSP(boolean o){
        sanPhamUI.getWinThemPBSP().setLocationRelativeTo(null);
        sanPhamUI.getWinThemPBSP().setVisible(o);
    }
    //Hàm lấy sản phẩm từ phần thông tin sản phẩm
    private SanPham layDataThem() {
        SanPham sp = new SanPham();
        String ten = sanPhamUI.getTxtTenSanPham().getText();

        // Lấy ngày từ JDateChooser
        Date selectedDate = sanPhamUI.getJDateChooserNgaySanXuat().getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày sản xuất");
            return null;
        }
        // Chuyển đổi từ Date sang LocalDate

        LocalDate localDate = dateToLocalDate(selectedDate);
        if(localDate.isAfter(LocalDate.now())){
            JOptionPane.showMessageDialog(null, "Ngày sản xuất không được lớn hơn ngày hiện tại");
            return null;
        }
        String xuatXu = sanPhamUI.getTxtXuatXu().getText();
        if(sanPhamUI.getCbDoTuoi().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
            JOptionPane.showMessageDialog(null, "Vui lòng chọn độ tuổi");
            return null;
        }
        DoTuoi doTuoi = DoTuoi.layGiaTri(sanPhamUI.getCbDoTuoi().getSelectedItem().toString());
        List<DanhMucSanPham> danhMucs = new ArrayList<>();
        List<LoaiSanPham> loaiSanPhams = new ArrayList<>();
        List<ThuongHieu> thuongHieu = new ArrayList<>();
        try {
            if(sanPhamUI.getCbLoai().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
                JOptionPane.showMessageDialog(null, "Vui lòng chọn loại sản phẩm");
                return null;
            }
            Map<String, Object> loai = new HashMap<>();
            loai.put("tenLoai", sanPhamUI.getCbLoai().getSelectedItem().toString());
            loaiSanPhams = loaiSanPhamImp.timKiem(loai);

            if(sanPhamUI.getDanhMuc().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
                JOptionPane.showMessageDialog(null, "Vui lòng chọn danh mục");
                return null;
            }
            Map<String, Object> dm = new HashMap<>();
            dm.put("tenDM", sanPhamUI.getDanhMuc().getSelectedItem().toString());
            danhMucs = danhMucSanPhamImp.timKiem(dm);

            if(sanPhamUI.getCbThuongHieu().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
                JOptionPane.showMessageDialog(null, "Vui lòng chọn thương hiệu");
                return null;
            }
            Map<String, Object> th = new HashMap<>();
            th.put("tenTH", sanPhamUI.getCbThuongHieu().getSelectedItem().toString());
            thuongHieu = thuongHieuImp.timKiem(th);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        float ptl = 0;
        if(sanPhamUI.getTxtPhamTramLoi().getText().matches("\\d+")){
            ptl = Float.parseFloat(sanPhamUI.getTxtPhamTramLoi().getText());
            if(ptl<0){
                JOptionPane.showMessageDialog(null, "Phần trăm lời không được lớn hơn 0");
                return null;
            }
        }else{
            JOptionPane.showMessageDialog(null, "Phần trăm lời phải là số");
            return null;
        }
        if(sanPhamUI.getCbKieuNguoiMac().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
            JOptionPane.showMessageDialog(null, "Vui lòng chọn kiểu người mặc");
            return null;
        }
        PhongCachMac pcm = PhongCachMac.layGiaTri(sanPhamUI.getCbKieuNguoiMac().getSelectedItem().toString());

        //Hình ảnh
        String anh = fileAnh;
        if(sanPhamUI.getCbTinhTrang().getSelectedItem().toString().equalsIgnoreCase("--Select--")){
            JOptionPane.showMessageDialog(null, "Vui lòng chọn tình trạng sản phẩm");
            return null;
        }
        TinhTrangSanPham tt = TinhTrangSanPham.layGiaTri(sanPhamUI.getCbTinhTrang().getSelectedItem().toString());
        //Mã sản phẩm

        Map<String, Object> conditions = new HashMap<>();
        //Ep kieu string cho boolean
        conditions.put("hieuLuc", "%false%");
        List<Thue> thue;
        try {
           thue = thueImp.timKiem(conditions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            sp = new SanPham(sanPhamUI.getTxtMaSanPham().getText(),ten,loaiSanPhams.get(0),pcm,doTuoi,xuatXu,thuongHieu.get(0) ,ptl,localDate,thue.get(0),tt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sp;
    }

    //Hàm lấy sản phẩm để update
    private SanPham layDataSua() {
        SanPham sps;
        String ten = sanPhamUI.getTxtTenSanPham().getText();

        // Lấy ngày từ JDateChooser
        Date selectedDate = sanPhamUI.getJDateChooserNgaySanXuat().getDate();

        // Chuyển đổi từ Date sang LocalDate
        LocalDate localDate = dateToLocalDate(selectedDate);

        String xuatXu = sanPhamUI.getTxtXuatXu().getText();
        DoTuoi doTuoi = DoTuoi.layGiaTri(sanPhamUI.getCbDoTuoi().getSelectedItem().toString());
        List<DanhMucSanPham> danhMucs = new ArrayList<>();
        List<LoaiSanPham> loaiSanPhams = new ArrayList<>();
        List<ThuongHieu> thuongHieu = new ArrayList<>();
        try {
            Map<String, Object> dm = new HashMap<>();
            dm.put("tenDM", sanPhamUI.getDanhMuc().getSelectedItem().toString());
            danhMucs = danhMucSanPhamImp.timKiem(dm);



            Map<String, Object> loai = new HashMap<>();
            loai.put("tenLoai", sanPhamUI.getCbLoai().getSelectedItem().toString());
            loaiSanPhams = loaiSanPhamImp.timKiem(loai);

            Map<String, Object> th = new HashMap<>();
            th.put("tenTH", sanPhamUI.getCbThuongHieu().getSelectedItem().toString());
            thuongHieu = thuongHieuImp.timKiem(th);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

//        MauSac ms = MauSac.layGiaTri(sanPhamUI.getCbMauSac().getSelectedItem().toString());
//        String kt = sanPhamUI.getTxtKichThuoc().getText().toString();
        float ptl = Float.parseFloat(sanPhamUI.getTxtPhamTramLoi().getText());
        PhongCachMac pcm = PhongCachMac.layGiaTri(sanPhamUI.getCbKieuNguoiMac().getSelectedItem().toString());

        //Hình ảnh
        String anh = fileAnh;
        TinhTrangSanPham tt = TinhTrangSanPham.layGiaTri(sanPhamUI.getCbTinhTrang().getSelectedItem().toString());
//        int soLuong = Integer.parseInt(sanPhamUI.getTxtSoLuongSanPham().getText());
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("HieuLuc", "CO");
        List<Thue> thue;
        try {
            thue = thueImp.timKiem(conditions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //Mã sản phẩm
        String ma = sanPhamUI.getTxtMaSanPham().getText();
        try {
            sps = new SanPham(ma,ten,loaiSanPhams.get(0),pcm,doTuoi,xuatXu,thuongHieu.get(0),ptl,localDate,thue.get(0),tt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sps;
    }

    //Hàm sinh mã sản phẩm
    public String laymaSP(){
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("maSP", maLoai);
        String[] cols = {"maSP"};
        List<Map<String, Object>> dsMaSP = null;
        String maSP = "";
        ArrayList<Integer> ma = new ArrayList<>();
        try {
            dsMaSP = sanPhamImp.timKiem(conditions, false, cols);
            for (int i = 0; i < dsMaSP.size(); i++) {
                ma.add(Integer.parseInt(dsMaSP.get(i).get("maSP").toString().substring(dsMaSP.get(i).get("maSP").toString().length()-3)));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        int max = 0;
        for(int i=0; i<ma.size(); i++){
            if(max < ma.get(i)){
                max = ma.get(i);
            }
        }
//        String nl = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
//        maSP = maSP+nl+"-";
//        String mau = sanPhamUI.getCbMauSac().getSelectedItem().toString();
//        maSP = maSP + mau + "-";
//        maSP = maSP + sanPhamUI.getTxtKichThuoc().getText();
        return formatNumber(max+1);
    }
    public String formatNumber(int number) {
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

    //Hàm xóa trắng dữ liệu nhập
    public void xoaTrangAll(){
        loadComboBoxPhanThongTinSP();
        sanPhamUI.getTxtMaSanPham().setText("");
        sanPhamUI.getTxtTenSanPham().setText("");
        sanPhamUI.getJDateChooserNgaySanXuat().setDate(null);
        sanPhamUI.getTxtXuatXu().setText("");
        sanPhamUI.getTxtPhamTramLoi().setText("");
    }

    //Xóa trắng khi sửa
    public void xoaTrangSua(){
        loadComboBoxPhanThongTinSP();
        sanPhamUI.getTxtTenSanPham().setText("");
        sanPhamUI.getJDateChooserNgaySanXuat().setDate(null);
        sanPhamUI.getTxtXuatXu().setText("");
        sanPhamUI.getTxtPhamTramLoi().setText("");
    }
    public void loadTTPBSP(){
        String maSP = sanPhamUI.getTxtMaSanPham().getText();
        String[] cols = {"mauSac", "kichThuoc", "hinhAnh", "soLuong"};
        Map<String, Object> condintions=  new HashMap<>();
        sanPhamUI.getTxtDialogMaSanPham().setText(maSP);
        condintions.put("sanPham.maSP", maSP);
        try {
            List<Map<String, Object>> listCTPBSP = chiTietPhienBanSanPhamImp.timKiem(condintions, false, cols);
            DefaultTableModel tmCTPBSP = (DefaultTableModel) sanPhamUI.getTbDialogDanhSachCacSanPham().getModel();
            tmCTPBSP.setRowCount(0);
            for(Map<String, Object> map : listCTPBSP){
                MauSac ms = MauSac.layGiaTri(map.get("mauSac").toString());
                String[] row = {map.get("mauSac").toString(), map.get("kichThuoc").toString(), map.get("soLuong").toString(),null};
                tmCTPBSP.addRow(row);
            }
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
                sanPhamUI.getPnDialogAnh().removeAll();
                sanPhamUI.getPnDialogAnh().add(picLabel);
                picLabel.setSize(new Dimension(240,320));
                picLabel.setIcon(icon);
    }
    //Load các comboBox phần thông tin
    public void loadComboBoxPhanThongTinSP(){
        //Lấy độ tuổi từ enum
        DoTuoi[] dsDoTuoi = DoTuoi.values();
        String[] itemsDoTuoi = new String[dsDoTuoi.length + 1];
        itemsDoTuoi[0] = "--Select--";
        for (int i = 0; i < dsDoTuoi.length; i++) {
            itemsDoTuoi[i + 1] = dsDoTuoi[i].toString();
        }
        DefaultComboBoxModel<String> doTuoiCb = new DefaultComboBoxModel<>(itemsDoTuoi);
        sanPhamUI.getCbDoTuoi().setModel(doTuoiCb);

        //Lấy tình trạng từ enum
        TinhTrangSanPham[] dsTinhTrang = TinhTrangSanPham.values();
        String[] itemsTinhTrang = new String[dsTinhTrang.length + 1];
        itemsTinhTrang[0] = "--Select--";
        for (int i = 0; i < dsTinhTrang.length; i++) {
            itemsTinhTrang[i + 1] = dsTinhTrang[i].toString();
        }
        DefaultComboBoxModel<String> tinhTrangCb = new DefaultComboBoxModel<>(itemsTinhTrang);
        sanPhamUI.getCbTinhTrang().setModel(tinhTrangCb);

        //Lấy tình trạng từ enum
        PhongCachMac[] dsPhongCachMac = PhongCachMac.values();
        String[] itemsPhongCachMac = new String[dsPhongCachMac.length + 1];
        itemsPhongCachMac[0] = "--Select--";
        for (int i = 0; i < dsPhongCachMac.length; i++) {
            itemsPhongCachMac[i + 1] = dsPhongCachMac[i].toString();
        }
        DefaultComboBoxModel<String> PhongCachMacCb = new DefaultComboBoxModel<>(itemsPhongCachMac);
        sanPhamUI.getCbKieuNguoiMac().setModel(PhongCachMacCb);

        //Load cbDanhMuc
        try {
            dsDanhMucSanPham = danhMucSanPhamImp.timKiem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String[] danhMucArray = dsDanhMucSanPham.stream().map(DanhMucSanPham::getTenDM).toArray(String[]::new);
        String[] itemsDanhMuc = new String[danhMucArray.length + 1];
        itemsDanhMuc[0] = "--Select--";
        System.arraycopy(danhMucArray, 0, itemsDanhMuc, 1, danhMucArray.length);
        DefaultComboBoxModel<String> danhMucComboBoxModel = new DefaultComboBoxModel<>(itemsDanhMuc);
        sanPhamUI.getDanhMuc().setModel(danhMucComboBoxModel);

        //Load cbLoaiSanPham
        try {
            dsLoaiSanPham = loaiSanPhamImp.timKiem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String[] loaiArray = dsLoaiSanPham.stream().map(LoaiSanPham::getTenLoai).toArray(String[]::new);
        String[] itemsLoai = new String[loaiArray.length + 1];
        itemsLoai[0] = "--Select--";
        System.arraycopy(loaiArray, 0, itemsLoai, 1, loaiArray.length);
        DefaultComboBoxModel<String> loaiComboBoxModel = new DefaultComboBoxModel<>(itemsLoai);
        sanPhamUI.getCbLoai().setModel(loaiComboBoxModel);

        //Load cbThuongHieu
        try {
            dsThuongHieu = thuongHieuImp.timKiem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String[] thuongHieuArray = dsThuongHieu.stream().map(ThuongHieu::getTenTH).toArray(String[]::new);
        String[] itemsThuongHieu = new String[thuongHieuArray.length + 1];
        itemsThuongHieu[0] = "--Select--";
        System.arraycopy(thuongHieuArray, 0, itemsThuongHieu, 1, thuongHieuArray.length);
        DefaultComboBoxModel<String> thuongHieuComboBoxModel = new DefaultComboBoxModel<>(itemsThuongHieu);
        sanPhamUI.getCbThuongHieu().setModel(thuongHieuComboBoxModel);
    }
    //Load cac comboBox phan thong tin phien ban san pham
    public void loadComboBoxPhanThongTinPhienBanSP(){

        MauSac[] dsMauSac = MauSac.values();
        String[] itemsMauSac = new String[dsMauSac.length + 1];
        itemsMauSac[0] = "--Select--";
        for (int i = 0; i < dsMauSac.length; i++) {
            itemsMauSac[i + 1] = dsMauSac[i].toString();
        }
        DefaultComboBoxModel<String> mauSacCb = new DefaultComboBoxModel<>(itemsMauSac);
        sanPhamUI.getCbDialogMauSac().setModel(mauSacCb);
        String maSP = sanPhamUI.getTxtMaSanPham().getText();
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("sanPham.maSP", maSP);
        String loai = maSP.substring(0, 1);

        //Lấy kích thước từ enum
        try {
            String[] kichThuoc;
            List<ChiTietPhienBanSanPham> pbsp = chiTietPhienBanSanPhamImp.timKiem(conditions);
            if(trangThaiNutThem == 0 && pbsp.size()>0){
                if(pbsp.get(0).getKichThuoc().matches("\\d+")){
                    kichThuoc = new String[]{"--Select--", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41","42", "43", "44", "45", "46", "47", "48"};
                }else{
                    kichThuoc = new String[]{"--Select--", "S", "M", "L", "XL", "XXL"};
                }
            }else{
                if(loai.equalsIgnoreCase("Q")){
                    kichThuoc = new String[]{"--Select--", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41","42", "43", "44", "45", "46", "47", "48"};
                }else{
                    kichThuoc = new String[]{"--Select--", "S", "M", "L", "XL", "XXL"};
                }
            }

            DefaultComboBoxModel<String> kichThuocCb = new DefaultComboBoxModel<>(kichThuoc);
            sanPhamUI.getCbDialogKichThuoc().setModel(kichThuocCb);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //Load các comboBox phần tìm kiếm
    public void loadComboBoxPhanTimKiem(){
        //Load cbDanhMuc
        try {
            dsDanhMucSanPham = danhMucSanPhamImp.timKiem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String[] danhMucArray = dsDanhMucSanPham.stream().map(DanhMucSanPham::getTenDM).toArray(String[]::new);
        String[] itemsDanhMuc = new String[danhMucArray.length + 1];
        itemsDanhMuc[0] = "--Danh mục--";
        System.arraycopy(danhMucArray, 0, itemsDanhMuc, 1, danhMucArray.length);
        DefaultComboBoxModel<String> danhMucComboBoxModel = new DefaultComboBoxModel<>(itemsDanhMuc);
        sanPhamUI.getCbTkDanhMuc().setModel(danhMucComboBoxModel);

        //Load cbLoaiSanPham
        try {
            dsLoaiSanPham = loaiSanPhamImp.timKiem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String[] loaiArray = dsLoaiSanPham.stream().map(LoaiSanPham::getTenLoai).toArray(String[]::new);
        String[] itemsLoai = new String[loaiArray.length + 1];
        itemsLoai[0] = "--Loại--";
        System.arraycopy(loaiArray, 0, itemsLoai, 1, loaiArray.length);
        DefaultComboBoxModel<String> loaiComboBoxModel = new DefaultComboBoxModel<>(itemsLoai);
        sanPhamUI.getCbTkLoaiSanPham().setModel(loaiComboBoxModel);

        //Lấy tình trạng từ enum
        TinhTrangSanPham[] dsTinhTrang = TinhTrangSanPham.values();
        String[] itemsTinhTrang = new String[dsTinhTrang.length + 1];
        itemsTinhTrang[0] = "--Tình trạng--";
        for (int i = 0; i < dsTinhTrang.length; i++) {
            itemsTinhTrang[i + 1] = dsTinhTrang[i].toString();
        }
        DefaultComboBoxModel<String> tinhTrangCb = new DefaultComboBoxModel<>(itemsTinhTrang);
        sanPhamUI.getCbTkTinhTrang().setModel(tinhTrangCb);

        //Load cbThuongHieu
        try {
            dsThuongHieu = thuongHieuImp.timKiem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String[] thuongHieuArray = dsThuongHieu.stream().map(ThuongHieu::getTenTH).toArray(String[]::new);
        String[] itemsThuongHieu = new String[thuongHieuArray.length + 1];
        itemsThuongHieu[0] = "--Thương hiệu--";
        System.arraycopy(thuongHieuArray, 0, itemsThuongHieu, 1, thuongHieuArray.length);
        DefaultComboBoxModel<String> thuongHieuComboBoxModel = new DefaultComboBoxModel<>(itemsThuongHieu);
        sanPhamUI.getCbTkThuongHieu().setModel(thuongHieuComboBoxModel);

        //Lấy Do tuổi
        DoTuoi[] dsDoTuoi = DoTuoi.values();
        String[] itemsDoTuoi = new String[dsDoTuoi.length + 1];
        itemsDoTuoi[0] = "--Độ tuổi--";
        for (int i = 0; i < dsDoTuoi.length; i++) {
            itemsDoTuoi[i + 1] = dsDoTuoi[i].toString();
        }
        DefaultComboBoxModel<String> doTuoiCb = new DefaultComboBoxModel<>(itemsDoTuoi);
        sanPhamUI.getCbTkDoTuoi().setModel(doTuoiCb);

        //Lấy PhongCachMac
        PhongCachMac[] dsPhongCachMac = PhongCachMac.values();
        String[] itemsPhongCachMac = new String[dsPhongCachMac.length + 1];
        itemsPhongCachMac[0] = "--Phong cách--";
        for (int i = 0; i < dsPhongCachMac.length; i++) {
            itemsPhongCachMac[i + 1] = dsPhongCachMac[i].toString();
        }
        DefaultComboBoxModel<String> PhongCachMacCb = new DefaultComboBoxModel<>(itemsPhongCachMac);
        sanPhamUI.getCbTkPhongCachMac().setModel(PhongCachMacCb);
    }

    //Hàm đóng/mở tương tác
    public void tuongTac(boolean c){
        sanPhamUI.getTxtTenSanPham().setEnabled(c);
        sanPhamUI.getJDateChooserNgaySanXuat().setEnabled(c);
        sanPhamUI.getTxtXuatXu().setEnabled(c);
        sanPhamUI.getCbDoTuoi().setEnabled(c);
        sanPhamUI.getCbTinhTrang().setEnabled(c);
        sanPhamUI.getDanhMuc().setEnabled(c);
        sanPhamUI.getCbLoai().setEnabled(c);
        sanPhamUI.getCbThuongHieu().setEnabled(c);
        sanPhamUI.getTxtPhamTramLoi().setEnabled(c);
        sanPhamUI.getCbKieuNguoiMac().setEnabled(c);
    }
    //Hàm đóng/mở tương tác tìm kiếm
    public void tuongTacTimKiem(boolean o){
        sanPhamUI.getCbTkDanhMuc().setEnabled(o);
        sanPhamUI.getCbTkLoaiSanPham().setEnabled(o);
        sanPhamUI.getCbTkTinhTrang().setEnabled(o);
        sanPhamUI.getCbTkThuongHieu().setEnabled(o);
        sanPhamUI.getCbTkDoTuoi().setEnabled(o);
        sanPhamUI.getCbTkPhongCachMac().setEnabled(o);
        sanPhamUI.getTxtTuKhoaTimKiem().setEnabled(o);
    }

//  Sự kiện click trên table load dữ liệu xuống phần thông tin
    @Override
    public void mouseClicked(MouseEvent event){
        if(event.getSource().equals(sanPhamUI.getTbDanhSachSanPham())){
            if(trangThaiNutThem==1){
                JOptionPane.showMessageDialog(null, "Đang thực hiện chức năng thêm, không được click!!");
            }else {
                int row = sanPhamUI.getTbDanhSachSanPham().getSelectedRow();
                String ma = sanPhamUI.getTbDanhSachSanPham().getValueAt(row, 0).toString();
                SanPham spHienThuc = new SanPham();
                try {
                    spHienThuc = sanPhamImp.timKiem(ma);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                sanPhamUI.getTxtMaSanPham().setText(spHienThuc.getMaSP());
                sanPhamUI.getTxtTenSanPham().setText(spHienThuc.getTenSP());

                //Xử lý ngày
                String date = String.valueOf(spHienThuc.getNgaySanXuat());
                Date date2 = null;
                try {
                    date2 = new SimpleDateFormat("yyyy-mm-dd").parse(date);
                } catch (ParseException e) {throw new RuntimeException(e);

                }
                sanPhamUI.getJDateChooserNgaySanXuat().setDate(date2);

                sanPhamUI.getTxtXuatXu().setText(spHienThuc.getXuatXu());

                sanPhamUI.getCbDoTuoi().setSelectedItem(spHienThuc.getDoTuoi().toString());
                sanPhamUI.getCbTinhTrang().setSelectedItem(spHienThuc.getTinhTrang().toString());
                sanPhamUI.getDanhMuc().setSelectedItem(spHienThuc.getLoaiSanPham().getDanhMucSanPham().getTenDM());
                sanPhamUI.getCbLoai().setSelectedItem(spHienThuc.getLoaiSanPham().getTenLoai());
                sanPhamUI.getCbThuongHieu().setSelectedItem(spHienThuc.getThuongHieu().getTenTH());

                sanPhamUI.getTxtPhamTramLoi().setText(String.valueOf(spHienThuc.getPhanTramLoi()));
                sanPhamUI.getCbKieuNguoiMac().setSelectedItem(spHienThuc.getPhongCachMac().toString());


            }
        }else if(event.getSource().equals(sanPhamUI.getTbDialogDanhSachCacSanPham())){
            if(trangThaiNutThemPBSP == 1){
                JOptionPane.showMessageDialog(null, "Đang thực hiện chức năng thêm, không được click!!");
            }else if(trangThaiNutThemPBSP == 0){

                int row = sanPhamUI.getTbDialogDanhSachCacSanPham().getSelectedRow();
                String ms = sanPhamUI.getTbDialogDanhSachCacSanPham().getValueAt(row, 0).toString();
                String kt = sanPhamUI.getTbDialogDanhSachCacSanPham().getValueAt(row, 1).toString();
                String sl = sanPhamUI.getTbDialogDanhSachCacSanPham().getValueAt(row, 2).toString();
                xoaTrangPBSP(false);
                sanPhamUI.getCbDialogMauSac().setSelectedItem(ms);
                sanPhamUI.getCbDialogKichThuoc().setSelectedItem(kt);
                sanPhamUI.getTxtDialogSoLuong().setText(sl);
                try {
                    Optional<ChiTietPhienBanSanPham> pbsp = chiTietPhienBanSanPhamImp.timKiem(sanPhamUI.getTxtDialogMaSanPham().getText(), MauSac.layGiaTri(ms), kt);
                    importAnh(pbsp.get().getHinhAnh());
                } catch (Exception e) {
                    throw new RuntimeException(e);
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void onDelete(int row) {
        if(sanPhamUI.getTbDialogDanhSachCacSanPham().isEditing()){
            sanPhamUI.getTbDialogDanhSachCacSanPham().getCellEditor().stopCellEditing();
        }
        if(trangThaiNutThemPBSP == 0){
            int result  = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa không?", "Thông báo", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                String maSP = sanPhamUI.getTxtMaSanPham().getText();
                String ms = sanPhamUI.getTbDialogDanhSachCacSanPham().getValueAt(row, 0).toString();
                String kt = sanPhamUI.getTbDialogDanhSachCacSanPham().getValueAt(row, 1).toString();
                try {
                    chiTietPhienBanSanPhamImp.xoa(phatSinhMaPBSP(maSP, ms, kt));
                    JOptionPane.showMessageDialog(null, "Xóa thành công!!");
                    loadTTPBSP();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }else{

        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if(e.getSource().equals(sanPhamUI.getTbDialogDanhSachCacSanPham())){
            DefaultTableModel tmPBSP = (DefaultTableModel) sanPhamUI.getTbDialogDanhSachCacSanPham().getModel();
            int row = e.getFirstRow();
            String maPBSP =  sanPhamUI.getTxtDialogMaSanPham().getText()+"-"+tmPBSP.getValueAt(row, 0).toString()+"-"+tmPBSP.getValueAt(row, 1).toString();
            try {
                ChiTietPhienBanSanPham phienBanSanPham = new ChiTietPhienBanSanPham(maPBSP, sanPhamImp.timKiem(sanPhamUI.getTxtDialogMaSanPham().getText()), MauSac.layGiaTri(tmPBSP.getValueAt(row, 0).toString()), tmPBSP.getValueAt(row, 1).toString(), Integer.parseInt(tmPBSP.getValueAt(row, 2).toString()), fileAnh);
                listCTPBSP.add(phienBanSanPham);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
