package dev.skyherobrine.app.controllers.loginui.mainLogin;

import dev.skyherobrine.app.daos.person.NhanVienDAO;
import dev.skyherobrine.app.entities.person.NhanVien;
import dev.skyherobrine.app.views.loginui.mainLogin.FormQuenMatKhau;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class QuenMatKhauController implements ActionListener {
    private static FormQuenMatKhau quenMatKhau;
    private NhanVienDAO nhanVienDAO;
    public QuenMatKhauController(FormQuenMatKhau quenMatKhau) {
        try {
            nhanVienDAO = new NhanVienDAO();
            this.quenMatKhau = quenMatKhau;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Object o = e.getSource();
            if(o.equals(quenMatKhau.getBtnGui())) {
                NhanVien nhanVien = nhanVienDAO.timKiem(quenMatKhau.getTxtMaNhanVien().getText()).get();
                if(nhanVien == null) {
                    JOptionPane.showMessageDialog(null, "Mã nhân viên không tồn tại");
                    quenMatKhau.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Gửi mail thành công");
                    quenMatKhau.getBtnGui().setText("Chờ đợi...");
                    quenMatKhau.getBtnGui().setEnabled(false);
                    quenMatKhau.getBtnHuy().setText("Chờ đợi...");
                    quenMatKhau.getBtnHuy().setEnabled(false);
                    guiMail(nhanVien);
                    quenMatKhau.setVisible(false);
                }
            } else {
                quenMatKhau.setVisible(false);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void guiMail(NhanVien nhanVien) throws Exception{
        int newPassword = ThreadLocalRandom.current().nextInt(11111111, 99999999);

        final String from = "skyherobrine13092003@gmail.com";
        final String password = "avws canv abvl bpwi"; //avws canv abvl bpwi

        //Properties:
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");	//SMTP Host
        props.put("mail.smtp.port", "587");	//TLS 587 SSL 465
        props.put("mail.smtp.auth", "true");	//Có yêu cầu đăng nhập khi gửi mail không?
        props.put("mail.smtp.starttls.enable", "true");

        //Create authentication:
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };

        //Phiên làm việc
        Session session = Session.getInstance(props, auth);

        //Gửi email
        final String to = nhanVien.getEmail();

        //Tạo một tin nhắn mới.
        MimeMessage msg = new MimeMessage(session);

        //Kiểu nội dung
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

        //Người gửi
        msg.setFrom(from);

        //Người nhận
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

        //Tiêu đề mail
        msg.setSubject("Khôi phục mật khẩu cho " + nhanVien.getHoTen() + ", với mã: " + nhanVien.getMaNV());

        //Quy định ngày gửi
        msg.setSentDate(Date.valueOf(LocalDateTime.now().toLocalDate()));

        //Nội dung
        msg.setContent("<html><head><meta charset='UTF-8'></head><body><h1>Mật khẩu của bạn đã được khôi phục thành: " + newPassword + "</h1></body></html>", "text/html");

        //Gửi email:
        Transport.send(msg);

        nhanVien.setMatKhau(String.valueOf(newPassword));
        nhanVienDAO.capNhat(nhanVien);
    }
}
