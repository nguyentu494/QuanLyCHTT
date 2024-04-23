/*
 * @ (#) HoaDonTests.java   1.0     23/04/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved
 */

import dev.skyherobrine.app.daos.HoaDonDAO;
import dev.skyherobrine.app.daos.order.HoaDonImp;
import dev.skyherobrine.app.entities.order.HoaDon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/*
 * @description:
 * @author: Tuss Nguyen
 * @date: 23/04/2024
 * @version: 1.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HoaDonTests {
    @BeforeAll
    public void setUp() throws Exception {
        HoaDonDAO<HoaDon> hoaDonDAO = new HoaDonImp();
    }


}
