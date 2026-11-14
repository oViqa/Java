# Exercise 2: product window and MySQL

## What changed, step by step

1. Added `src/main/java/tp6/exercise2/MainFrame.java`. This is the application's
   entry point. Its `main` method selects Swing's Nimbus theme and schedules the
   window on Swing's event thread with `SwingUtilities.invokeLater`. `JFrame`
   creates the window titled **Facture**. `pack` sizes it to its panel, and
   `setLocationRelativeTo(null)` centers it. Closing disposes this window.
2. Added `ProductPanel.java`. A `JPanel` groups the components. `JLabel` displays
   the title and four French labels, `JTextField` accepts input, and `JButton`
   triggers actions. BorderLayout places the heading, form/actions, and navigation;
   GridBagLayout aligns the fields; GridLayout spaces the buttons. Borders, fonts,
   pale blue buttons, and field widths reproduce the assignment's layout.
3. Connected each button through `addActionListener`. Ajouter calls `dao.add`,
   Modifier calls `dao.update`, and Supprimer calls `dao.delete` after a yes/no
   confirmation. Fermer closes the window. Modification and deletion target the
   code typed in Code Produit. To add another product, type a new unique code and
   its other values, then click Ajouter.
4. Added validation before SQL: a positive integer code, a trimmed nonempty name
   of at most 255 Unicode characters, and nonnegative prices up to 9999999999.99
   with no more than two decimal places. Both `12.50` and `12,50` are accepted.
   BigDecimal parsing rejects NaN and Infinity. The existing Product model's
   double API is retained.
5. Added ordered navigation. `dao.findAll` already returns products ordered by
   code. The panel keeps a list and selected index. `<<` selects first, `<`
   previous, `>` next, and `>>` last. Boundary buttons are disabled. With no rows,
   the fields are empty. After each write the panel reloads products from MySQL.
6. Added `SwingWorker`: database operations run off the UI thread, while `done`
   updates the form on the UI thread. Fields and write/navigation buttons are
   disabled during work to avoid overlapping operations. Errors appear in French
   dialogs; duplicate codes and missing records are handled. No sample data is
   inserted; startup only ensures the table exists and loads real rows.
7. Kept SQL in ProductDAO and connection settings in DatabaseConnection. The GUI
   uses the same TP6_DB_URL, TP6_DB_USER, and TP6_DB_PASSWORD as CheckConnection.
   Exercise 1 is unchanged.

## Run it from Ubuntu / WSL

For a single build-and-launch command, run:

```bash
bash Java/TP6/run-exercise2.sh
```

`bash` runs the launcher script. The script checks graphical Java support,
builds from the correct POM, and launches MainFrame. Invoke it from the repository root; it locates the POM automatically
and inherits the database environment variables from your terminal. Compilation
alone (`mvn compile`) does not open a window.

Use the terminal where CheckConnection succeeded so your connection environment
variables are still available. Otherwise set them again:

```bash
cd Java/TP6
export TP6_DB_URL='jdbc:mysql://localhost:3306/tp6'
export TP6_DB_USER='tp6_user'
read -r -s -p 'MySQL password: ' TP6_DB_PASSWORD
echo
export TP6_DB_PASSWORD
mvn compile dependency:copy-dependencies
java -cp 'target/classes:target/dependency/*' tp6.exercise2.MainFrame
```

- `cd` changes directory to the folder containing pom.xml.
- `export NAME=value` gives subsequently started programs an environment setting.
  TP6_DB_URL names the server, port, and database. Replace localhost with your reachable Windows host address when using WSL NAT, and use your working
  username if different; the WSL/Windows addresses may change after restarting.
- `read` takes your password from the keyboard. `-r` preserves backslashes, `-s`
  hides the input, and `-p` displays the prompt. The final word is the variable to
  receive your input. `echo` adds a newline, then export makes it available to Java.
- `mvn` runs Maven. `compile` turns src/main/java sources into target/classes.
  `dependency:copy-dependencies` copies Connector/J and other dependency JARs into
  target/dependency. It does not start MySQL.
- `java` starts Java. `-cp` specifies the classpath. `target/classes` contains the
  compiled classes, `:` separates entries on Linux, and `target/dependency/*`
  includes the library JARs. Quotes let Java handle the wildcard. MainFrame is
  the runnable class inside package tp6.exercise2; omit target/classes from its name.

This opens a desktop window and requires a graphical Java runtime and a display.
If Ubuntu reports a missing libawt_xawt.so or headless-only runtime, install the
headful runtime with `sudo apt install openjdk-17-jre`: sudo asks for your Ubuntu
password (not the MySQL password) and runs as administrator,
apt manages Ubuntu packages, install adds the named package. WSL also needs a
working GUI display (for example WSLg). An IDE run configuration must have the
same environment variables and use tp6.exercise2.MainFrame as its main class.

## Try the form

1. Enter an unused code, a name, and two prices; click Ajouter.
2. Change that product's name or prices; click Modifier.
3. Use the four arrows to move through existing records.
4. Try an empty name, negative price, duplicate code, or more than two decimal
   places: the form should report the problem.
5. Select the temporary product, click Supprimer, and confirm. Existing unrelated
   products remain intact.
6. Click Fermer to close the window.

The application writes to the real database configured in your environment.

## Verification

Maven compilation passed. A temporary headless Swing harness tested initial load,
record navigation, invalid prices, adding, updating, and panel rendering with an
in-memory DAO substitute. This substitute is only in /tmp, not application code.
A live MySQL GUI session and interactive delete confirmation need verification
with your credentials and a graphical runtime; no real database rows were changed
by the automated checks.
