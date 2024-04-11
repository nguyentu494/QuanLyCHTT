package dev.skyherobrine.app.enums;

public enum MauSac {
    TRANG(1), DO(2), DEN(3), VANG(4), CAM(5), XANH_DUONG(6), XANH_LA_CAY(7), HONG(8), XAM(9), NAU(10), TIM(11), XANH(12);
    private int value;
    MauSac(int value) { this.value = value; }
    public static MauSac layGiaTri(int value) {
        switch (value) {
            case 1 -> { return TRANG; }
            case 2 -> { return DO; }
            case 3 -> { return DEN; }
            case 4 -> { return VANG; }
            case 5 -> { return CAM; }
            case 6 -> { return XANH_DUONG; }
            case 7 -> { return XANH_LA_CAY; }
            case 8 -> { return HONG; }
            case 9 -> { return XAM; }
            case 10 -> { return NAU; }
            case 11 -> { return TIM; }
            case 12 -> { return XANH; }
            default -> throw new NullPointerException("Không tìm thây màu sắc ứng với giá trị!");
        }
    }

    public static int layGiaTri(MauSac mau) {
        return mau.value;
    }

    public static MauSac layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "TRANG" -> { return TRANG; }
            case "DO" -> { return DO; }
            case "DEN" -> { return DEN; }
            case "VANG" -> { return VANG; }
            case "CAM" -> { return CAM; }
            case "XANH_DUONG" -> { return XANH_DUONG; }
            case "XANH_LA_CAY" -> { return XANH_LA_CAY; }
            case "HONG" -> { return HONG; }
            case "XAM" -> { return XAM; }
            case "NAU" -> { return NAU; }
            case "TIM" -> { return TIM; }
            case "XANH" -> { return XANH;}
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi");
        }
    }
}
