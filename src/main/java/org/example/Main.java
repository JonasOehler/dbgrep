package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new DbGrepCommand());

        cmd.setExecutionExceptionHandler(new ExceptionHandler());
        cmd.setExitCodeExceptionMapper(new ExitCodeMapper());

        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }
}