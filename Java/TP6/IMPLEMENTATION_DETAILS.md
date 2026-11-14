> Historical implementation notes: the database code has since migrated to MySQL.
> SQLite URLs, driver instructions, and atomic schema/seed claims below describe
> the earlier implementation. Use [MYSQL_SETUP.md](MYSQL_SETUP.md) for current setup.

# TP6 — Detailed explanation of exercises 1 and 2

This document describes the code currently saved in TP6. It distinguishes working
code from placeholders and known bugs. It does not claim that both exercises are
finished.

## 1. Project structure

```text
TP6/
├── pom.xml
├── README.md
├── IMPLEMENTATION_DETAILS.md
├── produits.db
├── exercise1/
│   ├── Main.java
│   └── CalculatorPanel.java
├── exercise2/
│   └── database/
│       ├── InitDb.java
│       ├── schema.sql
│       └── seed.sql
└── src/main/java/tp6/exercise2/
    ├── MainFrame.java
    ├── Product.java
    ├── ProductDAO.java
    └── ProductPanel.java
```

`target/`, when present, contains generated compiler output. Its `.class` files
are compiled Java bytecode, not source files to edit. It may contain older classes
from previous folder/package arrangements; the source files above describe the
current implementation.

Current package declarations:

| Files | Package |
| --- | --- |
| Exercise 1 classes | `Java.TP6.exercise1` |
| Exercise 2 model, DAO and UI placeholders | `Java.TP6.src.main.java.tp6.exercise2` |
| `InitDb.java` | No package; a standalone setup program |

A package is part of a Java class's full name. For example, the calculator's
current entry point is `Java.TP6.exercise1.Main`. It is different from the earlier
`Java.TP6.src.main.java.tp6.Main` used before the files were moved.

## 2. Exercise 1 — Simple calculator

### 2.1 Purpose

The calculator accepts two numbers, performs an operation selected by a button,
and displays the result in a third field. It introduces Swing components,
event handling, numeric conversion and exception handling.

### 2.2 Main.java — Starting the application

`public static void main(String[] args)` is the application's entry point.
It uses `SwingUtilities.invokeLater(...)` to schedule window creation on Swing's
Event Dispatch Thread, the thread used to process interface events.

Inside that callback:

1. `new JFrame("Calculatrice")` creates the top-level window.
2. `setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)` makes closing its title-bar
   close button terminate the application.
3. `setContentPane(new CalculatorPanel())` creates and installs the calculator UI.
4. `pack()` sizes the window according to its components' preferred sizes.
5. `setLocationRelativeTo(null)` centers it on the screen.
6. `setVisible(true)` shows it.

Main does not perform arithmetic or access the database. Its role is to start
the calculator window.

### 2.3 CalculatorPanel.java — Components and layout

`CalculatorPanel extends JPanel`: it is a custom Swing panel that can contain
other components. It has three private final fields:

| Field | Role |
| --- | --- |
| `firstNumberField` | User input for the first number |
| `secondNumberField` | User input for the second number |
| `resultField` | Calculated result |

Each is created with `new JTextField(10)`. The value 10 controls the preferred
width in character columns; it does not restrict input to ten characters.
`private` limits direct access to the class. `final` prevents replacing the field's
reference; it does not stop the text inside a text field from changing.

The constructor configures `new GridLayout(0, 2, 8, 8)`:

- Two columns.
- As many rows as needed.
- Eight pixels of horizontal and vertical spacing.

Components are added in order. Labels and text fields appear first, followed by
the four operation buttons and Clear. GridLayout gives cells equal sizes.
`resultField.setEditable(false)` prevents the user from typing a result manually.

### 2.4 Operations and functional interface

The constructor registers these operations:

| Button | Calculation |
| --- | --- |
| `+` | `first + second` |
| `-` | `first - second` |
| `×` | `first * second` |
| `÷` | `first / second` |

The private nested interface `Calculation` declares:

```java
double apply(double first, double second);
```

`@FunctionalInterface` means the interface has one abstract method. A lambda
such as `(first, second) -> first + second` provides that method's behavior.
This allows the same calculation-handling code to support all four operations.

