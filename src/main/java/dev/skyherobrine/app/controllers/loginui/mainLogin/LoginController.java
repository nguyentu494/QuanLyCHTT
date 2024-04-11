package dev.skyherobrine.app.controllers.loginui.mainLogin;


import dev.skyherobrine.app.daos.person.NhanVienDAO;
import dev.skyherobrine.app.entities.person.NhanVien;
import dev.skyherobrine.app.views.dashboard.org.main.Main;
import dev.skyherobrine.app.views.loginui.mainLogin.FormQuenMatKhau;
import dev.skyherobrine.app.views.loginui.mainLogin.LoginUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class LoginController implements ActionListener, KeyListener {
    private static LoginUI loginUI;

    private NhanVienDAO nhanVienDAO;
    public LoginController(LoginUI loginUI) {
        try {
            nhanVienDAO = new NhanVienDAO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.loginUI = loginUI;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        try {
            if (o.equals(loginUI.getLoginButton())) {
                checkLogin();
            } else if(o.equals(loginUI.getBtnQuenMatKhau())) {
                new FormQuenMatKhau().setVisible(true);
            }
        } catch (Exception a) {
            throw new RuntimeException(a);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try {
                checkLogin();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void checkLogin() throws Exception {
        try {
            List<NhanVien> nhanViens = nhanVienDAO.timKiem();
            String tenTK = loginUI.getUsernameField().toString();
            String matKhau = loginUI.getUsernameField().toString();
            int count = 0;
            for (NhanVien nhanVien : nhanViens) {
                if (nhanVien.getTenTaiKhoan().equalsIgnoreCase(tenTK) && nhanVien.getMatKhau().equalsIgnoreCase(matKhau)) {
                    count = 1;
                }
            }
            if(count == 1){
                new Main().setVisible(true);
                loginUI.dispose();
            }else {
                JOptionPane.showMessageDialog(null, "Sai tên tài khoản hoặc mật khẩu");
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    public static String luuTkNhanVien(){
        return loginUI.getUsernameField().toString();
    }
}

