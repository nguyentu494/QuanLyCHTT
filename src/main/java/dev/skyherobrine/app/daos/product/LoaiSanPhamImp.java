package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.LoaiSanPhamDAO;
import dev.skyherobrine.app.entities.order.PhieuTraKhachHang;
import dev.skyherobrine.app.entities.product.LoaiSanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LoaiSanPhamImp extends UnicastRemoteObject implements LoaiSanPhamDAO<LoaiSanPham> {
    private EntityManager em;

    public LoaiSanPhamImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(LoaiSanPham loaiSanPham) throws Exception {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(loaiSanPham);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhat(LoaiSanPham target) throws Exception {
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
        return false;
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<LoaiSanPham> timKiem() throws Exception {
        return em.createQuery("select l from LoaiSanPham l", LoaiSanPham.class).getResultList();
    }

    @Override
    public List<LoaiSanPham> timKiem(Map<String, Object> conditions) throws Exception {
        StringBuilder jpqlBuilder = new StringBuilder("select t from LoaiSanPham t where 1 = 1");

        for (String key : conditions.keySet()) {
            jpqlBuilder.append(" and t.").append(key).append(" = :").append(key);
        }

        Query query = em.createQuery(jpqlBuilder.toString(), LoaiSanPham.class);

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<LoaiSanPham> resultList = query.getResultList();

        return resultList.isEmpty() ? null : resultList;
    }

    @Override
    public LoaiSanPham timKiem(String id) throws Exception {
        return Optional.of(em.find(LoaiSanPham.class, id)).get();
    }

    @Override
    public List<LoaiSanPham> timKiem(String... ids) throws Exception {
        List<LoaiSanPham> th = new ArrayList<>();
        for (String id : ids) {
            th.add(em.find(LoaiSanPham.class, id));
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

        jpqlBuilder.append(" FROM LoaiSanPham t WHERE 1 = 1");

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
