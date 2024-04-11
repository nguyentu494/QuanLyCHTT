package dev.skyherobrine.app.enums;

public enum CaLamViec {
    CA_1(0), CA_2(1);
    private int value;
    CaLamViec(int value) {
        this.value = value;
    }

    public static CaLamViec layGiaTri(int value) {
        return value == 0 ? CA_1 : CA_2;
    }

    public static int layGiaTri(CaLamViec ca){
        return ca.value;
    }

    public static CaLamViec layGiaTri(String ca) {
        return ca.toUpperCase().equalsIgnoreCase(CA_1.toString()) ? CA_1 : CA_2;
    }
}
