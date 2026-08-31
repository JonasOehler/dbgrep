# DbGrep

DbGrep is a CLI program designed to provide the functionality of the Linux grep command for accessing database management systems

## Project Goals

- Allows switching between different database management systems
- Provides the ability to specify the search level, e.g., entire database, tables, table columns
- The output should be formatted, including color highlighting of matches as well as tabular display with corresponding column names
- The ability to set filters, e.g., range

## Getting Started

### Clone the Repository

```
git clone https://gitlab.mi.hdm-stuttgart.de/jo041/dbgrep.git
```
### Package into .jar

```
cd location-to-repo
mvn clean package
```

### Required Files
- A JSON file for the different database profiles is needed
- The program expects at least one JDBC driver

## Authors

Adib Shaqaiq, Jonas Öhler

## License

Copyright © 2025 MI <br>

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
