/*
 * @ (#) ChiTietHoaDonId.java   1.0     10/04/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved
 */

package dev.skyherobrine.app.entities.Key;

import dev.skyherobrine.app.entities.order.HoaDon;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/*
 * @description:
 * @author: Tuss Nguyen
 * @date: 10/04/2024
 * @version: 1.0
 */
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChiTietHoaDonId implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_hd", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phien_ban_sp", nullable = false)
    private ChiTietPhienBanSanPham phienBanSanPham;
}
