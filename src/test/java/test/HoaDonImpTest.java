package test;

import dev.skyherobrine.app.daos.HoaDonDAO;
import dev.skyherobrine.app.daos.order.HoaDonImp;
import dev.skyherobrine.app.entities.order.HoaDon;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HoaDonImpTest {
    private HoaDonDAO<HoaDon> hoaDonDAO;

    @BeforeEach
    void setUp() throws Exception {
        hoaDonDAO = new HoaDonImp();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void timKiem() throws Exception {
        int soHoaDon = 3;
        List<HoaDon> list = hoaDonDAO.timKiem();
        Assertions.assertEquals(soHoaDon, list.size());
    }

    @Test
    void testTimKiem() throws Exception {
        String ma = "HD-20240422-077-1";
        HoaDon hd  = hoaDonDAO.timKiem("HD-20240422-077-1\n" +
                "AKN120231120001-DO-M");
        Assertions.assertEquals(ma, hd.getMaHD());
    }
}