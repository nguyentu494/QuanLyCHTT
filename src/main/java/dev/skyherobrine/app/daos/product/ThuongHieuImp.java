package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.ThuongHieuDAO;
import dev.skyherobrine.app.entities.product.SanPham;
import dev.skyherobrine.app.entities.product.ThuongHieu;
import dev.skyherobrine.app.enums.TinhTrangThuongHieu;
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

public class ThuongHieuImp extends UnicastRemoteObject implements ThuongHieuDAO<ThuongHieu> {
    private EntityManager em;

    public ThuongHieuImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(ThuongHieu thuongHieu) throws Exception {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(thuongHieu);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhat(ThuongHieu target) throws Exception {
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
    public List<ThuongHieu> timKiem() throws Exception {
        return em.createQuery("select th from ThuongHieu th", ThuongHieu.class).getResultList();
    }

    @Override
    public List<ThuongHieu> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from ThuongHieu th where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("th." + column + " = N'" + value + "'"));
            isNeedAnd.set(true);
        });

        List<ThuongHieu> thuongHieus = new ArrayList<>();
        try {
            thuongHieus = em.createNativeQuery(query.get(), ThuongHieu.class).getResultList();
            return thuongHieus;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }


//        StringBuilder jpqlBuilder = new StringBuilder("select t from ThuongHieu t where 1 = 1");
//
//        for (String key : conditions.keySet()) {
//            jpqlBuilder.append(" and t.").append(key).append(" = :").append(key);
//        }
//
//        Query query = em.createQuery(jpqlBuilder.toString(), ThuongHieu.class);
//
//        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
//            query.setParameter(entry.getKey(), entry.getValue());
//        }
//
//        List<ThuongHieu> resultList = query.getResultList();
//
//        return resultList.isEmpty() ? null : resultList;
    }

    @Override
    public ThuongHieu timKiem(String id) throws Exception {
        return Optional.of(em.find(ThuongHieu.class, id)).get();
    }

    @Override
    public List<ThuongHieu> timKiem(String... ids) throws Exception {
        List<ThuongHieu> th = new ArrayList<>();
        for (String id : ids) {
            th.add(em.find(ThuongHieu.class, id));
        }
        return th;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult,
                                             String... colNames) throws Exception {

        StringBuilder jpqlBuilder = new StringBuilder("SELECT " + (isDuplicateResult ? "distinct " : ""));

        for (int i = 0; i < colNames.length; i++) {
            jpqlBuilder.append("t.").append(colNames[i]);
            if (i < colNames.length - 1) {
                jpqlBuilder.append(", ");
            }
        }

        jpqlBuilder.append(" FROM ThuongHieu t WHERE 1 = 1");

        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                jpqlBuilder.append(" AND t.").append(key).append(" = :").append(key);
            }
        }

        Query query = em.createQuery(jpqlBuilder.toString());

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
