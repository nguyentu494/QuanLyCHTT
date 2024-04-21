package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.SanPhamDAO;
import dev.skyherobrine.app.entities.product.SanPham;
import dev.skyherobrine.app.enums.TinhTrangSanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SanPhamImp extends UnicastRemoteObject implements SanPhamDAO<SanPham> {
    private static EntityManager em;
    public SanPhamImp() throws Exception{
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(SanPham sanPham) throws Exception {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(sanPham);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert SanPham values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//        preparedStatement.setString(1, sanPham.getMaSP());
//        preparedStatement.setString(2, sanPham.getTenSP());
//        preparedStatement.setString(3, sanPham.getLoaiSanPham().getMaLoai());
//        preparedStatement.setString(4, sanPham.getPhongCachMac().toString());
//        preparedStatement.setString(5, sanPham.getDoTuoi().toString());
//        preparedStatement.setString(6, sanPham.getXuatXu());
//        preparedStatement.setString(7, sanPham.getThuongHieu().getMaTH());
//        preparedStatement.setDouble(8, sanPham.getPhanTramLoi());
//        preparedStatement.setDate(9, Date.valueOf(sanPham.getNgaySanXuat()));
//        preparedStatement.setString(10, sanPham.getThue().getMaThue());
//        preparedStatement.setString(11, sanPham.getTinhTrang().toString());
//        return preparedStatement.executeUpdate() > 0;
//        return false;
    }

    @Override
    public boolean capNhat(SanPham target) throws Exception {
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
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Update SanPham set TenSP = ?, MaLoai = ?, PhongCachMac = ?, DoTuoi = ?, XuatXu = ?, MaTH = ?, PhanTramLoi = ?, NgaySanXuat = ?, MaThue = ?, TinhTrang = ? where MaSP = ?");
//        preparedStatement.setString(1, target.getTenSP());
//        preparedStatement.setString(2, target.getLoaiSanPham().getMaLoai());
//        preparedStatement.setString(3, target.getPhongCachMac().toString());
//        preparedStatement.setString(4, target.getDoTuoi().toString());
//        preparedStatement.setString(5, target.getXuatXu());
//        preparedStatement.setString(6, target.getThuongHieu().getMaTH());
//        preparedStatement.setDouble(7, target.getPhanTramLoi());
//        preparedStatement.setDate(8, Date.valueOf(target.getNgaySanXuat()));
//        preparedStatement.setString(9, target.getThue().getMaThue());
//        preparedStatement.setString(10, target.getTinhTrang().toString());
//        preparedStatement.setString(11, target.getMaSP());
//
//        return preparedStatement.executeUpdate() > 0;
//        return false;
    }

    @Override
    public boolean xoa(String id) throws Exception {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            SanPham sanPham = em.find(SanPham.class, id);
            sanPham.setTinhTrang(TinhTrangSanPham.KHONG_CON_BAN);
            em.merge(sanPham);
            et.commit();
            return true;
        }catch (Exception e) {
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Update SanPham set TinhTrang = ? where MaSP = ?");
//        preparedStatement.setString(1, "KHONG_CON_BAN");
//        preparedStatement.setString(2, id);
//
//        return preparedStatement.executeUpdate() > 0;
//        return false;
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<SanPham> timKiem() throws RemoteException {
        try {
            return em.createNamedQuery("SanPham.findAll", SanPham.class).getResultList();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        return null;
    }

    @Override
    public List<SanPham> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from SanPham sp where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("sp." + column + " = N'" + value + "'"));
            isNeedAnd.set(true);
        });

        List<SanPham> sanPhams = new ArrayList<>();
        try {
            sanPhams = em.createNativeQuery(query.get(), SanPham.class).getResultList();
            return sanPhams;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SanPham timKiem(String id) throws Exception {
            SanPham sp = em.createNamedQuery("SanPham.findByID", SanPham.class).setParameter("maSP", id).getSingleResult();
            return Optional.of(sp).get();
    }

    @Override
    public List<SanPham> timKiem(String... ids) throws Exception {

        String query = "select * from SanPham where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("ma_sp = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<SanPham> sanPhams = new ArrayList<>();
        try {
            sanPhams = em.createNativeQuery(query, SanPham.class).getResultList();
            return sanPhams;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            SanPham sanPham = new SanPham(resultSet.getString("MaSP"),
//                    resultSet.getString("TenSP"),
//                    new LoaiSanPhamDAO().timKiem(resultSet.getString("MaLoai")).get(),
//                    PhongCachMac.layGiaTri(resultSet.getString("PhongCachMac")),
//                    DoTuoi.layGiaTri(resultSet.getString("DoTuoi")),
//                    resultSet.getString("XuatXu"),
//                    new ThuongHieuDAO().timKiem(resultSet.getString("MaTH")).get(),
//                    resultSet.getFloat("PhanTramLoi"),
//                    resultSet.getDate("NgaySanXuat").toLocalDate(),
//                    new ThueDAO().timKiem(resultSet.getString("MaThue")).get(),
//                    TinhTrangSanPham.layGiaTri(resultSet.getString("TinhTrang")));
//
//            sanPhams.add(sanPham);
//        }
//        return sanPhams;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);
        AtomicBoolean canAnd = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from SanPham where ");
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
    }
}
