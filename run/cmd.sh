#!/bin/bash

# for debugging add: -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
java -cp ../target/liquibase-hash-checker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar \
-Dload.db.dataset=test -Dliquibase-hash-checker.database.type=mysql \
com.liquibasehashchecker.App "$@"

exit 0
