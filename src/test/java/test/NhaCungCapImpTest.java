package test;

import dev.skyherobrine.app.daos.NhaCungCapDAO;
import dev.skyherobrine.app.daos.person.NhaCungCapImp;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NhaCungCapImpTest {

    private NhaCungCapDAO<NhaCungCap> ncc;
    @BeforeEach
    void setUp() throws RemoteException {
        ncc = new NhaCungCapImp();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void xoa() throws Exception {
            if (ncc.xoa("NCC-20231115-04")){
                NhaCungCap NCC = ncc.timKiem("NCC-20231115-04");
                Assertions.assertEquals(NCC.getTinhTrang().toString(), "CHAM_DUT");
            }
    }

    @Test
    void timKiem() throws Exception {
        int soNCC = 8;
        List<NhaCungCap> list = ncc.timKiem();
        Assertions.assertEquals(soNCC, list.size());
    }
}