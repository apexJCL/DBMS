package io.github.apexjcl.interfaces;

import io.github.apexjcl.entities.Column;
import io.github.apexjcl.entities.Row;

/**
 * Created by José Carlos López on 11/10/2016.
 */
public interface TableInterface {

    /**
     * Gets the table name
     * @return
     */
    String getName();

    /**
     * Returns the amount of registers in the table
     * @return
     */
    int registerAmount();

    /**
     * Returns the amount of columns that the table has
     *
     * @return
     */
    int columnAmount();

    /**
     * Indicates where is the table file
     * @param path
     */
    void setFilePath(String path);

    /**
     * Returns the table filepath
     * @return
     */
    String getFilePath();

    /**
     * Defines the table unique id on runtime
     * @param id
     */
    void setTableID(int id);

    /**
     * Returns the table unique id
     *
     * @return
     */
    int getTableID();

    /**
     * Returns the row size to be assigned in the file
     * @return
     */
    int getRowSize();

    /**
     * Returns the amount of defined index
     * @return
     */
    byte getIndexAmount();

    /**
     * Retrieves a row with the specified data
     * @param position File position to find and fetch a full row
     * @return Row object
     */
    Row fetchRow(long position);

    /**
     * Allows for fetching of a row and the specified columns
     *
     * @param columns Columns to get from the row
     * @param position File position to find and fetch the row
     * @return Row Object
     */
    Row fetchRow(Column[] columns, long position);




}
