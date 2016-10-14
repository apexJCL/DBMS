package io.github.apexjcl.objects;

import io.github.apexjcl.entities.Column;

/**
 * Created by José Carlos López on 12/10/2016.
 */
public class TableDescriptor {

    private String name;
    private Column[] columns;

    /**
     * Creates a new Table Descriptor, this is meant to express how a table is going to be
     * constructed, based on Objects that can be created without needing a file, thus only
     * one can create a table in memory, then write it to a file
     *
     * @param name       Name of the table
     * @param columns    Columns of the table
     */
    public TableDescriptor(String name, Column[] columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public Column[] getColumns() {
        return columns;
    }
}
