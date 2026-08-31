package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

public class ExceptionHandler implements CommandLine.IExecutionExceptionHandler {
    private static final Logger logger = LogManager.getLogger(ExceptionHandler.class);
    @Override
    public int handleExecutionException(Exception ex, CommandLine cmd, CommandLine.ParseResult parseResult) {
        logger.error(ex.getMessage(), ex);
        return new ExitCodeMapper().getExitCode(ex);
    }
}
