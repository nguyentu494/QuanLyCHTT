package dev.skyherobrine.app.enums;

public enum DoTuoi {
    TRE_EM(1), THIEU_NIEN(2), THANH_NIEN(3), TRUNG_NIEN(4);
    private int value;
    DoTuoi(int value) {
        this.value = value;
    }

    public static DoTuoi layGiaTri(int value) {
        switch (value){
            case 1 -> {
                return TRE_EM;
            }
            case 2 -> {
                return THIEU_NIEN;
            }
            case 3 -> {
                return THANH_NIEN;
            }
            case 4 -> {
                return TRUNG_NIEN;
            }
            default -> throw new NullPointerException("Giá trị này không ứng với độ tuổi nào cả!");
        }
    }

    public static int layGiaTri(DoTuoi tuoi) {
        return tuoi.value;
    }

    public static DoTuoi layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "TRE_EM" -> { return TRE_EM; }
            case "THIEU_NIEN" -> { return THIEU_NIEN; }
            case "THANH_NIEN" -> { return THANH_NIEN; }
            case "TRUNG_NIEN" -> { return TRUNG_NIEN; }
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi");
        }
    }
}
