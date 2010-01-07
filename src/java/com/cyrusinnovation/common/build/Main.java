package com.cyrusinnovation.common.build;

public class Main {
    public static void main(String[] args) {
        try {
            SchemaMigrator migrator = new SchemaMigratorFactory().createMigrator(args);
            migrator.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
