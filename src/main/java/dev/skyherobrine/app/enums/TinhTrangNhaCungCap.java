package dev.skyherobrine.app.enums;

public enum TinhTrangNhaCungCap {
    HOP_TAC(1), CHAM_DUT(0);
    private int value;
    TinhTrangNhaCungCap(int value) {
        this.value = value;
    }

    public static TinhTrangNhaCungCap layGiaTri(int value) {
        return value == 1 ? HOP_TAC : CHAM_DUT;
    }

    public static int layGiaTri(TinhTrangNhaCungCap tinhTrang) {
        return tinhTrang.value;
    }

    public static TinhTrangNhaCungCap layGiaTri(String value) {
        return value.toUpperCase().equalsIgnoreCase(HOP_TAC.toString()) ? HOP_TAC : CHAM_DUT;
    }
}
