package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.HoaDonDAO;
import dev.skyherobrine.app.daos.person.KhachHangImp;
import dev.skyherobrine.app.daos.person.NhanVienImp;
import dev.skyherobrine.app.entities.order.HoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class HoaDonImp extends UnicastRemoteObject implements HoaDonDAO<HoaDon> {
    private ConnectDB connectDB;
    private EntityManager em;
    public HoaDonImp() throws Exception {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(HoaDon hoaDon) throws Exception {
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            em.merge(hoaDon);
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
//            em.createNamedQuery()
            et.commit();
        }catch (Exception e){

        }
        return null;
    }

    @Override
    public List<HoaDon> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from HoaDon hd where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("hd." + column + " LIKE '" + value + "'") +"ORDER BY ngay_lap ASC");
            isNeedAnd.set(true);
        });


        return em.createNativeQuery(query.get(), HoaDon.class).getResultList();
    }

    @Override
    public HoaDon timKiem(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from HoaDon where MaHD = ?");
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(new HoaDon(
                    resultSet.getString("MaHD"),
                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
                    new NhanVienImp().timKiem(resultSet.getString("MaNV")),
                    new KhachHangImp().timKiem(resultSet.getString("MaKH")),
                    resultSet.getBigDecimal("SoTienKHTra"),
                    resultSet.getString("GhiChu")
            )).get();
        }
        return (HoaDon) Optional.empty().get();
    }

    @Override
    public List<HoaDon> timKiem(String... ids) throws Exception {
        String query = "select * from HoaDon where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("MaHD = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<HoaDon> hoaDons = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            HoaDon hoaDon = new HoaDon(
                    resultSet.getString("MaHD"),
                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
                    new NhanVienImp().timKiem(resultSet.getString("MaNV")),
                    new KhachHangImp().timKiem(resultSet.getString("MaKH")),
                    resultSet.getBigDecimal("SoTienKHTra"),
                    resultSet.getString("GhiChu")
            );
            hoaDons.add(hoaDon);
        }
        return hoaDons;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);
        AtomicBoolean canAnd = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from HoaDon where ");

        conditions.forEach((column, value) -> {
            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
            canAnd.set(true);
        });
        System.out.println(query.get());
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery(query.get());

        List<Map<String, Object>> listResult = new ArrayList<>();
        while(resultSet.next()){
            Map<String, Object> rowDatas = new HashMap<>();
            for(String column : Arrays.stream(colNames).toList()) {
                rowDatas.put(column, resultSet.getString(column));
            }
            listResult.add(rowDatas);
        }
        return listResult;
    }
}
