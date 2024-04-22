package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.LoaiSanPhamDAO;
import dev.skyherobrine.app.entities.order.PhieuTraKhachHang;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
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
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select lsp from LoaiSanPham lsp where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND lsp."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND lsp."+ key +" LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get(), LoaiSanPham.class);


        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if(entry.getKey().contains(".")) {
                    String ex = entry.getKey().substring(entry.getKey().lastIndexOf(".") + 1);
                    q.setParameter(ex, entry.getValue());
                }else{
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        List<LoaiSanPham> loaiSanPhams = new ArrayList<>();
        try {
            tx.begin();
            loaiSanPhams = q.getResultList();
            tx.commit();
            return loaiSanPhams;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
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
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + "t."+column);
            canPhay.set(true);
        });

        query.set(query.get() + " from LoaiSanPham t where 1 = 1");

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
