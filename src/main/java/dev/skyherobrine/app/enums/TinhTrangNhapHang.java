package dev.skyherobrine.app.enums;

public enum TinhTrangNhapHang {
    CHO_DUYET(0),DANG_CHUYEN(1), DA_NHAN(2);
    private int value;
    TinhTrangNhapHang(int value) {
        this.value = value;
    }

    public static TinhTrangNhapHang layGiaTri(int value) {
        switch (value) {
            case 1 -> { return CHO_DUYET; }
            case 2 -> { return DANG_CHUYEN; }
            case 3 -> { return DA_NHAN; }
            default -> throw new NullPointerException("Không tìm thây kiểu tình trạng làm việc tương ứng với giá trị này.");
        }
    }

    public static int layGiaTri(TinhTrangNhapHang tinhTrang) {
        return tinhTrang.value;
    }

    public static TinhTrangNhapHang layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "CHO_DUYET" -> { return CHO_DUYET; }
            case "DANG_CHUYEN" -> { return DANG_CHUYEN; }
            case "DA_NHAN" -> { return DA_NHAN; }
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi");
        }
    }
}
