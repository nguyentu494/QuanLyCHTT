/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.skyherobrine.app.views.dashboard.component.nutChon;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 *
 * @author Virtue Nguyen
 */
public class TableActionCellEditor extends DefaultCellEditor {
    
    private TableActionEvent event;

    public TableActionCellEditor(TableActionEvent event) {
        super(new JCheckBox());
        this.event = event;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column) {
        dev.skyherobrine.app.views.dashboard.component.nutChon.PanelAction action = new dev.skyherobrine.app.views.dashboard.component.nutChon.PanelAction();
        action.initEvent(event, row);
        action.setBackground(jtable.getSelectionBackground());
        return action;
    }
    
    
    
}
