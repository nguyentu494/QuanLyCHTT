/*
 * @ (#) Server.java   1.0     15/04/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved
 */

package test;

import dev.skyherobrine.app.daos.*;
import dev.skyherobrine.app.daos.order.*;
import dev.skyherobrine.app.daos.person.KhachHangImp;
import dev.skyherobrine.app.daos.person.NhaCungCapImp;
import dev.skyherobrine.app.daos.person.NhanVienImp;
import dev.skyherobrine.app.daos.product.*;
import dev.skyherobrine.app.daos.sale.ThueImp;
import dev.skyherobrine.app.entities.order.*;
import dev.skyherobrine.app.entities.person.KhachHang;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.entities.person.NhanVien;
import dev.skyherobrine.app.entities.product.*;
import dev.skyherobrine.app.entities.sale.Thue;
import dev.skyherobrine.app.enums.CaLamViec;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.registry.LocateRegistry;

/*
 * @description:
 * @author: Tuss Nguyen
 * @date: 15/04/2024
 * @version: 1.0
 */
public class Server {
    private static final String URL = "rmi://LAPTOP-UA4UAHPE:7878/";

    public static void main(String[] args) throws Exception {
        Context context = new InitialContext();

        NhanVienDAO<NhanVien> nhanVienDAO = new NhanVienImp();  //Java Remote Object
        ThueDAO<Thue> thueDAO = new ThueImp();
        NhaCungCapDAO<NhaCungCap> nhaCungCapDAO = new NhaCungCapImp();
        KhachHangDAO<KhachHang> khachHangDAO = new KhachHangImp();
        SanPhamDAO<SanPham> sanPhamDAO = new SanPhamImp();
        LoaiSanPhamDAO<LoaiSanPham> loaiSanPhamDAO = new LoaiSanPhamImp();
        ThuongHieuDAO<ThuongHieu> thuongHieuDAO = new ThuongHieuImp();
        DanhMucSanPhamDAO<DanhMucSanPham> danhMucSanPhamDAO = new DanhMucSanPhamImp();
        ChiTietHoaDonDAO<ChiTietHoaDon> chiTietHoaDonDAO = new ChiTietHoaDonImp();
        ChiTietPhienBanSanPhamDAO<ChiTietPhienBanSanPham> chiTietPhienBanSanPhamDAO = new ChiTietPhienBanSanPhamImp();
        ChiTietPhieuNhapHangDAO<ChiTietPhieuNhapHang> chiTietPhieuNhapHangDAO = new ChiTietPhieuNhapHangImp();
        ChiTietPhieuNhapHangPhienBanSPDAO<ChiTietPhieuNhapHangPhienBanSP> chiTietPhieuNhapHangPhienBanSPDAO = new ChiTietPhieuNhapHangPhienBanSPImp();
        HoaDonDAO<HoaDon> hoaDonDAO = new HoaDonImp();
        PhieuNhapHangDAO<PhieuNhapHang> phieuNhapHangDAO = new PhieuNhapHangImp();
        PhieuTraKhachHangDAO<PhieuTraKhachHang> phieuTraKhachHangDAO = new PhieuTraKhachHangImp();
        ChiTietPhieuTraKhachHangDAO<ChiTietPhieuTraKhachHang> chiTietPhieuTraKhachHangDAO = new ChiTietPhieuTraKhachHangImp();


//        System.out.println(nhanVienDAO.timKiem("NV-20231020-001").get());

        LocateRegistry.createRegistry(7878);


        context.bind(URL + "nhanVienDAO", nhanVienDAO);
        context.bind(URL + "thueDAO", thueDAO);
        context.bind(URL + "nhaCungCapDAO", nhaCungCapDAO);
        context.bind(URL + "khachHangDAO", khachHangDAO);
        context.bind(URL + "sanPhamDAO", sanPhamDAO);
        context.bind(URL + "loaiSanPhamDAO", loaiSanPhamDAO);
        context.bind(URL + "thuongHieuDAO", thuongHieuDAO);
        context.bind(URL + "danhMucSanPhamDAO", danhMucSanPhamDAO);
        context.bind(URL + "chiTietHoaDonDAO", chiTietHoaDonDAO);
        context.bind(URL + "chiTietPhienBanSanPhamDAO", chiTietPhienBanSanPhamDAO);
        context.bind(URL + "chiTietPhieuNhapHangDAO", chiTietPhieuNhapHangDAO);
        context.bind(URL + "chiTietPhieuNhapHangPhienBanSPDAO", chiTietPhieuNhapHangPhienBanSPDAO);
        context.bind(URL + "hoaDonDAO", hoaDonDAO);
        context.bind(URL + "phieuNhapHangDAO", phieuNhapHangDAO);
        context.bind(URL + "phieuTraKhachHangDAO", phieuTraKhachHangDAO);
        context.bind(URL + "chiTietPhieuTraKhachHangDAO", chiTietPhieuTraKhachHangDAO);


        System.out.println("Server is running...");
    }
}
