package com.mohistmc.bon3.gui;

import com.mohistmc.bon3.BON3Gui;
import com.mohistmc.bon3.BON3Impl;
import com.mohistmc.bon3.util.MCPVersions;
import com.mohistmc.bon3.util.MCPVersions.MCPVersion;
import com.mohistmc.bon3.util.MappingVersions;
import net.minecraftforge.srgutils.MinecraftVersion;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class StartListener extends MouseAdapter {
    private final BON3Gui parent;
    private Thread run = null;
    private final JTextField input;
    private final JTextField output;
    private final JComboBox<MinecraftVersion> mcVer;
    private final JComboBox<MappingVersions.MappingVersion> mappingVer;
    private final JLabel progressLabel;
    private final JProgressBar progressBar;

    public StartListener(BON3Gui parent, JTextField input, JTextField output, JComboBox<MinecraftVersion> mcVer, JComboBox<MappingVersions.MappingVersion> mappingVer, JLabel progressLabel, JProgressBar progressBar) {
        this.parent = parent;
        this.input = input;
        this.output = output;
        this.mcVer = mcVer;
        this.mappingVer = mappingVer;
        this.progressLabel = progressLabel;
        this.progressBar = progressBar;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!input.getText().endsWith(".jar") || !output.getText().endsWith(".jar")) {
            JOptionPane.showMessageDialog(parent, "Nice try, but only JAR mods work.", BON3Gui.ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
        }
        if (run != null && run.isAlive()) {
            return;
        }
        run = new Thread("BON3 Remapping Thread") {
            @Override
            public void run() {
                try {
                    MCPVersion mcp = MCPVersions.get((MinecraftVersion) mcVer.getSelectedItem());
                    MappingVersions.MappingVersion map = (MappingVersions.MappingVersion) mappingVer.getSelectedItem();

                    BON3Impl.remap(new File(input.getText()), new File(output.getText()), mcp, map, new GUIErrorHandler(parent), new GUIProgressListener(progressLabel, progressBar));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "There was an error.\n" + ex + "\n" + getFormattedStackTrace(ex.getStackTrace()), BON3Gui.ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        run.start();
    }

    private String getFormattedStackTrace(StackTraceElement[] stacktrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stacktrace) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
