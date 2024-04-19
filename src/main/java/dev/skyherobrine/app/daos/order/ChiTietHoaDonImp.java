package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ChiTietHoaDonDAO;
import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.entities.order.ChiTietHoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietHoaDonImp extends UnicastRemoteObject implements ChiTietHoaDonDAO<ChiTietHoaDon> {
    private ConnectDB connectDB;
    private EntityManager em;
    public ChiTietHoaDonImp() throws Exception {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(ChiTietHoaDon chiTietHoaDon) throws Exception {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(chiTietHoaDon);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public boolean capNhat(ChiTietHoaDon target) throws Exception {
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
    public List<ChiTietHoaDon> timKiem() throws Exception {
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
                ("select * from ChiTietHoaDon");
        List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
        while(resultSet.next()) {
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
//                    (new HoaDonDAO().timKiem(resultSet.getString("MaHD")).get(),
//                     new ChiTietPhienBanSanPhamDAO().timKiem(resultSet.getString("MaPhienBanSP")).get(),
//                     resultSet.getInt("SoLuongMua"));

            chiTietHoaDons.add(chiTietHoaDon);
        }
        return chiTietHoaDons;
    }

    @Override
    public List<ChiTietHoaDon> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from ChiTietHoaDon cthd where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("cthd." + column + " like '%" + value + "%'"));
            isNeedAnd.set(true);
        });

        List<ChiTietHoaDon> chiTietHoaDons = em.createNativeQuery(query.get(), ChiTietHoaDon.class).getResultList();
        return chiTietHoaDons;
    }

    @Override
    public ChiTietHoaDon timKiem(String id) throws Exception {
        return null;
    }

    @Override
    public List<ChiTietHoaDon> timKiem(String... ids) throws Exception {
        return null;
    }
    public List<Map<String, Integer>> timKiem(String cols, String join, String query) throws SQLException {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select " + cols + " from  ChiTietHoaDon " + join + " where " + query);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Integer>> listResult = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Integer> rowDatas = new HashMap<>();
            rowDatas.put(resultSet.getString("TenSP"), resultSet.getInt("soLuongBan"));
            listResult.add(rowDatas);
        }
        return listResult;
    }
    public List<Map<String, Object>> timKiemHD(String cols, String join, String query) throws SQLException {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select " + cols + " from  ChiTietHoaDon " + join + " where " + query);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Object>> listResult = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Integer> rowDatas = new HashMap<>();
            rowDatas.put(resultSet.getString("NgayLap"), resultSet.getInt("soLuongBan"));
            Map<String, Object> rowDatas1 = new HashMap<>();
            rowDatas1.put(resultSet.getString("MaSP"), rowDatas);
            listResult.add(rowDatas1);
        }
        return listResult;
    }
    public Optional<ChiTietHoaDon> timKiem(String maHD, String maPhienBanSP) throws Exception{
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from ChiTietHoaDon where MaPhienBanSP = ? and MaHD = ?");
        preparedStatement.setString(1, maPhienBanSP);
        preparedStatement.setString(2, maHD);

        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
//            return Optional.of(new ChiTietHoaDon(new HoaDonDAO().timKiem(resultSet.getString("MaHD")).get(),
//                    new ChiTietPhienBanSanPhamDAO().timKiem(resultSet.getString("MaPhienBanSP")).get(),
//                    resultSet.getInt("SoLuongMua")));
            return Optional.empty();
        } else {
            return Optional.empty();
        }
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

        query.set(query.get() + " from ChiTietHoaDon where ");

        conditions.forEach((column, value) -> {
            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
            canAnd.set(true);
        });

        System.out.println(query);
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
