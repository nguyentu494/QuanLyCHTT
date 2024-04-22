package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.HoaDonDAO;
import dev.skyherobrine.app.daos.person.KhachHangImp;
import dev.skyherobrine.app.daos.person.NhanVienImp;
import dev.skyherobrine.app.entities.order.HoaDon;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class HoaDonImp extends UnicastRemoteObject implements HoaDonDAO<HoaDon> {
    private EntityManager em;
    public HoaDonImp() throws Exception {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(HoaDon hoaDon) throws Exception {
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.persist(hoaDon);
            et.commit();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            et.rollback();
            return false;
        }
    }

    @Override
    public boolean capNhat(HoaDon target) throws Exception {
        return false;
    }

    @Override
    public boolean xoa(String id) throws Exception {
        return false;
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<HoaDon> timKiem() throws Exception {
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
//                ("select * from HoaDon ORDER BY NgayLap ASC");
//        List<HoaDon> hoaDons = new ArrayList<>();
//        while (resultSet.next()) {
//            HoaDon hoaDon = new HoaDon(
//                    resultSet.getString("MaHD"),
//                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
//                    new NhanVienDAO().timKiem(resultSet.getString("MaNV")).get(),
//                    new KhachHangDAO().timKiem(resultSet.getString("MaKH")).get(),
//                    resultSet.getBigDecimal("SoTienKHTra"),
//                    resultSet.getString("GhiChu")
//            );
//            hoaDons.add(hoaDon);
//        }
//        return hoaDons;
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
              List<HoaDon> hoaDons =   em.createNamedQuery("HD.orderByDate", HoaDon.class).getResultList();
            et.commit();
            return hoaDons;
        }catch (Exception e){
            et.rollback();
        }
        return null;
    }

    @Override
    public List<HoaDon> timKiem(Map<String, Object> conditions) throws Exception {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select hd from HoaDon hd where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND hd."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND hd."+ key +" LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get(), HoaDon.class);


        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if(entry.getKey().contains(".")) {
                    String ex = entry.getKey().substring(entry.getKey().lastIndexOf(".") + 1);
                    q.setParameter(ex, entry.getValue());
                }else{
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        List<HoaDon> hoaDons = new ArrayList<>();
        try {
            tx.begin();
            hoaDons = q.getResultList();
            tx.commit();
            return hoaDons;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public HoaDon timKiem(String id) throws Exception {
        return em.createNamedQuery("HD.findByID", HoaDon.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public List<HoaDon> timKiem(String... ids) throws Exception {
//        String query = "select * from HoaDon where ";
//        String[] listID = (String[]) Arrays.stream(ids).toArray();
//        for(int i = 0; i < listID.length; ++i) {
//            query += ("MaHD = '" + listID[i] + "'");
//            if((i + 1) >= listID.length) break;
//            else query += ", ";
//        }

        List<HoaDon> hoaDons = new ArrayList<>();
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            HoaDon hoaDon = new HoaDon(
//                    resultSet.getString("MaHD"),
//                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
//                    new NhanVienImp().timKiem(resultSet.getString("MaNV")),
//                    new KhachHangImp().timKiem(resultSet.getString("MaKH")),
//                    resultSet.getBigDecimal("SoTienKHTra"),
//                    resultSet.getString("GhiChu")
//            );
//            hoaDons.add(hoaDon);
//        }
        return hoaDons;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + "hd."+column);
            canPhay.set(true);
        });

        query.set(query.get() + " from HoaDon hd where 1 = 1");

        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                query.set(query + " AND t."+key+" LIKE :"+key);
            }
        }
        Query q = em.createQuery(query.get());

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }

        List<Map<String, Object>> listResult = new ArrayList<>();
        System.out.println(query.get());
        List<Object[]> results = q.getResultList();
        for (Object[] result : results) {
            Map<String, Object> rowDatas = new HashMap<>();
            for (int i = 0; i < colNames.length; i++) {
                rowDatas.put(colNames[i], result[i]);
            }
            listResult.add(rowDatas);
        }
        return listResult;
    }
}
