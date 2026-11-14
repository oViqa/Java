#!/usr/bin/env bash
set -euo pipefail

project_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

if ! command -v java >/dev/null || ! command -v mvn >/dev/null; then
    echo 'Java 17+ and Maven are required.' >&2
    exit 1
fi

java_binary="$(readlink -f -- "$(command -v java)")"
java_home_dir="$(dirname -- "$(dirname -- "$java_binary")")"
if [[ "$(uname -s)" == Linux && ! -f "$java_home_dir/lib/libawt_xawt.so" ]]; then
    echo 'This Java installation cannot open Swing windows (headless runtime).' >&2
    echo 'In Ubuntu, install desktop support first:' >&2
    echo '  sudo apt install openjdk-17-jre' >&2
    echo 'Enter your Ubuntu password, then run this launcher again.' >&2
    exit 1
fi

if [[ -z "${DISPLAY:-}" ]]; then
    echo 'No graphical display is available. Run from a WSLg desktop terminal.' >&2
    exit 1
fi

mvn -f "$project_dir/pom.xml" compile dependency:copy-dependencies
exec java -cp "$project_dir/target/classes:$project_dir/target/dependency/*" tp6.exercise2.MainFrame
