package dev.skyherobrine.app.enums;

public enum TinhTrangSanPham {
    CON_BAN(1), HET_HANG(0), KHONG_CON_BAN(-1);
    private int value;
    TinhTrangSanPham(int value) { this.value = value; }
    public static TinhTrangSanPham layGiaTri(int value) {
        return value == 1 ? CON_BAN : (value == 0 ? HET_HANG : KHONG_CON_BAN);
    }

    public static int layGiaTri(TinhTrangSanPham tinhTrang) {
        return tinhTrang.value;
    }

    public static TinhTrangSanPham layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "CON_BAN" -> { return CON_BAN; }
            case "HET_HANG" -> { return HET_HANG; }
            case "KHONG_CON_BAN" -> { return KHONG_CON_BAN; }
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi");
        }
    }
}
