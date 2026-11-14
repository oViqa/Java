# TP6 review and MySQL database

The product database code now targets MySQL. Follow [MYSQL_SETUP.md](MYSQL_SETUP.md)
for server setup, credentials, build commands, and database initialization.
The earlier SQLite database is retained as a backup.

## Current status

The existing Java sources compile with Java 17. Exercise 1 still has the issues listed below; Exercise 2 now has a product UI.
Exercise 2 uses a real MySQL database through JDBC.

### Exercise 1: calculator

Files: `exercise1/Main.java` and `exercise1/CalculatorPanel.java`.

- Present: Swing window, two inputs, read-only result, four operations, Clear,
  and a number-format error dialog for empty or malformed inputs.
- Missing: the PDF's Fermer button, heading, and matching French labels/layout.
- Bug: division by zero displays Infinity. The division button creates a new
  lambda, while the guard compares against the separate DIVISION constant by
  identity. Pass DIVISION to the button instead of creating another lambda.
- Validation gap: Double.parseDouble accepts NaN and Infinity. Reject nonfinite
  inputs/results using Double.isFinite. A failed input also leaves the old result visible.
- Build gap: exercise1 is outside src/main/java, so the current Maven build will
  not compile it. Either move the source files under that root while preserving
  their package declarations, or configure an additional Maven source directory.

### Exercise 2: products

The Swing product window is implemented. Run `tp6.exercise2.MainFrame` to open
**Facture / Feuille Produit** with add, update, delete, close, and record navigation.
It uses the MySQL DAO and validates codes, names, and prices. No sample rows are
inserted. See [EXERCISE2_GUIDE.md](EXERCISE2_GUIDE.md) for every implementation
step, launch command, parameter explanation, and manual test sequence.

## MySQL database

`DatabaseConnection` shares `TP6_DB_URL`, `TP6_DB_USER`, and `TP6_DB_PASSWORD`
between `ProductDAO` and the standalone `InitDb` utility. The default URL is
`jdbc:mysql://localhost:3306/tp6`. Create the database and account first, then run
`InitDb` using the instructions in [MYSQL_SETUP.md](MYSQL_SETUP.md).

The MySQL schema uses InnoDB, a positive integer primary key, a nonblank product
name, and nonnegative `DECIMAL(12, 2)` prices. Initialization creates the table
without inserting sample products or changing existing rows.

`produits.db` is the old SQLite file. The application no longer opens it, and
initialization does not import arbitrary existing SQLite records.

## Tools and concepts

| Tool/concept | Role in this project |
| --- | --- |
| Java / JDK | Language, compiler (javac), and runtime (java). Project targets Java 17. |
| Swing | Java's desktop UI toolkit: JFrame, JPanel, JLabel, JTextField, JButton, JOptionPane. |
| AWT / GridLayout | Arranges Swing components in a grid; desktop display required for JFrame. |
| Swing event thread | invokeLater schedules UI work; action listeners respond to button clicks. |
| Lambdas / Calculation | Calculator callbacks implementing a small functional interface. |
| JDBC | Standard Java API for connecting to and querying a database. |
| MySQL Connector/J 9.1.0 | JDBC driver already declared in pom.xml. |
| MySQL | Separate database server; stores products in the tp6 database. |
| SQL | CREATE TABLE, INSERT, SELECT, UPDATE, DELETE and constraints. |
| PreparedStatement | Binds values to SQL parameters rather than concatenating user input. |
| Try-with-resources | Automatically closes JDBC connections, statements and results. |
| DAO | A code organization pattern: ProductDAO isolates SQL access from the future UI. |
| Maven / pom.xml | Build and dependency configuration; compiler plugin 3.13.0, Java release 17. |
| InitDb.java | Standalone Java/JDBC utility used to create the table and list existing products. |
| WSL / Ubuntu | Linux development environment with javac 17; installed Java lacks headful AWT support. |
| Windows Java | Earlier GUI launch succeeded using installed Windows Java 8 and Java-8-compatible calculator classes. |

There is no Spring, Hibernate, JavaFX, web server, or ORM in the current code.
Maven is not currently available in the Ubuntu terminal. Its dependency declaration
does not by itself put the JDBC JAR on the classpath of a manually launched java process.

Exercise 2 uses Java text blocks and therefore cannot be compiled with --release 8
as written. Use a Java 17 runtime for the complete project; the earlier Java 8
workaround applied only to the calculator.

## Earlier SQLite verification (before migration)

- Compiled all existing Java source files together using javac --release 17.
- Checked calculator operations (+, -, multiplication, division) and Clear
  programmatically without opening a window: passed for ordinary inputs.
- Reproduced division-by-zero bug: 8 / 0 displays Infinity.
- Created and populated the actual SQLite database with three products.
- Tested the existing DAO using the exact JDBC version declared in pom.xml on a
  temporary copy of the database: reading seeds, insert, persisted update, delete,
  missing-record handling, duplicate-code rejection and negative-price rejection passed.
- Product GUI could not be tested: it is not implemented.
- No Maven build was run because mvn is unavailable. No full GUI test was performed
  in this review. Temporary test harnesses and driver downloads are outside the project.
