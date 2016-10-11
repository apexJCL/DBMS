package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.TableInterface;

/**
 * Created by José Carlos López on 11/10/2016.
 */
public class Table implements TableInterface {

    private String name;
    private String filePath;
    private int registerAmount = 0;
    private Column[] columns;
    private int tid;
    private int rowSize = -1;
    private byte indexAmount = 0;

    /**
     * Creates a new Table object
     *
     * @param filePath Path for file-based storage
     * @param name     Name of the table
     * @param tableID  Unique Table Identifier according to the database
     * @param columns  Columns that describe the table
     */
    public Table(String filePath, String name, int tableID, Column[] columns) {
        this.name = name;
        this.filePath = filePath;
        this.tid = tableID;
        this.columns = columns;
    }



    @Override
    public String getName() {
        return name;
    }

    @Override
    public int registerAmount() {
        return registerAmount;
    }

    @Override
    public int columnAmount() {
        return columns.length;
    }

    @Override
    public void setFilePath(String path) {
        this.filePath = path;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setTableID(int id) {
        this.tid = id;
    }

    @Override
    public int getTableID() {
        return tid;
    }

    @Override
    public int getRowSize() {
        if (this.rowSize >= 0)
            return this.rowSize;

        int size = 0;
        for (Column column :
                columns) {
            size += column.getSize();
        }
        this.rowSize = size;

        return rowSize;
    }

    @Override
    public byte getIndexAmount() {
        return indexAmount;
    }

    @Override
    public Row fetchRow(long position) {
        return null;
    }

    @Override
    public Row fetchRow(Column[] columns, long position) {
        return null;
    }
}