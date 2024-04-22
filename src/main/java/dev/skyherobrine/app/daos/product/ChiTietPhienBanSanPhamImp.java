package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ChiTietPhienBanSanPhamDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
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
    private static EntityManager em;

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
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ChiTietPhienBanSanPham ctpbsp = em.find(ChiTietPhienBanSanPham.class, id);
            em.remove(ctpbsp);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            return false;
        }
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
            return null;
        }
    }

    @Override
    public List<ChiTietPhienBanSanPham> timKiem(Map<String, Object> conditions) throws Exception {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select pbsp from ChiTietPhienBanSanPham pbsp where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND pbsp."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND pbsp."+ key +" LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get(), ChiTietPhienBanSanPham.class);


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
        System.out.println(query.get());
        List<ChiTietPhienBanSanPham> chiTietPhienBanSanPhams = new ArrayList<>();
        try {
            tx.begin();
            chiTietPhienBanSanPhams = q.getResultList();
            tx.commit();
            return chiTietPhienBanSanPhams;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public ChiTietPhienBanSanPham timKiem(String id) throws Exception {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            ChiTietPhienBanSanPham chiTietPhienBanSanPham = em.find(ChiTietPhienBanSanPham.class, id);
            et.commit();
            return chiTietPhienBanSanPham;
        }catch (Exception e) {
            et.rollback();
            return null;
        }
    }

    @Override
    public List<ChiTietPhienBanSanPham> timKiem(String... ids) throws Exception {
        return null;
    }

    public Optional<ChiTietPhienBanSanPham> timKiem(String maSP, MauSac mauSac, String kichThuoc) throws Exception{
        AtomicReference<String> query = new AtomicReference<>
                ("select ctpnsp from ChiTietPhienBanSanPham ctpnsp where 1 = 1");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        if (maSP != null) {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + "ctpnsp.MaSP = '" + maSP + "'");
            isNeedAnd.set(true);
        }
        if (mauSac != null) {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + "ctpnsp.MauSac = '" + mauSac.toString() + "'");
            isNeedAnd.set(true);
        }
        if (kichThuoc != null) {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + "ctpnsp.KichThuoc = '" + kichThuoc + "'");
            isNeedAnd.set(true);
        }

        List<ChiTietPhienBanSanPham> chiTietPhienBanSanPhams = new ArrayList<>();
        Query q = em.createQuery(query.get());
        try {
            chiTietPhienBanSanPhams = q.getResultList();
            return chiTietPhienBanSanPhams.isEmpty() ? Optional.empty() : Optional.of(chiTietPhienBanSanPhams.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + "t."+column);
            canPhay.set(true);
        });

        query.set(query.get() + " from ChiTietPhienBanSanPham t where 1 = 1");

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
        System.out.println(query.get());
        List<Map<String, Object>> listResult = new ArrayList<>();
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
