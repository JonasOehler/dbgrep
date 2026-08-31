## Project Information
**Group: 4** <br>

**Semester: Summer Semester 2025**<br>

**Participants:**<br>

| Name            | Enrolment Number |
|---------------------------|-------------|
| Adib Shaqaiq   | 44163         |
| Jonas Öhler   | 44338         |

## Getting Started

### Clone the Repository

```
git clone https://gitlab.mi.hdm-stuttgart.de/jo041/dbgrep.git
```
### Build the .jar

```
cd location/to/repo
mvn clean package
```

### Deploy Database Test Container

```
cd location/to/repo
docker compose -f dbgrep-compose.yml up -d
```