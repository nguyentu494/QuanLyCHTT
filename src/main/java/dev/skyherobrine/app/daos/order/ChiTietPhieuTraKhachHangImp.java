package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ChiTietPhieuTraKhachHangDAO;
import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.product.ChiTietPhienBanSanPhamImp;
import dev.skyherobrine.app.entities.Key.ChiTietPhieuTraKhachHangId;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHangPhienBanSP;
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
    }

    @Override
    public boolean capNhat(ChiTietPhieuTraKhachHang target) throws RemoteException {
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
    }

    @Override
    public List<ChiTietPhieuTraKhachHang> timKiem(Map<String, Object> conditions) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>
                ("select ctptkh from ChiTietPhieuTraKhachHang ctptkh where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND ctptkh."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND ctptkh."+ key +" LIKE :"+ key);
                }
            }
        }

        Query q = em.createQuery(query.get(), ChiTietPhieuTraKhachHang.class);

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if(entry.getKey().contains(".")){
                    String ex = entry.getKey().substring(entry.getKey().lastIndexOf(".") + 1);
                    q.setParameter(ex, entry.getValue());
                }else{
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        List<ChiTietPhieuTraKhachHang> chiTietPhieuTraKhachHangs = new ArrayList<>();
        try {
            tx.begin();
            chiTietPhieuTraKhachHangs = q.getResultList();
            tx.commit();
            return chiTietPhieuTraKhachHangs;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
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
    }

    @Override
    public List<ChiTietPhieuTraKhachHang> timKiem(String... ids) throws RemoteException {
        return null;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from ChiTietPhieuTraKhachHang t where 1 = 1");

        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND t."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND t."+ key +" LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get());

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
