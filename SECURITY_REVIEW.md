# TP4–TP6 security review

Scope: manual review of TP4–TP6 Java, shell, SQL, Maven configuration, and text
 documentation; targeted dependency-advisory checks. This is not a complete
 dependency vulnerability scan or a penetration test. PDF contents were not
 inspected with a PDF parser and require a separate inclusion decision.

## Findings addressed

- Connector/J 9.1.0 resolved protobuf-java 4.26.1. The Protobuf maintainer lists
  this version in the affected range for CVE-2024-7254, a denial-of-service issue
  when parsing crafted messages. Maven dependency management now selects 4.28.2,
  a listed patched release. The application's ordinary JDBC path does not itself
  demonstrate exploitation of the Protobuf parser; this removes a known vulnerable
  transitive component, not evidence of an attack.
  Source: https://github.com/protocolbuffers/protobuf/security/advisories/GHSA-735f-pc8j-v9w8
- ProductPanel previously displayed arbitrary exception text, potentially exposing
  server/account/SQL details. It now displays fixed messages, preserving useful
  duplicate-code and missing-record errors without raw exception contents.
- Added repository ignore rules for databases, generated classes/JARs, analysis
  outputs, environment files, and private keys. This prevents accidental staging
  through normal git add; explicit force-add can still bypass ignore rules.
- Replaced machine-specific addresses and absolute user paths in the public setup
  guides with generic examples. Password examples are placeholders or hidden
  interactive prompts, not real credentials.

## Remaining configuration considerations

- Connector/J defaults to sslMode=PREFERRED, which can fall back to unencrypted
  transport and does not verify server identity. For deployments beyond the local
  lab, configure trusted certificates and sslMode=VERIFY_IDENTITY. This was not
  forced on the current self-signed local MySQL setup because it would require
  server/trust-store configuration and could break the working lab connection.
  Source: https://dev.mysql.com/doc/connector-j/en/connector-j-connp-props-security.html
- Use a dedicated database account restricted to the client host and tp6 schema.
  Avoid using root for the application. The initializer needs CREATE; ordinary
  CRUD needs SELECT, INSERT, UPDATE, and DELETE. The GUI currently creates the table
  at startup, so its account still needs CREATE unless initialization is separated.
- No hardcoded database password was found in the reviewed Java/text files.
  Credentials are read from environment variables. That finding does not cover
  PDFs, local shell history, database server configuration, or previous Git history.
- The SQL CRUD operations bind user values with PreparedStatement; no user-input
  SQL concatenation was found. The schema also enforces basic value constraints.
- TP4 and TP5 are console exercises with no network/database access found. Their
  sample names/identifiers are embedded lab data; authors should confirm these
  are fictional before publishing accompanying reports.
- The calculator's division-by-zero guard uses a different lambda instance from
  its button callback; it can show Infinity. This is an existing correctness issue,
  not a demonstrated security exploit, and was not changed in this security pass.

## Checks

- TP4 compiled and its 17 assertion checks passed.
- TP5 compiled and its console demonstration ran.
- TP6 clean Maven compilation passed, with dependency tree resolving Protobuf 4.28.2.
- Temporary headless Swing checks cover load, navigation, price validation, add,
  update, and rendering. No live database was modified during these checks.

The intended commit excludes local databases, build artifacts, generated analysis,
secrets, and unrelated IDE settings. Documentation and PDFs are separate approval
choices before staging and committing.
