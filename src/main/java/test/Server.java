/*
 * @ (#) Server.java   1.0     15/04/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved
 */

package test;

import dev.skyherobrine.app.daos.NhaCungCapDAO;
import dev.skyherobrine.app.daos.NhanVienDAO;
import dev.skyherobrine.app.daos.ThueDAO;
import dev.skyherobrine.app.daos.person.NhaCungCapImp;
import dev.skyherobrine.app.daos.person.NhanVienImp;
import dev.skyherobrine.app.daos.sale.ThueImp;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.entities.person.NhanVien;
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

//        System.out.println(nhanVienDAO.timKiem("NV-20231020-001").get());

        LocateRegistry.createRegistry(7878);

        context.bind(URL + "nhanVienDAO", nhanVienDAO);
        context.bind(URL + "thueDAO", thueDAO);
        context.bind(URL + "nhaCungCapDAO", nhaCungCapDAO);

        System.out.println("Server is running...");
    }
}
