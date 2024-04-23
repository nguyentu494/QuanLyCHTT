package test;

import dev.skyherobrine.app.daos.KhachHangDAO;
import dev.skyherobrine.app.daos.person.KhachHangImp;
import dev.skyherobrine.app.entities.person.KhachHang;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KhachHangImpTest {

    private KhachHangDAO<KhachHang> kh;
    @BeforeEach
    void setUp() throws RemoteException {
        kh = new KhachHangImp();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void timKiem() throws Exception {
        int soKH = 165;
        List<KhachHang> list = kh.timKiem();
        System.out.println(list.size());
        Assertions.assertEquals(soKH, list.size());
    }

    @Test
    void testTimKiem() {
//        String ten = "Cao Thi Thu Ha";
//        Map<String, Object> conditions = new HashMap<>();
//        conditions.put("CONCAT(maKH, hoTen, soDienThoai, gioiTinh, ngaySinh, diemTichLuy)", ten);
//        String[] col = {"maKH", "hoTen", "soDienThoai", "gioiTinh", "ngaySinh", "diemTichLuy"};
//        List<Map<String, Object>> khachHang;
//        try {
//            khachHang = kh.timKiem(conditions, false, col);
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//        Assertions.assertEquals(ten, khachHang.getClass().getName());
    }
}