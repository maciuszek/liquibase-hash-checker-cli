package com.liquibasehashchecker;

import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.FileSystemResourceAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Liquibase Hash Checker
 *
 * Get the hash code of liquibase changelog files - supplied as paths to file over cli arguments (if loadData is
 * referenced in the changelog file, copy the full referenced path to directory where you invoke the jvm)
 *
 * note: liquibase version may affect the hash
 */
public class App
{
    public static void main( String[] args ) throws LiquibaseException, InterruptedException {
//        TimeUnit.SECONDS.sleep(10); // for debugging
        final Map<String, String> changeSetHashes = new HashMap<>();
        for (final String filePath : args) {
            final Database database = DatabaseFactory.getInstance().getDatabase(System.getProperty("liquibase-hash-checker.database.type")); // database type may affect hash (especially if database is null)
            final Liquibase liquibase = new Liquibase(filePath, new FileSystemResourceAccessor(), database);
            final ChangeLogParser parser = ChangeLogParserFactory.getInstance().getParser(filePath, new FileSystemResourceAccessor());
            final DatabaseChangeLog databaseChangeLog = parser.parse(filePath, liquibase.getChangeLogParameters(), new FileSystemResourceAccessor());
            for (final ChangeSet changeSet : databaseChangeLog.getChangeSets()) {
                changeSetHashes.put(changeSet.getId(), changeSet.generateCheckSum().toString());
            }
        }
        System.out.println(changeSetHashes);
    }

}