`addOperationButton(String text, Calculation calculation)` creates a JButton,
registers an action listener that calls `calculate(calculation)`, and adds the
button to the panel. The listener runs when the button is activated; registering
it does not immediately perform the calculation.

### 2.5 calculate(...) — Reading and checking inputs

For each operation, this method:

1. Reads each field using `getText()`.
2. Removes surrounding whitespace with `trim()`.
3. Converts the strings into `double` numbers using `Double.parseDouble(...)`.
4. Attempts to check division by zero.
5. Calls `calculation.apply(first, second)`.
6. Converts the result back to text and displays it in `resultField`.

`double` supports decimal values. For example, entering 8 and 2 and clicking
addition displays `10.0`.

The `catch (NumberFormatException ...)` block displays a JOptionPane error dialog
when an input cannot be parsed. Empty fields and ordinary text such as `abc`
reach this error path. Decimal input currently uses a dot, such as `2.5`.
There is no comma-decimal conversion.

### 2.6 Clear button

The Clear listener calls `setText("")` on all three fields. This resets the two
inputs and the result. It does not close the application.

### 2.7 Known limitations and unfinished requirements

- **Division-by-zero bug:** the guard compares `calculation == DIVISION`.
  However, the division button receives a separate lambda rather than the
  `DIVISION` object. This identity comparison does not recognize that button's
  operation. A test with 8 and 0 produced `Infinity`. The intended fix is to pass
  `DIVISION` to the division button, or identify the operation explicitly.
- **Nonfinite input:** `Double.parseDouble` can accept values such as `NaN` and
  `Infinity`. These need an explicit `Double.isFinite` check. Very large finite
  operands can also produce an infinite result.
- **Old result:** an invalid input does not clear a result from a previous calculation.
- **Fermer:** there is no separate Fermer button from the PDF. The title-bar close
  button works, but it does not fulfill the pictured button requirement.
- **Appearance:** English labels, the absent heading and GridLayout arrangement
  do not reproduce the PDF's interface exactly.
- **Maven source location:** the calculator files are outside `src/main/java`,
  so the existing Maven configuration does not compile them automatically.

No database is used by exercise 1.

## 3. Exercise 2 — Product management and JDBC

### 3.1 Current implementation status

The product data model, SQL operations and database initializer are implemented.
The product window and panel are empty placeholders. Therefore the project does
not yet offer a complete graphical product-management application.

### 3.2 Product.java — Representing one product

Product contains four private final fields, initialized by its constructor:

| Java field | Type | SQL column | Meaning |
| --- | --- | --- | --- |
| `code` | `int` | `code` | Product identifier |
| `nom` | `String` | `nom` | Product name |
| `prixAchat` | `double` | `prix_achat` | Purchase price |
| `prixVente` | `double` | `prix_vente` | Sale price |

The getters `getCode()`, `getNom()`, `getPrixAchat()` and `getPrixVente()` expose
these values to other classes. There are no setters: to represent changed values,
the current design creates another Product object.

The constructor does not validate values. The existing database constraints
reject some invalid values, but a future form should validate inputs before
constructing and saving a product.

Prices currently use floating-point values. No currency or rounding policy is
implemented, and the code does not require the sale price to exceed the purchase price.

### 3.3 ProductDAO.java — Database access

DAO stands for Data Access Object, a pattern for keeping persistence operations
in one class rather than mixing SQL directly into button handlers.

The URL is:

```java
private static final String URL = "jdbc:sqlite:produits.db";
```

`jdbc:sqlite:` selects the SQLite driver. `produits.db` is relative to the Java
process's working directory. Run from TP6 to use the database created for this
project. Running elsewhere may create a separate empty database file.

The private `connect()` method calls `DriverManager.getConnection(URL)` and
returns a Connection. The current DAO opens a connection for each operation.
It does not use a connection pool or require a database server/password.

| Method | SQL operation | Behavior |
| --- | --- | --- |
| `createTable()` | `CREATE TABLE IF NOT EXISTS` | Creates PRODUIT if missing; does not insert samples |
| `add(Product)` | `INSERT` | Inserts all four fields; returns no value |
| `update(Product)` | `UPDATE ... WHERE code = ?` | Updates name and prices for the supplied code |
| `delete(int)` | `DELETE ... WHERE code = ?` | Deletes the row with that code |
| `findAll()` | `SELECT ... ORDER BY code` | Returns all products as a `List<Product>` |

