# Run TP6 with MySQL

The DAO sends product CRUD queries to a MySQL server through JDBC.
`DatabaseConnection` supplies the same connection settings to the DAO and `InitDb`.
`InitDb` creates the table if needed and prints existing rows; it inserts no sample data.
The existing Maven dependency is already MySQL Connector/J 9.1.0.

## 1. Prepare the tools

Use JDK 17+, Maven, and a running MySQL server (8.0.16+ for enforced CHECK
constraints; MySQL 8.4 is suitable). MySQL Workbench is an optional client,
not the database server itself.

On Ubuntu, if these tools are missing:

```bash
sudo apt update
sudo apt install mysql-server mysql-client maven
sudo service mysql start
```

If your server is on Windows or another machine, start it there and use its
reachable host address in step 3. Run the remaining shell commands in Ubuntu/bash.

## 2. Create a database and account

Open an administrator SQL session (`sudo mysql` on a typical Ubuntu installation,
or an administrator connection in Workbench). Run the following, replacing the
example password before executing it:

```sql
CREATE DATABASE IF NOT EXISTS tp6 CHARACTER SET utf8mb4;
CREATE USER 'tp6_user'@'localhost' IDENTIFIED BY 'REPLACE_WITH_YOUR_PASSWORD';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE ON tp6.* TO 'tp6_user'@'localhost';
```

If this account already exists, use its current password and omit CREATE USER.
The localhost account is for a locally running Java application. A remote server
needs an account permitted for the application's actual client host.

## 3. Set the connection settings

In the same terminal you will use to run Java:

```bash
cd Java/TP6
export TP6_DB_URL='jdbc:mysql://localhost:3306/tp6'
export TP6_DB_USER='tp6_user'
read -r -s -p 'MySQL password: ' TP6_DB_PASSWORD
echo
export TP6_DB_PASSWORD
```

The password must match step 2. For IDE launches, set these three environment
variables in the Java run configuration as well; terminal exports do not update
an already running IDE. Change localhost/3306/tp6 if your server uses different
settings. Do not add credentials to Java source files.

## 4. Build and download the JDBC driver

```bash
mvn compile dependency:copy-dependencies
```

If your terminal is at the repository root, use this equivalent command instead:

```bash
mvn -f Java/TP6/pom.xml compile dependency:copy-dependencies
```

In the IDE, reload the Maven project after changing dependencies. The initializer
is outside Maven's source root, so the next command runs it in Java source-file
mode with the compiled application classes on its classpath.

## 5. Initialize the table

First verify connectivity without changing data (run from TP6):

```bash
java -cp 'target/classes:target/dependency/*' tp6.exercise2.CheckConnection
```

From the TP6 directory:

```bash
java -cp 'target/classes:target/dependency/*' exercise2/database/InitDb.java
```

You should see `MySQL database initialized.` and any stored products. No sample
products are inserted. Existing database rows are left untouched. The schema
file contains one statement.

## 6. Check the data and use the DAO

In Workbench or the mysql client:

```sql
USE tp6;
SELECT code, nom, prix_achat, prix_vente FROM PRODUIT ORDER BY code;
SHOW CREATE TABLE PRODUIT;
```

`ProductDAO.add`, `update`, `delete`, and `findAll` now connect to MySQL using
the same environment variables. `createTable` also uses the MySQL schema.
The Product model retains its existing double API; storage rounds prices to two
decimal places. The GUI now runs through tp6.exercise2.MainFrame. See
[EXERCISE2_GUIDE.md](EXERCISE2_GUIDE.md) for launch commands and button behavior.

## Existing SQLite records

`produits.db` is preserved, but MySQL cannot open that file. The initializer
creates the table without inserting records; it does not migrate SQLite data. If you
have added more records, export the PRODUIT rows from SQLite and import them into
MySQL before relying on the new database. Check duplicate codes and price rounding
when importing. Keep the SQLite backup until you verify the imported rows.

## Common connection errors

- `No suitable driver`: run the Maven command and include target/dependency/*.
- `Access denied`: check the username, password, and account's permitted host.
- `Unknown database 'tp6'`: complete step 2 on the server named in TP6_DB_URL.
- `Communications link failure`: check server status, host, port, and connectivity.

References: [JDBC URL format](https://dev.mysql.com/doc/connector-j/en/connector-j-reference-jdbc-url-format.html),
[CHECK constraints](https://dev.mysql.com/doc/refman/8.4/en/create-table-check-constraints.html),
[implicit DDL commits](https://dev.mysql.com/doc/refman/8.4/en/implicit-commit.html).
