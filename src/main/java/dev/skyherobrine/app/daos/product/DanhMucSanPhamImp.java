package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.DanhMucSanPhamDAO;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.entities.product.DanhMucSanPham;
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

public class DanhMucSanPhamImp extends UnicastRemoteObject implements DanhMucSanPhamDAO<DanhMucSanPham> {
    private EntityManager em;

    public DanhMucSanPhamImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(DanhMucSanPham danhMucSanPham) throws Exception {
//      PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//              ("insert DanhMucSanPham values(?, ?)");
//      preparedStatement.setString(1, danhMucSanPham.getMaDM());
//      preparedStatement.setString(2, danhMucSanPham.getTenDM());
//      return preparedStatement.executeUpdate() > 0;
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(danhMucSanPham);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhat(DanhMucSanPham target) throws Exception {
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
    public List<DanhMucSanPham> timKiem() throws Exception {
//      ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
//              ("select * from DanhMucSanPham");
//      List<DanhMucSanPham> danhMucSanPhams = new ArrayList<>();
//      while (resultSet.next()) {
//          DanhMucSanPham danhMucSanPham = new DanhMucSanPham(resultSet.getString("MaDM"), resultSet.getString("TenDM"));
//          danhMucSanPhams.add(danhMucSanPham);
//      }
//      return danhMucSanPhams;
        return em.createQuery("select d from DanhMucSanPham d", DanhMucSanPham.class).getResultList();
    }

    @Override
    public List<DanhMucSanPham> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from DanhMucSanPham dm where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("dm." + column + " = N'" + value + "'"));
            isNeedAnd.set(true);
        });

        List<DanhMucSanPham> danhMucSanPhams = new ArrayList<>();
        try {
            danhMucSanPhams = em.createNativeQuery(query.get(), DanhMucSanPham.class).getResultList();
            return danhMucSanPhams;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//      PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//      ResultSet result = preparedStatement.executeQuery();
//      while(result.next()) {
//          DanhMucSanPham danhMucSanPham = new DanhMucSanPham(
//                  result.getString("MaDM"),
//                  result.getString("TenDM"));
//          danhMucSanPhams.add(danhMucSanPham);
//      }
//      return danhMucSanPhams;
//        StringBuilder jpqlBuilder = new StringBuilder("select t from DanhMucSanPham t where 1 = 1");
//
//        for (String key : conditions.keySet()) {
//            jpqlBuilder.append(" and t.").append(key).append(" = :").append(key);
//        }
//
//        Query query = em.createQuery(jpqlBuilder.toString(), DanhMucSanPham.class);
//
//        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
//            query.setParameter(entry.getKey(), entry.getValue());
//        }
//
//        List<DanhMucSanPham> resultList = query.getResultList();
//
//        return resultList.isEmpty() ? null : resultList;
    }

    @Override
    public DanhMucSanPham timKiem(String id) throws Exception {
//      PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//              ("select * from DanhMucSanPham where MaDM = ?");
//      preparedStatement.setString(1, id);
//      ResultSet resultSet = preparedStatement.executeQuery();
//      if(resultSet.next()) {
//          DanhMucSanPham danhMucSanPham = new DanhMucSanPham(resultSet.getString("MaDM"), resultSet.getString("TenDM"));
//          return Optional.of(danhMucSanPham);
//      } else {
//          return Optional.empty();
//      }
        return Optional.of(em.find(DanhMucSanPham.class, id)).get();
    }

    @Override
    public List<DanhMucSanPham> timKiem(String... ids) throws Exception {
//      String query = "select * from DanhMucSanPham where ";
//      String[] listID = (String[]) Arrays.stream(ids).toArray();
//      for(int i = 0; i < listID.length; ++i) {
//          query += ("MaDM = '" + listID[i] + "'");
//          if((i + 1) >= listID.length) break;
//          else query += ", ";

        List<DanhMucSanPham> dmsps = new ArrayList<>();
        for (String id : ids) {
            dmsps.add(em.find(DanhMucSanPham.class, id));
        }
        return dmsps;

    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {

        StringBuilder jpqlBuilder = new StringBuilder("SELECT " + (isDuplicateResult ? "distinct " : ""));

        for (int i = 0; i < colNames.length; i++) {
            jpqlBuilder.append("t.").append(colNames[i]);
            if (i < colNames.length - 1) {
                jpqlBuilder.append(", ");
            }
        }

        jpqlBuilder.append(" FROM DanhMucSanPham t WHERE 1 = 1");

        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                jpqlBuilder.append(" AND t.").append(key).append(" = :").append(key);
            }
        }

        Query query = em.createNativeQuery(jpqlBuilder.toString());

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        List<Object[]> results = query.getResultList();

        List<Map<String, Object>> formattedResults = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 0; i < colNames.length; i++) {
                rowMap.put(colNames[i], row[i]);
            }
            formattedResults.add(rowMap);
        }

        if (isDuplicateResult || !formattedResults.isEmpty()) {
            return formattedResults;
        } else {
            return null;
        }
    }
}
