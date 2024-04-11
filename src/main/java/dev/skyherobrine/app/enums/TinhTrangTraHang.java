package dev.skyherobrine.app.enums;

public enum TinhTrangTraHang {
    CHUA_NHAN_HANG(0), DA_NHAN_HANG(1);
    private int value;
    TinhTrangTraHang(int value) {
        this.value = value;
    }

    public static TinhTrangTraHang layGiaTri(int value) {
        return value == 0 ? CHUA_NHAN_HANG : DA_NHAN_HANG;
    }

    public static int layGiaTri(TinhTrangTraHang tinhTrang) {
        return tinhTrang.value;
    }

    public static TinhTrangTraHang layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "CHUA_NHAN_HANG" -> { return CHUA_NHAN_HANG; }
            case "DA_NHAN_HANG" -> { return DA_NHAN_HANG; }
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi!");
        }
    }
}
