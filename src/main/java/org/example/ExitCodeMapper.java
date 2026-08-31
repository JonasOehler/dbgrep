package org.example;

import picocli.CommandLine;

import java.io.FileNotFoundException;

public class ExitCodeMapper implements CommandLine.IExitCodeExceptionMapper {
    @Override
    public int getExitCode(Throwable exception) {
        if (exception instanceof NullPointerException) return  101;
        if (exception instanceof FileNotFoundException) return 102;
        if (exception instanceof IllegalArgumentException) return 103;
        return 1; // default error code
    }

}