Both `update` and `delete` return whether `executeUpdate()` affected at least
one row. A false result means that no matching row was affected. `update` does
not change the product's code; the code identifies which existing row to modify.

`findAll()` builds an ArrayList. It iterates through a ResultSet with `next()`,
reads each row, constructs a Product, and adds it to the list. With no records,
it returns an empty list, not null. The ordering can support navigation later,
but no current method moves a UI selection between products.

### 3.4 JDBC practices in the DAO

Values are bound using PreparedStatement placeholders (`?`) and `setInt`,
`setString`, and `setDouble`. This keeps input values separate from SQL syntax.
For example, a product name containing an apostrophe remains a parameter value.

Try-with-resources closes connections, statements and result sets when each
operation finishes, including when an exception occurs.

Methods declare `throws SQLException`. They pass database failures to their
caller rather than displaying GUI messages. The future UI must catch these
exceptions and explain errors to the user.

### 3.5 MainFrame.java and ProductPanel.java

These classes currently contain no fields, methods or UI code.

- MainFrame does not yet extend JFrame and has no `main` method.
- ProductPanel does not yet extend JPanel.
- There are no product input fields or action listeners.
- No product navigation or selected-record state exists.

Their filenames indicate their intended roles, but those roles are not implemented.

## 4. Database files and initialization

### 4.1 schema.sql

The table definition is:

```sql
CREATE TABLE IF NOT EXISTS PRODUIT (
    code INTEGER PRIMARY KEY CHECK (code > 0),
    nom TEXT NOT NULL CHECK (length(trim(nom)) > 0),
    prix_achat REAL NOT NULL CHECK (prix_achat >= 0),
    prix_vente REAL NOT NULL CHECK (prix_vente >= 0)
);
```

The primary key prevents duplicate codes. The checks require positive codes,
nonblank names and nonnegative prices; NOT NULL requires values for name/prices.
CREATE TABLE IF NOT EXISTS preserves an existing table. It does not migrate an
older table whose columns or constraints differ.

ProductDAO.createTable contains its own copy of this schema. If the table design
changes, both definitions must currently be kept consistent.

### 4.2 seed.sql

This inserts three demonstration products:

| Code | Name | Purchase | Sale |
| --- | --- | --- | --- |
| 1 | Clavier | 80.00 | 120.00 |
| 2 | Souris | 35.00 | 60.00 |
| 3 | Ecran | 900.00 | 1200.00 |

`ON CONFLICT(code) DO NOTHING` skips products whose codes already exist.
Running initialization again does not overwrite modifications to those products
or remove any other records. It is an initializer, not a reset command.

### 4.3 InitDb.java

This is a standalone command-line Java program. It replaced the earlier Python
initializer and uses the same SQLite JDBC library as the DAO.

Execution steps:

1. Read the TP6 directory from the first argument, or use the current directory.
2. Convert it to an absolute normalized Path.
3. Resolve `exercise2/database` for the SQL files and `produits.db` for the database.
4. Check that schema.sql and seed.sql exist; otherwise throw an IOException
   explaining the expected working directory.
5. Open a JDBC connection to the database.
6. Disable auto-commit, so schema creation and seed insertion are one transaction.
7. Read and execute schema.sql, then seed.sql.
8. Commit if both succeed; roll back if an IOException or SQLException occurs.
9. Query the products ordered by code and print the database path and rows.
10. Close JDBC resources automatically.

`runSqlFile(Connection, Path)` reads a whole SQL file with Files.readString and
executes it using a Statement. Each current file contains one complete SQL
statement. This method is not a general multi-statement SQL script parser.

The initializer's `main` declares `throws Exception`, so setup failures are
reported as command-line exceptions. It does not open a window.

### 4.4 produits.db

This is the actual SQLite database, not a Java or SQL source file. It contains
the PRODUIT table and the sample data. Use SQL/JDBC to inspect or modify it rather
than editing it as plain text.

## 5. Build configuration and tools

