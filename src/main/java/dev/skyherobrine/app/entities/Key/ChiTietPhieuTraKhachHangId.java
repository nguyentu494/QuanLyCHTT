/*
 * @ (#) ChiTietPhieuTraKhachHangId.java   1.0     10/04/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved
 */

package dev.skyherobrine.app.entities.Key;

import dev.skyherobrine.app.entities.order.PhieuTraKhachHang;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

/*
 * @description:
 * @author: Tuss Nguyen
 * @date: 10/04/2024
 * @version: 1.0
 */
@Embeddable
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChiTietPhieuTraKhachHangId implements Serializable{
    @ManyToOne
    @JoinColumn(name = "ma_phieu_tra_khach_hang", nullable = false)
    private PhieuTraKhachHang phieuTra;
    @ManyToOne
    @JoinColumn(name = "ma_phien_ban_sp", nullable = false)
    private ChiTietPhienBanSanPham phienBanSanPham;
}
