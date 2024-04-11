package dev.skyherobrine.app.enums;

public enum ChucVu {
    NHAN_VIEN_BAN_HANG(1), QUAN_LY_NHAN_SU(2), ADMIN(3);
    private int value;
    ChucVu(int value) {
        this.value = value;
    }

    public static ChucVu layGiaTri(int value){
        switch (value) {
            case 1 -> { return NHAN_VIEN_BAN_HANG; }
            case 2 -> { return QUAN_LY_NHAN_SU; }
            case 3 -> { return ADMIN; }
            default -> throw new NullPointerException("Không tìm thấy chức vụ nào ứng với giá trị này.");
        }
    }

    public static int layGiaTri(ChucVu chucVu) {
        return chucVu.value;
    }

    public static ChucVu layGiaTri(String value) {
        switch (value.toUpperCase()) {
            case "NHAN_VIEN_BAN_HANG" -> { return NHAN_VIEN_BAN_HANG; }
            case "QUAN_LY_NHAN_SU" -> { return QUAN_LY_NHAN_SU; }
            case "ADMIN" -> { return ADMIN; }
            default -> throw new NullPointerException("Không có giá trị tương ứng để quy đổi");
        }
    }
}
