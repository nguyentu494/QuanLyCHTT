package dev.skyherobrine.app.daos.person;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.KhachHangDAO;
import dev.skyherobrine.app.entities.person.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class KhachHangImp extends UnicastRemoteObject implements KhachHangDAO<KhachHang> {
    private EntityManager em;

    public KhachHangImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(KhachHang khachHang) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(khachHang);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("insert KhachHang values(?, ?, ?, ?, ?, ?)");
//        preparedStatement.setString(1, khachHang.getMaKH());
//        preparedStatement.setString(2, khachHang.getHoTen());
//        preparedStatement.setString(3, khachHang.getSoDienThoai());
//        preparedStatement.setBoolean(4, khachHang.isGioiTinh());
//        preparedStatement.setDate(5, Date.valueOf(khachHang.getNgaySinh()));
//        preparedStatement.setFloat(6, khachHang.getDiemTichLuy());
//
//        int result = preparedStatement.executeUpdate();
//        return result > 0;
    }

    @Override
    public boolean capNhat(KhachHang target) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(target);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Update KhachHang  set HoTen = ?," +
//                        "SoDienThoai = ?, GioiTinh = ?, NgaySinh = ?," +
//                        "DiemTichLuy = ? where MaKH = ?");
//        preparedStatement.setString(1, target.getHoTen());
//        preparedStatement.setString(2, target.getSoDienThoai());
//        preparedStatement.setBoolean(3, target.isGioiTinh());
//        preparedStatement.setDate(4, Date.valueOf(target.getNgaySinh()));
//        preparedStatement.setFloat(5, target.getDiemTichLuy());
//        preparedStatement.setString(6, target.getMaKH());
//
//        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean xoa(String id) throws RemoteException {
        return false;
    }

    @Override
    public int xoa(String... ids) throws RemoteException {
        return 0;
    }

    @Override
    public List<KhachHang> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<KhachHang> khachHangs = em.createNamedQuery("KhachHang.findAll", KhachHang.class).getResultList();
            et.commit();
            return khachHangs;
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }
        return null;
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
//                ("select TOP(10) * from KhachHang ORDER BY DiemTichLuy DESC");
//        List<KhachHang> khachHangs = new ArrayList<>();
//        while (resultSet.next()) {
//            KhachHang khachHang = new KhachHang
//                    (resultSet.getString("MaKH"), resultSet.getString("HoTen"), resultSet.getString("SoDienThoai"),
//                            resultSet.getBoolean("GioiTinh"), resultSet.getDate("NgaySinh").toLocalDate(),
//                            resultSet.getFloat("DiemTichLuy"));
//            khachHangs.add(khachHang);
//        }
//        return khachHangs;
    }

    @Override
    public List<KhachHang> timKiem(Map<String, Object> conditions) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from KhachHang t where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("t." + column + "= '" + value +"'"));
            isNeedAnd.set(true);
        });
        List<KhachHang> khachHangs = new ArrayList<>();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();
            List<KhachHang> khachHangs1 = em.createNativeQuery(query.get(), KhachHang.class).getResultList();
            et.commit();
            return khachHangs1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//        ResultSet result = preparedStatement.executeQuery();
//        while(result.next()) {
//            KhachHang khachHang = new KhachHang(
//                    result.getString("MaKH"),
//                    result.getString("HoTen"),
//                    result.getString("SoDienThoai"),
//                    result.getBoolean("GioiTinh"),
//                    result.getDate("NgaySinh").toLocalDate(),
//                    result.getFloat("DiemTichLuy"));
//            khachHangs.add(khachHang);
//        }
//        return khachHangs;
    }

    @Override
    public KhachHang timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            KhachHang khachHang = em.find(KhachHang.class, id);
            et.commit();
            return khachHang;
        }catch (Exception e){
            et.rollback();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select * from KhachHang KH where KH.MaKH = ?");
//        preparedStatement.setString(1, id);
//        ResultSet result = preparedStatement.executeQuery();
//        if (result.next()) {
//            return Optional.of(new KhachHang(result.getString("MaKH"),
//                    result.getString("HoTen"), result.getString("SoDienThoai"),
//                    result.getBoolean("GioiTinh"), result.getDate("NgaySinh").toLocalDate(), result.getFloat("DiemTichLuy"))).get();
//        } else {
//            return (KhachHang) Optional.empty().get();
//        }
    }

    @Override
    public List<KhachHang> timKiem(String... ids) throws RemoteException {
        String query = "select * from KhachHang KH where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for (int i = 0; i < listID.length; ++i) {
            query += ("KH.ma_kh like '%" + listID[i] + "%'");
            if ((i + 1) >= listID.length) {
                break;
            } else {
                query += ", ";
            }
        }

        List<KhachHang> khachHangs = new ArrayList<>();
        try {
            EntityTransaction et = em.getTransaction();
            et.begin();
            khachHangs = em.createNativeQuery(query, KhachHang.class).getResultList();
            et.commit();
            return khachHangs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
//        List<KhachHang> khachHangs = new ArrayList<>();
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while (resultSet.next()) {
//            KhachHang khachHang = new KhachHang(resultSet.getString("MaKH"), resultSet.getString("HoTen"),
//                    resultSet.getString("SoDienThoai"), resultSet.getBoolean("GioiTinh"), resultSet.getDate("NgaySinh").toLocalDate(),
//                    resultSet.getFloat("DiemTichLuy"));
//            khachHangs.add(khachHang);
//        }
//        return khachHangs;
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

        query.set(query.get() + " from KhachHang where ");
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
