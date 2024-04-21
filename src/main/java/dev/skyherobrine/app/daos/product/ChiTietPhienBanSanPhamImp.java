package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ChiTietPhienBanSanPhamDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHangPhienBanSP;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import dev.skyherobrine.app.enums.MauSac;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietPhienBanSanPhamImp extends UnicastRemoteObject implements ChiTietPhienBanSanPhamDAO<ChiTietPhienBanSanPham> {
    private EntityManager em;

    public ChiTietPhienBanSanPhamImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(ChiTietPhienBanSanPham chiTietPhienBanSanPham) throws Exception {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(chiTietPhienBanSanPham);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhat(ChiTietPhienBanSanPham target) throws Exception {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(target);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoa(String id) throws Exception {
//      PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//              ("Delete from PhienBanSanPham where MaPhienBanSP = ?");
//      preparedStatement.setString(1, id);
//
//      return preparedStatement.executeUpdate() > 0;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ChiTietPhienBanSanPham ctpbsp = em.find(ChiTietPhienBanSanPham.class, id);
            em.remove(ctpbsp);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<ChiTietPhienBanSanPham> timKiem() throws Exception {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<ChiTietPhienBanSanPham> chiTietPhienBanSanPhams = em.createNamedQuery("ChiTietPhienBanSanPham.findAll",
                    ChiTietPhienBanSanPham.class).getResultList();
            tx.commit();
            return chiTietPhienBanSanPhams;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return null;
        }
//      ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery("select * from PhienBanSanPham");
//      List<ChiTietPhienBanSanPham> chiTietPhienBanSanPhams = new ArrayList<>();
//      while(resultSet.next()) {
//          ChiTietPhienBanSanPham chiTietPhienBanSanPham = new ChiTietPhienBanSanPham(
//                  resultSet.getString("MaPhienBanSP"),
//                  new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
//                  MauSac.layGiaTri(resultSet.getString("MauSac")),
//                  resultSet.getString("KichThuoc"),
//                  resultSet.getInt("SoLuong"),
//                  resultSet.getString("HinhAnh")
//          );
//
//          chiTietPhienBanSanPhams.add(chiTietPhienBanSanPham);
//      }
//      return chiTietPhienBanSanPhams;

//        return em.createQuery("select n from ChiTietPhienBanSanPham n", ChiTietPhienBanSanPham.class).getResultList();
    }

    @Override
    public List<ChiTietPhienBanSanPham> timKiem(Map<String, Object> conditions) throws Exception {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>
                ("select pbsp from ChiTietPhienBanSanPham pbsp where 1 = 1");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                query.set(query + " AND pbsp."+key+" LIKE :"+key);
            }
        }
        Query q = em.createQuery(query.get());


        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }
        List<ChiTietPhienBanSanPham> chiTietPhienBanSanPhams = new ArrayList<>();
        try {
            chiTietPhienBanSanPhams = em.createQuery(query.get(), ChiTietPhienBanSanPham.class).getResultList();
            return chiTietPhienBanSanPhams;
        } catch (Exception e) {
            return chiTietPhienBanSanPhams;
        }
//      AtomicReference<String> query = new AtomicReference<>
//              ("select * from PhienBanSanPham t where ");
//      AtomicBoolean isNeedAnd = new AtomicBoolean(false);
//
//      conditions.forEach((column, value) -> {
//          query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("t." + column + " like '%" + value + "%'"));
//          isNeedAnd.set(true);
//      });
//
//      List<ChiTietPhienBanSanPham> chiTietPhienBanSanPhams = new ArrayList<>();
//      PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//      ResultSet resultSet = preparedStatement.executeQuery();
//      while(resultSet.next()) {
//          ChiTietPhienBanSanPham chiTietPhienBanSanPham = new ChiTietPhienBanSanPham(
//                  resultSet.getString("MaPhienBanSP"),
//                  new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
//                  MauSac.layGiaTri(resultSet.getString("MauSac")),
//                  resultSet.getString("KichThuoc"),
//                  resultSet.getInt("SoLuong"),
//                  resultSet.getString("HinhAnh")
//          );
//
//          chiTietPhienBanSanPhams.add(chiTietPhienBanSanPham);
//      }
//      return chiTietPhienBanSanPhams;

//        StringBuilder jpqlBuilder = new StringBuilder("select t from ChiTietPhienBanSanPham t where 1 = 1");
//
//        for (String key : conditions.keySet()) {
//            jpqlBuilder.append(" and t.").append(key).append(" = :").append(key);
//        }
//
//        Query query = em.createQuery(jpqlBuilder.toString(), ChiTietPhienBanSanPham.class);
//
//        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
//            query.setParameter(entry.getKey(), entry.getValue());
//        }
//
//        List<ChiTietPhienBanSanPham> resultList = query.getResultList();
//
//        return resultList.isEmpty() ? null : resultList;
    }

    @Override
    public ChiTietPhienBanSanPham timKiem(String id) throws Exception {
//      PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//              ("select * from PhienBanSanPham where MaPhienBanSP = ?");
//      preparedStatement.setString(1, id);
//      ResultSet resultSet = preparedStatement.executeQuery();
//      if(resultSet.next()) {
//          return Optional.of(new ChiTietPhienBanSanPham(resultSet.getString("MaPhienBanSP"), new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
//                  MauSac.layGiaTri(resultSet.getString("MauSac")), resultSet.getString("KichThuoc"), resultSet.getInt("SoLuong"), resultSet.getString("HinhAnh")));
//      } else {
//          return Optional.empty();
//      }
        return Optional.of(em.find(ChiTietPhienBanSanPham.class, id)).get();
    }

    @Override
    public List<ChiTietPhienBanSanPham> timKiem(String... ids) throws Exception {
        return null;
    }

    public Optional<ChiTietPhienBanSanPham> timKiem(String maSP, MauSac mauSac, String kichThuoc) throws Exception{
//      PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//              ("select * from PhienBanSanPham where MaSP = ? and MauSac = ? and KichThuoc = ?");
//      preparedStatement.setString(1, maSP);
//      preparedStatement.setString(2, mauSac.toString());
//      preparedStatement.setString(3, kichThuoc);
//      ResultSet resultSet = preparedStatement.executeQuery();
//      if(resultSet.next()) {
//          return Optional.of(new ChiTietPhienBanSanPham(resultSet.getString("MaPhienBanSP"), new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
//                  MauSac.layGiaTri(resultSet.getString("MauSac")), resultSet.getString("KichThuoc"), resultSet.getInt("SoLuong"), resultSet.getString("HinhAnh")));
//      } else {
//          return Optional.empty();
//      }
        AtomicReference<String> query = new AtomicReference<>
                ("select * from ChiTietPhienBanSanPham pbsp where pbsp.ma_sp = ? and pbsp.mau_sac = ? and pbsp.kich_thuoc = ?");
        Query q = em.createNativeQuery(query.get(), ChiTietPhienBanSanPham.class);
        q.setParameter(1, maSP);
        q.setParameter(2, mauSac.toString());
        q.setParameter(3, kichThuoc);
        List<ChiTietPhienBanSanPham> results = q.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
//        StringBuilder jpqlBuilder = new StringBuilder("SELECT " + (isDuplicateResult ? "distinct " : ""));
//
//        for (int i = 0; i < colNames.length; i++) {
//            jpqlBuilder.append("t.").append(colNames[i]);
//            if (i < colNames.length - 1) {
//                jpqlBuilder.append(", ");
//            }
//        }
//
//        jpqlBuilder.append(" FROM ChiTietPhienBanSanPham t WHERE 1 = 1");
//
//        if (conditions != null && !conditions.isEmpty()) {
//            for (String key : conditions.keySet()) {
//                jpqlBuilder.append(" AND t.").append(key).append(" LIKE :").append(key);
//            }
//        }
//
//
//        Query query = em.createQuery(jpqlBuilder.toString());
//
//        if (conditions != null && !conditions.isEmpty()) {
//            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
//                query.setParameter(entry.getKey(), entry.getValue());
//            }
//        }
//        List<Object[]> results = query.getResultList();
//
//        List<Map<String, Object>> formattedResults = new ArrayList<>();
//        for (Object[] row : results) {
//            Map<String, Object> rowMap = new HashMap<>();
//            for (int i = 0; i < colNames.length; i++) {
//                rowMap.put(colNames[i], row[i]);
//            }
//            formattedResults.add(rowMap);
//        }
//
//
//        if (isDuplicateResult || !formattedResults.isEmpty()) {
//            return formattedResults;
//        } else {
//            return null;
//        }

        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);
        AtomicBoolean canAnd = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + "t."+column);
            canPhay.set(true);
        });

        query.set(query.get() + " from ChiTietPhienBanSanPham t where 1 = 1");
        System.out.println(query.get());

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
