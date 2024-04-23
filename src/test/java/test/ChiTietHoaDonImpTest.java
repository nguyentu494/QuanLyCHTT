package test;

import dev.skyherobrine.app.daos.ChiTietHoaDonDAO;
import dev.skyherobrine.app.daos.order.ChiTietHoaDonImp;
import dev.skyherobrine.app.daos.order.HoaDonImp;
import dev.skyherobrine.app.entities.order.ChiTietHoaDon;
import dev.skyherobrine.app.entities.order.HoaDon;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChiTietHoaDonImpTest {
private ChiTietHoaDonDAO<ChiTietHoaDon> cthd;

    @BeforeEach
    void setUp() throws Exception {
        cthd = new ChiTietHoaDonImp();
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void timKiem() throws Exception {
        int soCTTHD = 4;
        List<ChiTietHoaDon> list = cthd.timKiem();
        Assertions.assertEquals(soCTTHD, list.size());
    }
}