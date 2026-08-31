## Architecture & Components

### 1. DatabaseConfig
- **Description:** A bean class containing the configuration parameters for a database connection
- **Key Fields:**
    - `driverClass` – JDBC driver class (e.g., `"org.postgresql.Driver"`)
    - `driverFile` – Path to the JDBC driver JAR file
    - `url` – JDBC URL of the database
    - `username` – Username for DB connection
    - `password` – Password for DB connection
- **Methods:** Getters/Setters for the fields

---

### 2. DbGrepCommand
- **Description:** Main command class that processes CLI parameters, triggers the search logic, and handles error messages
- **CLI Options:**
    - `-l, --fileLocation` – Path to the JSON file with DB profiles (required)
    - `-p, --profile` – Profile name to be used (required)
    - `-t, --table` – List of tables to search (optional, defaults to all)
    - `-c, --column` – List of columns to search in format `table.column` (optional)
    - `--range` – Value range `[min:max]` for numeric or date columns (optional)
    - Search Pattern (parameter) – The pattern to search for (case-insensitive)
- **Functionality:**
    - Loads profiles from the JSON file
    - Retrieves the DbConfig of the specified profile
    - Dynamically registers JDBC driver
    - Opens database connection
    - Retrieves all or specified tables
    - Searches the pattern in specified or all columns for each table

---

### 3. DriverShim
- **Description:** A utility class for JDBC driver objects to avoid class loader issues during dynamic driver loading

---

### 4. Main
- **Description:** Application entry point
- **Function:**
    - Initializes and runs the CLI using `DbGrepCommand`
    - Sets the `ExceptionHandler` and `ExceptionMapper`

---

### 5. ProfileHandler
- **Description:** Manages connection and driver handling
- **Function:**
    - Dynamically loads the JDBC driver JAR based on the profile
    - Registers the driver with the `DriverManager`
    - Opens DB connections using the profile data

---

### 6. ProfileLoader
- **Description:** Loads DB profiles from a JSON file
- **Implementation:** Uses Jackson `ObjectMapper`, returns a map of profile names to `DatabaseConfig` objects

---

### 7. TableDAO
- **Description:** Data access layer for reading tables and columns and executing search queries
- **Key Methods:**
    - `getAllTableNames(Connection)` – Returns all table names
    - `getSearchableColumns(Connection, table, columns)` – Returns columns to be searched (filtered by CLI parameters)
    - `searchInTable(Connection, table, searchableCols, pattern, range)` – Searches rows with the pattern and optional range constraints
- **Search:**
    - Searches pattern column-wise (case-insensitive)
    - Supports range filtering for integer and date columns
    - Outputs result in a tabular format in the console with highlighted matches

---

### 8. TableUtils
- **Description:** Utility class for tabular output in the console
- **Function:**
    - Prints column headers in bold
    - Prints rows with match highlighting (in red)
    - Uses ANSI escape codes for pattern highlighting

---

### 9. ExceptionHandler
- **Description:** Global error handler for command execution
- **Function:**
    - Catches unhandled exceptions during the execution of `DbGrepCommand`
    - Logs the exception and stack trace using Log4j
    - Returns a specific exit code based on the type of exception (see `ExitCodeMapper`)

---

### 10. ExitCodeMapper
- **Description:** Maps known exceptions to specific exit codes
- **Mapping:**

| Exception Type             | Return Code |
|---------------------------|-------------|
| `NullPointerException`    | 101         |
| `FileNotFoundException`   | 102         |
| `IllegalArgumentException`| 103         |
| Other/Unknown Errors       | 1           |

- **Usage:** Used by `ExceptionHandler` to return a specific exit code based on the exception type

---

## Profile Data Format (JSON)

```
{
  "profileName1": {
    "driverClass": "org.postgresql.Driver",
    "driverFile": "/path/to/postgresql.jar",
    "url": "jdbc:postgresql://localhost:5432/mydb",
    "username": "user",
    "password": "secret"
  },
  "profileName2": {
    ...
  }
}
```