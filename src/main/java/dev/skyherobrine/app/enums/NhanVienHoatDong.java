package dev.skyherobrine.app.enums;

public enum NhanVienHoatDong {
    THEM(1), CAP_NHAT(2), XOA(3), TIM_KIEM(4);
    private int value;
    NhanVienHoatDong(int value) { this.value = value; }
    public static NhanVienHoatDong layGiaTri(int value) {
        switch (value) {
            case 1 -> { return THEM; }
            case 2 -> { return CAP_NHAT; }
            case 3 -> { return XOA; }
            case 4 -> { return TIM_KIEM; }
            default -> throw new NullPointerException("Không tìm thấy loại hoạt động nào ứng với giá trịn này.");
        }
    }

    public static int layGiaTri(NhanVienHoatDong hoatDong) {
        return hoatDong.value;
    }

    public static NhanVienHoatDong layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "THEM" -> { return THEM; }
            case "CAP_NHAT" -> { return CAP_NHAT; }
            case "XOA" -> { return XOA; }
            case "TIM_KIEM" -> { return TIM_KIEM; }
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi");
        }
    }
}
