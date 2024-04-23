package dev.skyherobrine.app.daos.person;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.KhachHangDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import dev.skyherobrine.app.entities.person.KhachHang;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
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
    }

    @Override
    public List<KhachHang> timKiem(Map<String, Object> conditions) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select kh from KhachHang kh where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND kh."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND kh."+ key +" LIKE :"+ key);
                }
            }
        }

        Query q = em.createQuery(query.get(), KhachHang.class);

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if(entry.getKey().contains(".")){
                    String ex = entry.getKey().substring(entry.getKey().indexOf(".") + 1);
                    q.setParameter(ex, entry.getValue());
                }else{
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        List<KhachHang> khachHangs = new ArrayList<>();
        try {
            tx.begin();
            khachHangs = q.getResultList();
            tx.commit();
            return khachHangs;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
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
    }

    @Override
    public List<KhachHang> timKiem(String... ids) throws RemoteException {
        String query = "select kh from KhachHang kh where 1 = 1";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
           for (int i = 0; i < listID.length; i++) {
                query = query + " AND kh.id = :id" + i;
            }
        Query q = em.createQuery(query);
        for (int i = 0; i < listID.length; i++) {
            q.setParameter("id" + i, listID[i]);
        }
        List<KhachHang> khachHangs = new ArrayList<>();
        try {
            khachHangs = q.getResultList();
            return khachHangs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from KhachHang t where 1 = 1");

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
