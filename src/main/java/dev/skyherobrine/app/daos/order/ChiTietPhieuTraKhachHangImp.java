package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ChiTietPhieuTraKhachHangDAO;
import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.product.ChiTietPhienBanSanPhamImp;
import dev.skyherobrine.app.entities.Key.ChiTietPhieuTraKhachHangId;
import dev.skyherobrine.app.entities.order.ChiTietPhieuTraKhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietPhieuTraKhachHangImp extends UnicastRemoteObject implements ChiTietPhieuTraKhachHangDAO<ChiTietPhieuTraKhachHang> {
    private EntityManager em;

    public ChiTietPhieuTraKhachHangImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(ChiTietPhieuTraKhachHang chiTietPhieuTraKhachHang) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(chiTietPhieuTraKhachHang);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }

//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert ChiTietPhieuTraKhachHang values(?, ?, ?, ?)");
//        preparedStatement.setString(1, chiTietPhieuTraKhachHang.getPhieuTra().getMaPhieuTraKhachHang());
//        preparedStatement.setString(2, chiTietPhieuTraKhachHang.getChiTietPhienBanSanPham().getMaPhienBanSP());
//        preparedStatement.setInt(3, chiTietPhieuTraKhachHang.getSoLuongTra());
//        preparedStatement.setString(4, chiTietPhieuTraKhachHang.getNoiDungTra());
//
//        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean capNhat(ChiTietPhieuTraKhachHang target) throws RemoteException {
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
    public List<ChiTietPhieuTraKhachHang> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<ChiTietPhieuTraKhachHang> chiTietPhieuTraKhachHangs = em.createNamedQuery("ChiTietPhieuTraKhachHang.findAll",
                    ChiTietPhieuTraKhachHang.class).getResultList();
            et.commit();
            return chiTietPhieuTraKhachHangs;
        }catch (Exception e){
            et.rollback();
            return null;
        }
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery("select * from ChiTietPhieuTraKhachHang");
//        List<ChiTietPhieuTraKhachHang> chiTietPhieuTraKhachHangs = new ArrayList<>();
//        while(resultSet.next()) {
//            ChiTietPhieuTraKhachHang chiTietPhieuTraKhachHang = new ChiTietPhieuTraKhachHang(
//                    new PhieuTraKhachHangDAO().timKiem(resultSet.getString("MaPhieuTraKH")).get(),
//                    new ChiTietPhienBanSanPhamDAO().timKiem(resultSet.getString("MaPhienBanSP")).get(),
//                    resultSet.getInt("SoLuongTra"),
//                    resultSet.getString("NoiDungTra")
//            );
//
//            chiTietPhieuTraKhachHangs.add(chiTietPhieuTraKhachHang);
//        }
//        return chiTietPhieuTraKhachHangs;
    }

    @Override
    public List<ChiTietPhieuTraKhachHang> timKiem(Map<String, Object> conditions) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from ChiTietPhieuTraKhachHang ctptkh where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);
        System.out.println(query.get().toString());
        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("ctptkh." + column + " like '%" + value + "%'"));
            isNeedAnd.set(true);
        });
        List<ChiTietPhieuTraKhachHang> chiTietPhieuTraKhachHangs = new ArrayList<>();
        try {
            chiTietPhieuTraKhachHangs = em.createQuery(query.get(), ChiTietPhieuTraKhachHang.class).getResultList();
            return chiTietPhieuTraKhachHangs;
        }catch (Exception e){
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            ChiTietPhieuTraKhachHang chiTietPhieuTraKhachHang = new ChiTietPhieuTraKhachHang(
//                    new PhieuTraKhachHangDAO().timKiem(resultSet.getString("MaPhieuTraKH")).get(),
//                    new ChiTietPhienBanSanPhamDAO().timKiem(resultSet.getString("MaPhienBanSP")).get(),
//                    resultSet.getInt("SoLuongTra"),
//                    resultSet.getString("NoiDungTra")
//            );
//
//            chiTietPhieuTraKhachHangs.add(chiTietPhieuTraKhachHang);
//        }
//        return chiTietPhieuTraKhachHangs;
    }

    @Override
    public ChiTietPhieuTraKhachHang timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            ChiTietPhieuTraKhachHang chiTietPhieuTraKhachHang = em.find(ChiTietPhieuTraKhachHang.class, id);
            et.commit();
            return chiTietPhieuTraKhachHang;
        }catch (Exception e){
            et.rollback();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select * from ChiTietPhieuTraKhachHang where MaPhieuTraKH = ?");
//        preparedStatement.setString(1, id);
//
//        ResultSet resultSet = preparedStatement.executeQuery();
//        if(resultSet.next()) {
//            return Optional.of(new ChiTietPhieuTraKhachHang(
//                    new ChiTietPhieuTraKhachHangId(new PhieuTraKhachHangImp().timKiem(resultSet.getString("MaPhieuTraKH")),
//                            new ChiTietPhienBanSanPhamImp().timKiem(resultSet.getString("MaPhienBanSP"))),
//                    resultSet.getInt("SoLuongTra"),
//                    resultSet.getString("NoiDungTra")
//            )).get();
//        }
//        return null;
    }

    @Override
    public List<ChiTietPhieuTraKhachHang> timKiem(String... ids) throws RemoteException {
        return null;
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

        query.set(query.get() + " from ChiTietPhieuTraKhachHang where ");
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
