//package test;
//
//import dev.skyherobrine.app.daos.NhaCungCapDAO;
//import dev.skyherobrine.app.daos.NhanVienDAO;
//import dev.skyherobrine.app.daos.person.NhaCungCapImp;
//import dev.skyherobrine.app.daos.person.NhanVienImp;
//import dev.skyherobrine.app.entities.person.NhaCungCap;
//import dev.skyherobrine.app.entities.person.NhanVien;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.rmi.RemoteException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class NhanVienImpTest {
//
//    private NhanVienDAO<NhanVien> nv;
//    @BeforeEach
//    void setUp() throws RemoteException {
//      nv = new NhanVienImp();
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//    @Test
//    void capNhat() {
//        NhanVien NV = nv.timKiem("NV-20231020-001");
//        NV.setChucVu();
//        if(nv.capNhat())
//    }
//
//    @Test
//    void timKiem() {
//    }
//}