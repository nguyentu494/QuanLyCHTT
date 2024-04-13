/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package dev.skyherobrine.app.views.dashboard.org.main;


import dev.skyherobrine.app.controllers.dashboardui.mainDashboard.LapHoaDonController;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.views.dashboard.component.*;
import dev.skyherobrine.app.views.dashboard.libDashBoard.scroll.win11.ScrollPaneWin11;
import dev.skyherobrine.app.views.dashboard.org.menu.Menu;
import dev.skyherobrine.app.views.dashboard.org.menu.MenuEvent;


import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * @author Virtue Nguyen
 */
public class Main extends javax.swing.JFrame {
//    protected static int process = 0;

    /**
     * Creates new form Main
     */
    public Main() {
//        frameLoad();
        initComponents();
//        LoadMain loadMain = new LoadMain();
//        loadMain.execute();
        TrangChu t = new TrangChu();
//        process = 10;
        QuanLySanPham sp = new QuanLySanPham();
//        process = 20;
        QuanLyNhapHang nh = new QuanLyNhapHang();
//        process = 100;
        LapHoaDon lapHoaDon = new LapHoaDon();
        QuanLyHoaDon quanLyHoaDon = new QuanLyHoaDon();
        QuanLyPhieuTraHangChoKhachHang quanLyPhieuTraHangChoKhachHang = new QuanLyPhieuTraHangChoKhachHang();
        LapHoaDonController lapHoaDonController = new LapHoaDonController(lapHoaDon);
        FrmNhaCungCap ncc = new FrmNhaCungCap();
        FrmKhachHang kh = new FrmKhachHang();
        FrmNhanVien nv = new FrmNhanVien();
        pnBody.add(t);
        menuDashBoard.setEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex) {

                try {

                    long startTime = System.currentTimeMillis();



                    if (index == 0) {//trang chủ
                        showForm(t);
//                        lapHoaDonController.checkCamera();
//                        System.out.println("Form : " + index + " " + subIndex);
                    } else if(index ==1){// thông tin cá nhân
                        showForm(new FormTHongTinCaNhan());
//                        lapHoaDonController.checkCamera();
//                        System.out.println("Form : " + index + " " + subIndex);
                    } else if (index==2 && subIndex==1) {//Lập hoá đơn
                        showForm(lapHoaDon);
//                        lapHoaDonController.setCamera();
//                        System.out.println("Form : " + index + " " + subIndex);
                    } else if (index==2 && subIndex==2) {//Quản lý hoá đơn
                        showForm(quanLyHoaDon);
//                        lapHoaDonController.checkCamera();
//                        System.out.println("Form : " + index + " " + subIndex);
                    } else if(index == 3) {//Thuế
                        showForm(new FormThue());
                        System.out.println("Form: " + index + " " + subIndex);
                    } else if (index==4 && subIndex==1) {// quản lý sản phẩm
                        showForm(sp);
//                        lapHoaDonController.checkCamera();
//                        lapHoaDonController.offCamera();
                        System.out.println("Form : " + index + " " + subIndex);
                    }else if (index==4 && subIndex==2) {//Quản lý Nhập hàng
                        showForm(nh);
//                        lapHoaDonController.checkCamera();
                        System.out.println("Form : " + index + " " + subIndex);
                    }
                    else  if (index==4 && subIndex==3) {//quản lý trả hàng của khách hàng
                        showForm(quanLyPhieuTraHangChoKhachHang);
                        System.out.println("Form : " + index + " " + subIndex);
                    }
                    else if (index==4 && subIndex==5) {// xoá
                        showForm(new DefaultForm("Form : " + index + " " + subIndex));
                        System.out.println("Form : " + index + " " + subIndex);
                    }
                    else if (index==4 && subIndex==6) {// xoá
                        showForm(new DefaultForm("Form : " + index + " " + subIndex));
                        System.out.println("Form : " + index + " " + subIndex);
                    } else if (index==5 ) {//nhà cung cấp
                        showForm(ncc);
                        System.out.println("Form : " + index + " " + subIndex);
                    }else if (index==6 ) {//khách hàng
                        showForm(kh);
                        System.out.println("Form : " + index + " " + subIndex);
                    }else if (index==7) {//nhân viên
                        showForm(nv);
                        System.out.println("Form : " + index + " " + subIndex);
                    }else if (index==8 && subIndex ==1 ) {// báo cáo cửa hàng về doanh thu
                        showForm(new FormBaoCaoDoanhThuCuaHang());
                        System.out.println("Form : " + index + " " + subIndex);
                    }else if (index==8 && subIndex ==2 ) {// báo cáo cửa hàng v sản phẩm
                        showForm(new FormBaoCaoSanPhamCuaHang());
                        System.out.println("Form : " + index + " " + subIndex);
                    }else if (index==9 ) {// trợ giúp
                        showForm(new DefaultForm("Form : " + index + " " + subIndex));
                        System.out.println("Form : " + index + " " + subIndex);
                    }else if (index==10 ) {// đăng xuất
                        showForm(new DefaultForm("Form : " + index + " " + subIndex));
                        System.exit(0);
                        System.out.println("Form : " + index + " " + subIndex);
                    }

                    // tính toán thời gian load
                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;
                    System.out.println("Thời gian load cho Form " + index + " " + subIndex + ": " + executionTime + "ms");


                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void showForm(JComponent component) {
        pnBody.removeAll();
        pnBody.add(component);
        pnBody.repaint();
        pnBody.revalidate();
    }



    /**
     * This method is called from within t6he constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnBackGroungDashBoard = new javax.swing.JPanel();
        pnLogoNMenu = new javax.swing.JPanel();
        header1 = new Header();
        scrollPaneWin112 = new ScrollPaneWin11();
        menuDashBoard = new Menu();
        pnBody = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("dashboard"); // NOI18N
        setUndecorated(true);

        pnBackGroungDashBoard.setBackground(new java.awt.Color(255, 255, 255));
        pnBackGroungDashBoard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnBackGroungDashBoard.setAlignmentX(0.0F);
        pnBackGroungDashBoard.setAlignmentY(0.0F);
        pnBackGroungDashBoard.setPreferredSize(new java.awt.Dimension(1920, 1000));

        pnLogoNMenu.setPreferredSize(new java.awt.Dimension(1920, 1022));

        scrollPaneWin112.setBorder(null);
        scrollPaneWin112.setViewportView(menuDashBoard);

        javax.swing.GroupLayout pnLogoNMenuLayout = new javax.swing.GroupLayout(pnLogoNMenu);
        pnLogoNMenu.setLayout(pnLogoNMenuLayout);
        pnLogoNMenuLayout.setHorizontalGroup(
                pnLogoNMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(header1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(scrollPaneWin112, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnLogoNMenuLayout.setVerticalGroup(
                pnLogoNMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnLogoNMenuLayout.createSequentialGroup()
                                .addComponent(header1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(scrollPaneWin112, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );

        pnBody.setPreferredSize(new java.awt.Dimension(1651, 1000));
        pnBody.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout pnBackGroungDashBoardLayout = new javax.swing.GroupLayout(pnBackGroungDashBoard);
        pnBackGroungDashBoard.setLayout(pnBackGroungDashBoardLayout);
        pnBackGroungDashBoardLayout.setHorizontalGroup(
                pnBackGroungDashBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnBackGroungDashBoardLayout.createSequentialGroup()
                                .addComponent(pnLogoNMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(pnBody, javax.swing.GroupLayout.PREFERRED_SIZE, 1686, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnBackGroungDashBoardLayout.setVerticalGroup(
                pnBackGroungDashBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnLogoNMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE)
                        .addGroup(pnBackGroungDashBoardLayout.createSequentialGroup()
                                .addComponent(pnBody, javax.swing.GroupLayout.PREFERRED_SIZE, 1004, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnBackGroungDashBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnBackGroungDashBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variablesa
//    private JFrame frmLoad;
//    private JPanel pnLoad;
//    private JLabel label;
    private Header header1;
    private Menu menuDashBoard;
    private javax.swing.JPanel pnBackGroungDashBoard;
    private javax.swing.JPanel pnBody;
    private javax.swing.JPanel pnLogoNMenu;
    private ScrollPaneWin11 scrollPaneWin112;
    // End of variables declaration//GEN-END:variables
//    class LoadMain extends SwingWorker<Void, Integer> {
//
//        @Override
//        protected Void doInBackground() throws Exception {
//
//
//            // Giả lập quá trình tải (bạn có thể thay thế bằng logic tải thực tế)
//            int check = 0;
//            while(process < 100) {
//                if(process!=check){
//                    check = process;
//                    label.setText("Loading... " + check + "%");
//                }
//                publish(process);
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void done() {
//            frame.setVisible(false);
//        }
//    }

}
