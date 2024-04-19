package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.PhieuNhapHangDAO;
import dev.skyherobrine.app.entities.order.PhieuNhapHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class PhieuNhapHangImp extends UnicastRemoteObject implements PhieuNhapHangDAO<PhieuNhapHang> {
    private EntityManager em;
    public PhieuNhapHangImp() throws Exception{
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(PhieuNhapHang phieuNhapHang) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert PhieuNhapHang values(?, ?, ?, ?, ?, ?)");
//        preparedStatement.setString(1, phieuNhapHang.getMaPhieuNhap());
//        preparedStatement.setString(2, phieuNhapHang.getNhaCungCap().getMaNCC());
//        preparedStatement.setTimestamp(3, Timestamp.valueOf(phieuNhapHang.getNgayHenGiao()));
//        preparedStatement.setTimestamp(4, Timestamp.valueOf(phieuNhapHang.getNgayLapPhieu()));
//        preparedStatement.setString(5, phieuNhapHang.getGhiChu());
//        preparedStatement.setString(6, phieuNhapHang.getTinhTrang().toString());
//
//        return preparedStatement.executeUpdate() > 0;
        return false;
    }

    @Override
    public boolean capNhat(PhieuNhapHang target) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Update PhieuNhapHang set MaPhieuNhap = ?, MaNCC = ?, NgayHenGiao = ?, NgayLapPhieu = ?, GhiChu = ?, TinhTrang = ? where MaPhieuNhap = ?");
//        preparedStatement.setString(1, target.getMaPhieuNhap());
//        preparedStatement.setString(2, target.getNhaCungCap().getMaNCC());
//        preparedStatement.setTimestamp(3, Timestamp.valueOf(target.getNgayHenGiao()));
//        preparedStatement.setTimestamp(4, Timestamp.valueOf(target.getNgayLapPhieu()));
//        preparedStatement.setString(5, target.getGhiChu());
//        preparedStatement.setString(6, target.getTinhTrang().toString());
//        preparedStatement.setString(7, target.getMaPhieuNhap());
//        return preparedStatement.executeUpdate() > 0;
        return false;
    }

    @Override
    public boolean xoa(String id) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Delete from PhieuNhapHang where MaPhieuNhap = ?");
//        preparedStatement.setString(1, id);
//
//        return preparedStatement.executeUpdate() > 0;
        return false;
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<PhieuNhapHang> timKiem() throws Exception {
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            List<PhieuNhapHang> phieuNhapHangs = em.createNamedQuery("PNH.findAll", PhieuNhapHang.class).getResultList();
            et.commit();
            return phieuNhapHangs;
        }catch (Exception e){
            et.rollback();
            return null;
        }
    }

    @Override
    public List<PhieuNhapHang> timKiem(Map<String, Object> conditions) throws Exception {
//        AtomicReference<String> query = new AtomicReference<>
//                ("select * from PhieuNhapHang t where ");
//        AtomicBoolean isNeedAnd = new AtomicBoolean(false);
//
//        conditions.forEach((column, value) -> {
//            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("t." + column + " like '" + value + "'"));
//            isNeedAnd.set(true);
//        });
        List<PhieuNhapHang> phieuNhapHangs = new ArrayList<>();
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//        ResultSet result = preparedStatement.executeQuery();
//        while(result.next()) {
//            PhieuNhapHang phieuNhapHang = new PhieuNhapHang(result.getString("MaPhieuNhap"),
//                    new NhaCungCapDAO().timKiem(result.getString("MaNCC")).get(),
//                    result.getTimestamp("NgayLapPhieu").toLocalDateTime(),
//                    result.getTimestamp("NgayHenGiao").toLocalDateTime(),
//                    result.getString("GhiChu"),
//                    TinhTrangNhapHang.layGiaTri(result.getString("TinhTrang")));
//
//            phieuNhapHangs.add(phieuNhapHang);
//        }
        return phieuNhapHangs;
    }

    @Override
    public PhieuNhapHang timKiem(String id) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select * from PhieuNhapHang where MaPhieuNhap = ?");
//        preparedStatement.setString(1, id);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        if(resultSet.next()) {
//            return Optional.of(new PhieuNhapHang(resultSet.getString("MaPhieuNhap"),
//                    new NhaCungCapDAO().timKiem(resultSet.getString("MaNCC")).get(),
//                    resultSet.getTimestamp("NgayLapPhieu").toLocalDateTime(),
//                    resultSet.getTimestamp("NgayHenGiao").toLocalDateTime(),
//                    resultSet.getString("GhiChu"),
//                    TinhTrangNhapHang.layGiaTri(resultSet.getString("TinhTrang"))));
//        } else {
            return null;
//        }
    }

    @Override
    public List<PhieuNhapHang> timKiem(String... ids) throws Exception {
        String query = "select * from PhieuNhapHang where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("MaPhieuNhap = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<PhieuNhapHang> phieuNhapHangs = new ArrayList<>();
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            PhieuNhapHang phieuNhapHang = new PhieuNhapHang(resultSet.getString("MaPhieuNhap"),
//                    new NhaCungCapDAO().timKiem(resultSet.getString("MaNCC")).get(),
//                    resultSet.getTimestamp("NgayLapPhieu").toLocalDateTime(),
//                    resultSet.getTimestamp("NgayHenGiao").toLocalDateTime(),
//                    resultSet.getString("GhiChu"),
//                    TinhTrangNhapHang.layGiaTri(resultSet.getString("TinhTrang")));
//
//            phieuNhapHangs.add(phieuNhapHang);
//        }
        return phieuNhapHangs;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
//        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
//        AtomicBoolean canPhay = new AtomicBoolean(false);
//        AtomicBoolean canAnd = new AtomicBoolean(false);
//
//        Arrays.stream(colNames).forEach(column -> {
//            query.set(query.get() + (canPhay.get() ? "," : "") + column);
//            canPhay.set(true);
//        });
//
//        query.set(query.get() + " from PhieuNhapHang where ");
//
//        conditions.forEach((column, value) -> {
//            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
//            canAnd.set(true);
//        });
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery(query.get());

        List<Map<String, Object>> listResult = new ArrayList<>();
//        while(resultSet.next()){
//            Map<String, Object> rowDatas = new HashMap<>();
//            for(String column : Arrays.stream(colNames).toList()) {
//                rowDatas.put(column, resultSet.getString(column));
//            }
//            listResult.add(rowDatas);
//        }
        return listResult;
    }
}
