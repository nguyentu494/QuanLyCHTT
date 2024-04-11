/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.skyherobrine.app.views.dashboard.component.nutChon;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Virtue Nguyen
 */
public class TableActionCellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSeleted, boolean bln1, int row, int column) {
        Component com = super.getTableCellRendererComponent(jtable, o, isSeleted, bln1, row, column);
        dev.skyherobrine.app.views.dashboard.component.nutChon.PanelAction action = new dev.skyherobrine.app.views.dashboard.component.nutChon.PanelAction();
        if (isSeleted == false && row % 2 == 0) {
            action.setBackground(Color.WHITE);
        } else {
            action.setBackground(com.getBackground());
        }
        return action;
    }
    
}
