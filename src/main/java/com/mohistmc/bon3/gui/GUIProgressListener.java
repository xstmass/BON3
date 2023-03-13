package com.mohistmc.bon3.gui;

import com.mohistmc.bon3.cli.CLIProgressListener;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class GUIProgressListener extends CLIProgressListener {
    private final JLabel progressLabel;
    private final JProgressBar progressBar;

    public GUIProgressListener(JLabel progressLabel, JProgressBar progressBar) {
        this.progressLabel = progressLabel;
        this.progressBar = progressBar;
    }

    @Override
    public void start(final int max, final String label) {
        super.start(max, label);
        SwingUtilities.invokeLater(() -> {
            progressLabel.setText(getFormatedText(label));
            if (progressBar.isIndeterminate()) {
                progressBar.setIndeterminate(false);
            }
            if (max >= 0) {
                progressBar.setMaximum(max);
            }
            progressBar.setValue(0);
        });
    }

    @Override
    public void startWithoutProgress(final String label) {
        super.startWithoutProgress(label);
        SwingUtilities.invokeLater(() -> {
            progressLabel.setText(getFormatedText(label));
            progressBar.setIndeterminate(true);
        });
    }

    @Override
    public void setProgress(final int value) {
        super.setProgress(value);
        SwingUtilities.invokeLater(() -> progressBar.setValue(value));
    }

    @Override
    public void setMax(final int max) {
        super.setMax(max);
        SwingUtilities.invokeLater(() -> progressBar.setMaximum(max));
    }

    @Override
    public void setLabel(String label) {
        super.setLabel(label);
        SwingUtilities.invokeLater(() -> progressLabel.setText(getFormatedText(label)));
    }

    private String getFormatedText(String value) {
        return "<html>" + value + "</html>"; //This prevents the window from being resized when the text is long.
    }
}
