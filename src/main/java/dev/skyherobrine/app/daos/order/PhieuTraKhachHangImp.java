package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.PhieuTraKhachHangDAO;
import dev.skyherobrine.app.entities.order.PhieuTraKhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PhieuTraKhachHangImp extends UnicastRemoteObject implements PhieuTraKhachHangDAO<PhieuTraKhachHang> {
    private EntityManager em;

    public PhieuTraKhachHangImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(PhieuTraKhachHang phieuTraKhachHang) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(phieuTraKhachHang);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert PhieuTraKhachHang values(?, ?, ?)");
//        preparedStatement.setString(1, phieuTraKhachHang.getMaPhieuTraKhachHang());
//        preparedStatement.setTimestamp(2, Timestamp.valueOf(phieuTraKhachHang.getNgayLap()));
//        preparedStatement.setString(3, phieuTraKhachHang.getHoaDon().getMaHD());
//
//        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean capNhat(PhieuTraKhachHang target) throws RemoteException {
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
    public List<PhieuTraKhachHang> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<PhieuTraKhachHang> phieuTraKhachHangs = em.createNamedQuery("PhieuTraKhachHang.findAll", PhieuTraKhachHang.class).getResultList();
            et.commit();
            return phieuTraKhachHangs;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
            return null;
        }
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
//                ("select * from PhieuTraKhachHang");
//        List<PhieuTraKhachHang> phieuTraKhachHangs = new ArrayList<>();
//        while(resultSet.next()) {
//            PhieuTraKhachHang phieuTraKhachHang = new PhieuTraKhachHang(
//                    resultSet.getString("MaPhieuTraKH"),
//                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
//                    new HoaDonImp().timKiem(resultSet.getString("MaHD"))
//            );
//
//            phieuTraKhachHangs.add(phieuTraKhachHang);
//        }
//        return phieuTraKhachHangs;
    }

    @Override
    public List<PhieuTraKhachHang> timKiem(Map<String, Object> conditions) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from PhieuTraKhachHang where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + (column + " like '%" + value + "%'"));
            isNeedAnd.set(true);
        });
        List<PhieuTraKhachHang> phieuTraKhachHangs = new ArrayList<>();
        try {
            phieuTraKhachHangs = em.createQuery(query.get(), PhieuTraKhachHang.class).getResultList();
            return phieuTraKhachHangs;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            PhieuTraKhachHang phieuTraKhachHang = new PhieuTraKhachHang(
//                    resultSet.getString("MaPhieuTraKH"),
//                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
//                    new HoaDonImp().timKiem(resultSet.getString("MaHD"))
//            );
//
//            phieuTraKhachHangs.add(phieuTraKhachHang);
//        }
//        return phieuTraKhachHangs;
    }

    @Override
    public PhieuTraKhachHang timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            PhieuTraKhachHang phieuTraKhachHang = em.find(PhieuTraKhachHang.class, id);
            et.commit();
            return phieuTraKhachHang;
        }catch (Exception e) {
            et.rollback();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select * from PhieuTraKhachHang where MaPhieuTraKH = ?");
//        preparedStatement.setString(1, id);
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//        if(resultSet.next()) {
//            return Optional.of(new PhieuTraKhachHang(
//                    resultSet.getString("MaPhieuTraKH"),
//                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
//                    new HoaDonImp().timKiem(resultSet.getString("MaHD"))
//            )).get();
//        }
//        return null;
    }

    @Override
    public List<PhieuTraKhachHang> timKiem(String... ids) throws RemoteException {
        String query = "select * from PhieuTraKhachHang where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("ma_phieu_tra_khach_hang = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<PhieuTraKhachHang> phieuTraKhachHangs = new ArrayList<>();
        try {
            phieuTraKhachHangs = em.createNativeQuery(query, PhieuTraKhachHang.class).getResultList();
            return phieuTraKhachHangs;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            PhieuTraKhachHang phieuTraKhachHang = new PhieuTraKhachHang(
//                    resultSet.getString("MaPhieuTraKH"),
//                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
//                    new HoaDonImp().timKiem(resultSet.getString("MaHD"))
//            );
//
//            phieuTraKhachHangs.add(phieuTraKhachHang);
//        }
//        return phieuTraKhachHangs;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);
        AtomicBoolean canAnd = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from PhieuTraKhachHang where ");
        conditions.forEach((column, value) -> {
            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
            canAnd.set(true);
        });

        List<Map<String, Object>> listResult = new ArrayList<>();
        Query q = em.createNativeQuery(query.get());
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