### 5.1 pom.xml

| Setting | Current value | Purpose |
| --- | --- | --- |
| modelVersion | 4.0.0 | Maven POM format |
| groupId | ma.hiba | Project group identifier |
| artifactId | tp6 | Project identifier |
| version | 1.0-SNAPSHOT | Development version |
| maven.compiler.release | 17 | Target Java release |
| sourceEncoding | UTF-8 | Source text encoding |
| sqlite-jdbc | 3.54.1.0 | External database driver |
| maven-compiler-plugin | 3.13.0 | Java compilation plugin |

Maven normally compiles src/main/java. With the current layout, it does not
automatically compile exercise1 or the standalone initializer. No extra source
directories or application launcher plugin have been configured.

JDBC interfaces are included with Java, while the SQLite JDBC driver is an
external dependency. When running Java manually, put the driver JAR on the
classpath. Declaring it in pom.xml does not change an arbitrary java command.

### 5.2 Technologies actually used

- Java: classes, constructors, methods, access modifiers, interfaces and exceptions.
- Swing: desktop interface components and event handling for the calculator.
- AWT: layout support and desktop graphics infrastructure.
- JDBC: connections, SQL execution, parameter binding and result sets.
- SQLite: embedded relational database stored in a local file.
- SQL: table creation, constraints and CRUD operations.
- Maven: project metadata, compiler configuration and dependency declaration.
- Java NIO: Path and Files for locating and reading setup scripts.
- DAO pattern: separation of product persistence from the planned UI.

The project does not currently use Spring, Hibernate, JavaFX, a web framework,
an ORM, or a database server. Python is no longer required for initialization.

## 6. Commands and environment requirements

Run the following from TP6 in Ubuntu. Java 17 is the project target.

### 6.1 Compile the current sources manually

```bash
javac --release 17 -d target/classes exercise1/*.java src/main/java/tp6/exercise2/*.java exercise2/database/InitDb.java
```

Successful compilation normally prints nothing. It does not launch a window.

### 6.2 Run the calculator

```bash
java -cp target/classes Java.TP6.exercise1.Main
```

This requires graphical support in the Java environment. The Ubuntu runtime
previously raised HeadlessException when creating a JFrame. A working Windows
Java installation was used to display the calculator earlier.

Classes compiled for Java 17 require a compatible runtime. The installed Windows
Java observed earlier was Java 8. To use that runtime for the calculator only,
compile its sources separately with `--release 8`. Exercise 2 uses Java text
blocks and is not Java-8-compatible as written.

### 6.3 Initialize the database using the downloaded driver

```bash
java -cp /tmp/tp6-review/sqlite-jdbc-3.54.1.0.jar exercise2/database/InitDb.java
```

This uses source-file launching, which compiles the initializer in memory and
runs it. The driver under /tmp is a temporary downloaded artifact; if removed,
download dependencies again or use another valid driver JAR location.

With Maven installed, a reusable alternative is:

```bash
mvn dependency:copy-dependencies
java -cp 'target/dependency/*' exercise2/database/InitDb.java
```

There is currently no command to open a completed product form because the
product UI and its application entry point have not been implemented.

## 7. Verification and remaining work

Previously performed checks on this implementation:

- All existing sources compiled using Java 17.
- Ordinary calculator arithmetic and Clear passed programmatic checks.
- Division by zero was reproduced as a bug, producing Infinity.
- InitDb ran successfully and printed the three seeded products.
- DAO checks using the declared driver passed for reading, insertion, persisted
  updates, deletion, missing-record returns, duplicate codes and negative prices.
  Those mutation checks used a temporary copy of the database.

No complete product UI test was possible, and no Maven build was performed
because Maven was unavailable in the Ubuntu terminal during the review.
The database initializer has been run repeatedly without replacing sample rows.

Remaining exercise 1 work: division/finite-number validation, Fermer button,
PDF appearance, and Maven source inclusion.

Remaining exercise 2 work: implement MainFrame and ProductPanel, create the
four input fields, wire add/update/delete/close buttons, implement first/previous/
next/last navigation and empty-database behavior, validate user input, handle SQL
errors in the interface, and initialize/load the database during application startup.
