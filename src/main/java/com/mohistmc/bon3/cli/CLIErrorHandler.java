package com.mohistmc.bon3.cli;

import com.mohistmc.bon3.data.IErrorHandler;

public class CLIErrorHandler implements IErrorHandler {

    @Override
    public boolean handleError(String message, boolean warning) {
        System.err.println(message);
        return true;
    }
}
