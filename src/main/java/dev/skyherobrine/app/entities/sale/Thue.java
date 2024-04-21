package dev.skyherobrine.app.entities.sale;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
@NamedQueries({
        @NamedQuery(name = "Thue.findAll", query = "SELECT t FROM Thue t")
})
public class Thue  implements Serializable {

    /**
	 * 
	 */
    @Serial
    private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "ma_thue", nullable = false)
    private String maThue;
    @Column(name = "gia_tri", nullable = false)
    private double giaTri;
    @Column(name = "ngay_ap_dung", nullable = false)
    private LocalDateTime ngayApDung;
    @Column(name = "hieu_luc", nullable = false)
    private boolean hieuLuc;

    public Thue(String maThue, double giaTri, LocalDateTime ngayApDung, boolean hieuLuc) {
        this.maThue = maThue;
        this.giaTri = giaTri;
        this.ngayApDung = ngayApDung;
        this.hieuLuc = hieuLuc;
    }

    public Thue() {

    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public void setGiaTri(double giaTri) {
        this.giaTri = giaTri;
    }

    public void setNgayApDung(LocalDateTime ngayApDung) {
        this.ngayApDung = ngayApDung;
    }

    public void setHieuLuc(boolean hieuLuc) {
        this.hieuLuc = hieuLuc;
    }

    @Override
    public String toString() {
        return "Thue{" +
                "maThue='" + maThue + '\'' +
                ", giaTri=" + giaTri +
                ", ngayApDung=" + ngayApDung +
                ", hieuLuc=" + hieuLuc +
                '}';
    }
}
