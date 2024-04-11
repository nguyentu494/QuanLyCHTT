package dev.skyherobrine.app.views.dashboard.org.menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class Menu extends JComponent {

    public MenuEvent getEvent() {
        return event;
    }

    public void setEvent(MenuEvent event) {
        this.event = event;
    }

    private MenuEvent event;
    private MigLayout layout;
    private String[][] menuItems = new String[][]{
            {"Trang chủ"},
            {"Thông tin cá nhân"},
            {"Hoá đơn","Lập hoá đơn","Quản lý hoá đơn"},
            {"Thuế"},
            {"Sản phẩm","Quản lý sản phẩm","Quản lý nhập hàng","Quản lý trả hàng của khách hàng"},
            {"Nhà cung cấp"},
            {"Khách hàng"},
            {"Nhân viên"},
            {"Báo cáo cửa hàng","Doanh thu","Sản phẩm"},
            {"Trợ giúp"},
            {"Đăng xuất"}
    };

    public Menu() {
        init();
    }

    private Icon getIcon(String namePic) {
        URL url = getClass().getResource("/img/iconChoTabMenuDashBoard/" + namePic + ".png");
        if (url != null) {
            ImageIcon iconGoc = new ImageIcon(url);
            Image anh = iconGoc.getImage();
            Image tinhChinhAnh = anh.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            return new ImageIcon(tinhChinhAnh);
        } else {
            return null;
        }
    }


    private void init() {
        layout = new MigLayout("wrap 1, fillx, gapy 0, inset 2", "fill");
        setLayout(layout);
        setOpaque(true);
        //  Init MenuItem
        for (int i = 0; i < menuItems.length; i++) {
            addMenu(menuItems[i][0], i);
        }

    }


    private void addMenu(String menuName, int index) {
        int length = menuItems[index].length;
        MenuItem item = new MenuItem(menuName, index, length > 1);
        Font font = new Font("Time New Roman", Font.BOLD, 20);
        Icon icon = getIcon(menuName);
        item.setIcon(icon);
        item.setFont(font);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (length > 1) {
                    if (!item.isSelected()) {
                        item.setSelected(true);
                        addSubMenu(item, index, length, getComponentZOrder(item));
                    } else {
                        //  Hide menu
                        hideMenu(item, index);
                        item.setSelected(false);
                    }
                } else {
                    if (event != null) {
                        event.selected(index, 0);
                    }
                }
            }
        });
        add(item);
        revalidate();
        repaint();
    }

    private void addSubMenu(MenuItem item, int index, int length, int indexZorder) {
        JPanel panel = new JPanel(new MigLayout("wrap 1, fillx, inset 0, gapy 0", "fill"));
        panel.setName(index + " ");
        panel.setBackground(new Color(18, 99, 63));
        Font font = new Font("Time New Roman", Font.BOLD, 13);
        for (int i = 1; i < length; i++) {
            MenuItem subItem = new MenuItem(menuItems[index][i], i, false);
            subItem.setFont(font);
            subItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (event != null) {
                        event.selected(index, subItem.getIndex());
                    }
                }
            });
            subItem.initSubMenu(i, length);
            panel.add(subItem);
        }
        add(panel, "h 0!", indexZorder + 1);
        revalidate();
        repaint();
        MenuAnimation.showMenu(panel, item, layout, true);
    }

    private void hideMenu(MenuItem item, int index) {
        for (Component com : getComponents()) {
            if (com instanceof JPanel && com.getName() != null && com.getName().equals(index + " ")) {
                com.setName(null);
                MenuAnimation.showMenu(com, item, layout, false);
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setColor(new Color(21, 110, 71));
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        super.paintComponent(grphcs);
    }

}
