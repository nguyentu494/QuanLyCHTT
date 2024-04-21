package dev.skyherobrine.app.daos.person;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.NhaCungCapDAO;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import dev.skyherobrine.app.enums.TinhTrangNhaCungCap;
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

public class NhaCungCapImp extends UnicastRemoteObject implements NhaCungCapDAO<NhaCungCap> {
    private EntityManager em;

    public NhaCungCapImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(NhaCungCap nhaCungCap) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(nhaCungCap);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert NhaCungCap values(?, ?, ?, ?, ?)");
//        preparedStatement.setString(1, nhaCungCap.getMaNCC());
//        preparedStatement.setString(2, nhaCungCap.getTenNCC());
//        preparedStatement.setString(3, nhaCungCap.getDiaChiNCC());
//        preparedStatement.setString(4, nhaCungCap.getEmail());
//        preparedStatement.setString(5, nhaCungCap.getTinhTrang().toString());
//
//        int result = preparedStatement.executeUpdate();
//        return result > 0;
    }

    @Override
    public boolean capNhat(NhaCungCap target) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(target);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Update NhaCungCap set TenNCC = ?, DiaChiNCC = ?, Email = ?, TinhTrang = ? where MaNCC = ?");
//        preparedStatement.setString(1, target.getMaNCC());
//        preparedStatement.setString(2, target.getDiaChiNCC());
//        preparedStatement.setString(3, target.getEmail());
//        preparedStatement.setString(4, target.getTinhTrang().toString());
//        preparedStatement.setString(5, target.getMaNCC());
//
//        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean xoa(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            NhaCungCap nhaCungCap = em.find(NhaCungCap.class, id);
            em.remove(nhaCungCap);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Update NhaCungCap set TinhTrang = ? where MaNCC = ?");
//        preparedStatement.setString(1, "CHAM_DUT");
//        preparedStatement.setString(2, id);
//
//        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public int xoa(String... ids) throws RemoteException {
        return 0;
    }

    @Override
    public List<NhaCungCap> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<NhaCungCap> nhaCungCaps = em.createNamedQuery("NhaCungCap.findAll", NhaCungCap.class).getResultList();
            et.commit();
            return nhaCungCaps;
        } catch (Exception e) {
            et.rollback();
            return new ArrayList<>();
        }
//        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
//        ResultSet result = connectDB.getConnection().createStatement().executeQuery("select * from NhaCungCap");
//        while (result.next()) {
//            NhaCungCap nhaCungCap = new NhaCungCap(result.getString("MaNCC"),
//                    result.getString("TenNCC"), result.getString("DiaChiNCC"),
//                    result.getString("Email"), TinhTrangNhaCungCap.layGiaTri(result.getString("TinhTrang")));
//
//            nhaCungCaps.add(nhaCungCap);
//        }
//        return nhaCungCaps;
    }

    @Override
    public List<NhaCungCap> timKiem(Map<String, Object> conditions) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from NhaCungCap ncc where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("ncc." + column + " like N'%" + value + "%'"));
            isNeedAnd.set(true);
        });

        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
        try {
            nhaCungCaps = em.createNativeQuery(query.get(), NhaCungCap.class).getResultList();
            return nhaCungCaps;
        } catch (Exception e) {
            return nhaCungCaps;
        }
//        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//        ResultSet result = preparedStatement.executeQuery();
//        while(result.next()) {
//            NhaCungCap nhaCungCap = new NhaCungCap(
//                    result.getString("MaNCC"),
//                    result.getString("TenNCC"),
//                    result.getString("DiaChiNCC"),
//                    result.getString("Email"),
//                    TinhTrangNhaCungCap.layGiaTri(result.getString("Tinh_trang"))
//            );
//            nhaCungCaps.add(nhaCungCap);
//        }
//        return nhaCungCaps;
    }

    @Override
    public NhaCungCap timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            NhaCungCap nhaCungCap = em.createNamedQuery("NhaCungCap.findByID", NhaCungCap.class).setParameter("maNCC", id).getSingleResult();
            et.commit();
            return nhaCungCap;
        } catch (Exception e) {
            et.rollback();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select * from NhaCungCap NCC where NCC.MaNCC = ?");
//        preparedStatement.setString(1, id);
//
//        ResultSet result = preparedStatement.executeQuery();
//        if (result.next()) {
//            return Optional.of(new NhaCungCap(result.getString("MaNCC"),
//                    result.getString("TenNCC"), result.getString("DiaChiNCC"),
//                    result.getString("Email"), TinhTrangNhaCungCap.layGiaTri(result.getString("Tinh_Trang")))).get();
//        } else {
//            return (NhaCungCap) Optional.empty().get();
//        }
    }

    @Override
    public List<NhaCungCap> timKiem(String... ids) throws RemoteException {
        String query = "select * from NhaCungCap ncc where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for (int i = 0; i < listID.length; ++i) {
            query += ("NCC.ma_ncc like '%" + listID[i] + "%'");
            if ((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
        try {
            nhaCungCaps = em.createNativeQuery(query, NhaCungCap.class).getResultList();
            return nhaCungCaps;
        } catch (Exception e) {
            return nhaCungCaps;
        }


//        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
//        ResultSet result = preparedStatement.executeQuery();
//        while (result.next()) {
//            NhaCungCap nhaCungCap = new NhaCungCap(result.getString("MaNCC"),
//                    result.getString("TenNCC"), result.getString("DiaChiNCC"),
//                    result.getString("Email"), TinhTrangNhaCungCap.layGiaTri(result.getString("TinhTrang")));
//            nhaCungCaps.add(nhaCungCap);
//        }
//        return nhaCungCaps;
//        return null;
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

        query.set(query.get() + " from NhaCungCap where ");
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
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery(query.get());
//
//        List<Map<String, Object>> listResult = new ArrayList<>();
//        while (resultSet.next()) {
//            Map<String, Object> rowDatas = new HashMap<>();
//            for (String column : Arrays.stream(colNames).toList()) {
//                rowDatas.put(column, resultSet.getString(column));
//            }
//            listResult.add(rowDatas);
//        }
//        return listResult;
//        return null;
    }
}
