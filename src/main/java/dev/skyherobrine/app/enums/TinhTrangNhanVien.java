package dev.skyherobrine.app.enums;

public enum TinhTrangNhanVien {
    DANG_LAM(1), TAM_NGHI(0), NGHI(-1);
    private int value;
    TinhTrangNhanVien(int value) {
        this.value = value;
    }

    public static TinhTrangNhanVien layGiaTri(int value) {
        switch (value) {
            case 1 -> { return DANG_LAM; }
            case 2 -> { return TAM_NGHI; }
            case 3 -> { return NGHI; }
            default -> throw new NullPointerException("Không tìm thây kiểu tình trạng làm việc tương ứng với giá trị này.");
        }
    }

    public static int layGiaTri(TinhTrangNhanVien tinhTrang) {
        return tinhTrang.value;
    }

    public static TinhTrangNhanVien layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "DANG_LAM" -> { return DANG_LAM; }
            case "TAM_NGHI" -> { return TAM_NGHI; }
            case "NGHI" -> { return NGHI; }
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi");
        }
    }
}
