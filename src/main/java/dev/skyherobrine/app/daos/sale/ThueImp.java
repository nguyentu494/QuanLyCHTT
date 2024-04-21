package dev.skyherobrine.app.daos.sale;

import dev.skyherobrine.app.daos.ThueDAO;
import dev.skyherobrine.app.entities.product.SanPham;
import dev.skyherobrine.app.entities.sale.Thue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ThueImp extends UnicastRemoteObject implements ThueDAO<Thue> {

    private EntityManager em;

    public ThueImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(Thue thue) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(thue);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhat(Thue target) throws RemoteException {
        return false;
    }

    public boolean update(String HieuLuc) {

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(HieuLuc);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
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
    public List<Thue> timKiem() throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Thue> thues = em.createNamedQuery("Thue.findAll", Thue.class).getResultList();
            tx.commit();
            return thues;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return null;
        }
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

        query.set(query.get() + " from Thue where ");
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

//        StringBuilder jpqlBuilder = new StringBuilder("SELECT " + (isDuplicateResult ? "distinct " : ""));
//
//        for (int i = 0; i < colNames.length; i++) {
//            jpqlBuilder.append("t.").append(colNames[i]);
//            if (i < colNames.length - 1) {
//                jpqlBuilder.append(", ");
//            }
//        }
//
//        jpqlBuilder.append(" FROM Thue t WHERE 1 = 1");
//
//        if (conditions != null && !conditions.isEmpty()) {
//            for (String key : conditions.keySet()) {
//                jpqlBuilder.append(" AND t.").append(key).append(" = :").append(key);
//            }
//        }
//
//        Query query = em.createQuery(jpqlBuilder.toString());
//
//        if (conditions != null && !conditions.isEmpty()) {
//            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
//                query.setParameter(entry.getKey(), entry.getValue());
//            }
//        }
//
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
//        if (isDuplicateResult || !formattedResults.isEmpty()) {
//            return formattedResults;
//        } else {
//            return null;
//        }
    }


    @Override
    public List<Thue> timKiem(Map<String, Object> conditions) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from Thue t where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("t." + column + " = N'" + value + "'"));
            isNeedAnd.set(true);
        });

        List<Thue> thues = new ArrayList<>();
        try {
            thues = em.createNativeQuery(query.get(), Thue.class).getResultList();
            return thues;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        StringBuilder jpqlBuilder = new StringBuilder("select t from Thue t where 1 = 1");
//
//        for (String key : conditions.keySet()) {
//            jpqlBuilder.append(" and t.").append(key).append(" = :").append(key);
//        }
//
//        Query query = em.createQuery(jpqlBuilder.toString(), Thue.class);
//
//        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
//            query.setParameter(entry.getKey(), entry.getValue());
//        }
//
//        List<Thue> resultList = query.getResultList();
//
//        return resultList.isEmpty() ? null : resultList;
    }


    @Override
    public Thue timKiem(String id) throws Exception {

        return Optional.of(em.find(Thue.class, id)).get();
    }


    @Override
    public List<Thue> timKiem(String... ids) throws Exception {
        List<Thue> thues = new ArrayList<>();
        for (String id : ids) {
            thues.add(em.find(Thue.class, id));
        }
        return thues;
    }
}
