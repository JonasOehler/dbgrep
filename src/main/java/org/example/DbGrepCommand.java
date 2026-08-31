package org.example;

import picocli.CommandLine.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Command(name = "dbgrep", mixinStandardHelpOptions = true, version = "dbgrep 1.0",
        description = "Searches for patterns in database tables, grep style.")
public class DbGrepCommand implements Callable<Integer> {
    @Option(names = {"-l", "--fileLocation"}, required = true, description = "Specify location of .json File for profiles")
    private String profileFileLocation;

    @Option(names = {"-p", "--profile"}, required = true, description = "Database profile name to use")
    private String profileName;

    @Option(names = {"-t", "--table"}, description = "Tables to search (optional, default: all tables)")
    private List<String> tables = new ArrayList<>();

    @Option(names = {"-c", "--column"}, description = "Columns to search, format table.column (optional)")
    private List<String> columns = new ArrayList<>();

    @Option(names = {"--range"}, description = "Inclusive value range in format [min:max] for integer/date columns only (e.g., [1:100] or [2020-01-01:2022-01-01])")
    private String range;

    @Parameters(description = "Search pattern (case insensitive)")
    private String searchPattern;

    @Override
    public Integer call() throws Exception {
        Map<String, DatabaseConfig> profiles = ProfileLoader.loadProfiles(profileFileLocation);
        if(profiles.isEmpty()) System.err.println("No profiles found in" + profileFileLocation);

        DatabaseConfig config = profiles.get(profileName);
        if (config == null) System.err.println("The profile " + profileName + " does not exist");

        ProfileHandler profileHandler = new ProfileHandler(config);
        profileHandler.registerDriver();

        try (Connection conn = profileHandler.getConnection()) {
            TableDAO dao = new TableDAO();
            if (tables.isEmpty()) {
                tables = dao.getAllTableNames(conn);
            }

            for (String table : tables) {
                List<String> searchableCols = dao.getSearchableColumns(conn, table, columns);
                if (!searchableCols.isEmpty()) {
                    dao.searchInTable(conn, table, searchableCols, searchPattern, range);
                }
            }
        }
        return 0;
    }
}
