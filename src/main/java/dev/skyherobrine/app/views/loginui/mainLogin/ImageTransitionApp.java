package dev.skyherobrine.app.views.loginui.mainLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

public class ImageTransitionApp {
    private static final String[] imagePaths = {
            "/img/introductionLogin/anh01.jpg", // Đường dẫn của ảnh 1
            "/img/introductionLogin/anh02.jpg", // Đường dẫn của ảnh 2
            "/img/introductionLogin/anh03.jpg"  // Đường dẫn của ảnh 3
    };

    private int currentImageIndex = 0;
    private JLabel imageLabel;
    private Timer timer;
    private long startTime;
    private static final long TRANSITION_DURATION = 2000; // Thời gian chuyển cảnh (ms)

    public ImageTransitionApp() {
        JFrame frame = new JFrame("Image Transition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        imageLabel = new JLabel();
        frame.add(imageLabel);

        // Bắt đầu thời gian chuyển cảnh
        startTime = System.currentTimeMillis();

        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                float progress = (float)(currentTime - startTime) / TRANSITION_DURATION;
                updateImageTransition(progress);
                if (progress >= 1.0f) {
                    ((Timer)e.getSource()).stop();
                    transitionToNextImage();
                }
            }
        });

        transitionToNextImage();
        timer.start();

        frame.setVisible(true);
    }

    private void transitionToNextImage() {
        currentImageIndex = (currentImageIndex + 1) % imagePaths.length;
        URL imageURL = getClass().getResource(imagePaths[currentImageIndex]);
        if (imageURL != null) {
            ImageIcon imageIcon = new ImageIcon(imageURL);
            imageLabel.setIcon(imageIcon);
        } else {
            System.out.println("Lỗi");
        }
        // Bắt đầu thời gian chuyển cảnh
        startTime = System.currentTimeMillis();
        timer.restart();
    }

    private void updateImageTransition(float progress) {
        if (imageLabel.getIcon() != null) {
            // Giới hạn giá trị progress trong khoảng từ 0.0 đến 1.0
            progress = Math.max(0.0f, Math.min(1.0f, progress));

            ImageIcon image1 = (ImageIcon) imageLabel.getIcon();
            Image img1 = image1.getImage();

            URL imageURL = getClass().getResource(imagePaths[currentImageIndex]);
            ImageIcon image2 = new ImageIcon(imageURL);
            Image img2 = image2.getImage();

            int width = img1.getWidth(null);
            int height = img1.getHeight(null);

            BufferedImage blendedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = blendedImage.createGraphics();
            g2d.setComposite(AlphaComposite.SrcOver.derive(1.0f - progress));
            g2d.drawImage(img1, 0, 0, null);
            g2d.setComposite(AlphaComposite.SrcOver.derive(progress));
            g2d.drawImage(img2, 0, 0, null);
            g2d.dispose();

            imageLabel.setIcon(new ImageIcon(blendedImage));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImageTransitionApp();
            }
        });
    }
}
