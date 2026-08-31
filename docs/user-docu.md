## Required Files
- A JSON file is needed for the different database profiles, which must be created beforehand

```json
{
  "mysql": {
    "driverClass": "com.mysql.cj.jdbc.Driver",
    "driverFile": "drivers/mysql-connector-j-9.3.0.jar",
    "url": "jdbc:mysql://localhost:3306/dbgrep-database",
    "username": "root",
    "password": "test"
  },
  "postgres": {
    "driverClass": "org.postgresql.Driver",
    "driverFile": "drivers/postgresql-42.7.5.jar",
    "url": "jdbc:postgresql://localhost:5432/dbgrep-database",
    "username": "root",
    "password": "test"
  }
}

```
- The program requires at least one JDBC driver, which must be specified in the .json file

---

## Usage

```
dbgrep -l <path/to/profile.json> -p <profilename> [options] <search-pattern>
```

---

## Arguments

#### -l, --fileLocation (required)

Path to the .json file containing the database profiles
```
 -l ./profiles.json
```
<br>

#### -p, --profile (required)

Name of the database profile to use from the profile file
```
-p dev_db
```
<br>

#### -t, --table (optional)

Specific table(s) to be searched. Can be used multiple times. If not specified, all tables will be searched
```
-t users -t orders
```

<br>

#### -c, --column (optional)

Column(s) to search in the format `table.column`. Can be used multiple times
```
-c users.name -c orders.amount
```

<br>

#### --range (optional)

Specifies a value range for integer or date columns. Format is [min:max]
```
--range [10:100]
--range [2020-01-01:2023-01-01]
```

<br>

#### &lt;search-pattern&gt; (required)

The pattern to search for in the database. The search is case-insensitive
```
--fileLocation ./profiles.json --profile dev_db searchterm
```













