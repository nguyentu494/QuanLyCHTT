package dev.skyherobrine.app.controllers.dashboardui.product;

import dev.skyherobrine.app.daos.sale.ThueDAO;
import dev.skyherobrine.app.entities.sale.Thue;
import dev.skyherobrine.app.views.dashboard.component.FormThue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ThueController implements MouseListener, ActionListener {
    private FormThue formThue;
    private ThueDAO thueDAO;
    private int tinhtrangnutthem = 0;
    private int tinhtrangnutxoa = 0;
    public ThueController(FormThue formThue) {
        this.formThue = formThue;
        try {
            this.thueDAO = new ThueDAO();
            formThue.getTxtMaThue().setEnabled(false);

            tuongTac(false);
            loadThue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadThue(){
        try {
            List<Thue> listThue = thueDAO.timKiem();
            DefaultTableModel model = (DefaultTableModel) formThue.getTbDanhSachThueCuaSanPham().getModel();
            int i = 0;
            for (Thue thue : listThue) {
                String row[] = {(++i)+"",thue.getMaThue(), thue.getNgayApDung()+"", thue.getGiaTri()+""};
                model.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = formThue.getTbDanhSachThueCuaSanPham().getSelectedRow();
        String maThue = formThue.getTbDanhSachThueCuaSanPham().getValueAt(row, 1).toString();
        try {
            Optional<Thue> thue = thueDAO.timKiem(maThue);
            formThue.getTxtMaThue().setText(thue.get().getMaThue());
            LocalDate ngayApDung1 = LocalDate.from(thue.get().getNgayApDung());
            Date ngayApDung = java.sql.Date.valueOf(ngayApDung1);
            formThue.getjDateChooserNgayApDungThongTinThue().setDate(ngayApDung);
            formThue.getTxtThue().setText(thue.get().getGiaTri()+"");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    public void tuongTac(boolean o){
        formThue.getjDateChooserNgayApDungThongTinThue().setEnabled(o);
        formThue.getTxtThue().setEnabled(o);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(formThue.getBtnThemThue())) {
            if(tinhtrangnutthem == 0) {
                tuongTac(true);
                formThue.getTxtMaThue().setText("");
                formThue.getjDateChooserNgayApDungThongTinThue().setDate(null);
                formThue.getTxtThue().setText("");
                phatSinhMaThue();
                tinhtrangnutxoa = 1;
                tinhtrangnutthem = 1;
                formThue.getBtnXoaTrangThue().setText("Hủy");
                formThue.getBtnThemThue().setText("Xác nhận");
            }else {
                int result = JOptionPane.showConfirmDialog(formThue, "Bạn có muốn thêm thuế này không?", "Thêm thuế", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION){
                    try {
                        String maThue = formThue.getTxtMaThue().getText();
                        String tiLe = formThue.getTxtThue().getText();
                        LocalDateTime ngayApDung= LocalDateTime.parse(formThue.getjDateChooserNgayApDungThongTinThue()+" 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        System.out.println( ngayApDung);
                        Thue thue = new Thue(maThue,  Double.parseDouble(tiLe), ngayApDung, true);
                        thueDAO.update("false");
                        thueDAO.them(thue);

                        JOptionPane.showMessageDialog(formThue, "Thêm thuế thành công");
                        DefaultTableModel model = (DefaultTableModel) formThue.getTbDanhSachThueCuaSanPham().getModel();
                        model.setRowCount(0);
                        tinhtrangnutthem = 0;
                        formThue.getBtnThemThue().setText("Thêm thuế");
                        loadThue();
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                }
            }
        }else{
            if(tinhtrangnutxoa == 0) {
                xoaTrang();
            }else {
                tinhtrangnutxoa = 0;
                tinhtrangnutthem = 0;
                formThue.getBtnXoaTrangThue().setText("Xóa trắng");
                formThue.getBtnThemThue().setText("Thêm thuế");
                xoaTrang();
                tuongTac(false);
            }
        }
    }
    public void xoaTrang(){
        formThue.getTxtMaThue().setText("");
        formThue.getjDateChooserNgayApDungThongTinThue().setDate(null);
        formThue.getTxtThue().setText("");
    }
    public void phatSinhMaThue(){
        try {
            List<Thue> list = thueDAO.timKiem();
            int max = 0;
            for (Thue thue : list) {
                int ma = Integer.parseInt(thue.getMaThue().substring(4));
                if(ma > max){
                    max = ma;
                }
            }
            formThue.getTxtMaThue().setText("THUE"+formatNumber(max+1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String formatNumber(int number) {
        if(number < 10)
            return String.format("00%d", number);
        else if((number >= 10) && (number < 100))
            return String.format("0%d", number);
        else
            return String.format("%d", number);
    }
}
