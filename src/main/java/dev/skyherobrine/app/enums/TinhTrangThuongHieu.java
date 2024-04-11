package dev.skyherobrine.app.enums;

public enum TinhTrangThuongHieu {
    HOP_TAC(1), TAM_NGUNG(0), KHONG_HOP_TAC(-1);
    private int value;
    TinhTrangThuongHieu(int value) { this.value = value; }

    public static TinhTrangThuongHieu layGiaTri(int value) {
        switch (value){
            case 1 -> { return HOP_TAC; }
            case 0 -> { return TAM_NGUNG; }
            case -1 -> {return  KHONG_HOP_TAC; }
            default -> throw new NullPointerException("Không tìm thấy loại tình trạng ứng với giá trị này.");
        }
    }

    public static int layGiaTri(TinhTrangThuongHieu tinhTrang) {
        return tinhTrang.value;
    }

    public static TinhTrangThuongHieu layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "HOP_TAC" -> { return HOP_TAC; }
            case "TAM_NGUNG" -> { return TAM_NGUNG; }
            case "KHONG_HOP_TAC" -> { return KHONG_HOP_TAC; }
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi");
        }
    }
}
