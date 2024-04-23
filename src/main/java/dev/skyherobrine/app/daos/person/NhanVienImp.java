package dev.skyherobrine.app.daos.person;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.daos.NhanVienDAO;
import dev.skyherobrine.app.entities.person.KhachHang;
import dev.skyherobrine.app.entities.person.NhanVien;
import dev.skyherobrine.app.enums.CaLamViec;
import dev.skyherobrine.app.enums.ChucVu;
import dev.skyherobrine.app.enums.TinhTrangNhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import javax.swing.text.html.Option;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class NhanVienImp extends UnicastRemoteObject implements NhanVienDAO<NhanVien> {
    private EntityManager em;

    public NhanVienImp() throws RemoteException{
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(NhanVien nhanVien) throws Exception {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(nhanVien);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhat(NhanVien target) throws Exception {
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
            NhanVien nhanVien = em.find(NhanVien.class, id);
            nhanVien.setTinhTrang(TinhTrangNhanVien.NGHI);
            em.merge(nhanVien);
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
    public List<NhanVien> timKiem() throws Exception {
        return em.createQuery("select n from NhanVien n", NhanVien.class).getResultList();
    }

    @Override
    public List<NhanVien> timKiem(Map<String, Object> conditions) throws Exception {

        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select kh from NhanVien kh where 1 = 1");
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

        Query q = em.createQuery(query.get(), NhanVien.class);

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
        List<NhanVien> nhanViens = new ArrayList<>();
        try {
            tx.begin();
            nhanViens = q.getResultList();
            tx.commit();
            return nhanViens;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public NhanVien timKiem(String id) throws Exception {
        Optional<NhanVien> nhanVien = Optional.of(em.find(NhanVien.class, id));

        NhanVien nv = nhanVien.get();
        return nv;
    }

    @Override
    public List<NhanVien> timKiem(String... ids) throws Exception {
        List<NhanVien> nhanViens = new ArrayList<>();
        for (String id : ids) {
            nhanViens.add(em.find(NhanVien.class, id));
        }
        return nhanViens;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult,
                                             String... colNames) throws Exception {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from NhanVien t where 1 = 1");

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
