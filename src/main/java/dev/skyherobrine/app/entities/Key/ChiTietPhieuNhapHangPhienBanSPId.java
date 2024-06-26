/*
 * @ (#) ChiTietPhieuNhapHangPhienBanSPId.java   1.0     10/04/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved
 */

package dev.skyherobrine.app.entities.Key;

import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/*
 * @description:
 * @author: Tuss Nguyen
 * @date: 10/04/2024
 * @version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@ToString
public class ChiTietPhieuNhapHangPhienBanSPId implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ma_chi_tiet_phieu_nhap", nullable = false)
    private ChiTietPhieuNhapHang chiTietPhieuNhapHang;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_phien_ban_sp", nullable = false)
    private ChiTietPhienBanSanPham chiTietPhienBanSanPham;
}
