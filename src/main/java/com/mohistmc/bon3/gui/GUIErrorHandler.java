package com.mohistmc.bon3.gui;

import com.mohistmc.bon3.BON3Gui;
import com.mohistmc.bon3.data.IErrorHandler;

import javax.swing.JOptionPane;
import java.awt.Component;

public class GUIErrorHandler implements IErrorHandler {
    private final Component parent;

    public GUIErrorHandler(Component parent) {
        this.parent = parent;
    }

    @Override
    public boolean handleError(String message, boolean warning) {
        JOptionPane.showMessageDialog(parent, message, BON3Gui.ERROR_DIALOG_TITLE, warning ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE);
        return true;
    }
}
