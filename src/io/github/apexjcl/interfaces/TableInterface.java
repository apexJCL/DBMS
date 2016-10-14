package io.github.apexjcl.interfaces;

import io.github.apexjcl.entities.Column;
import io.github.apexjcl.entities.Database;
import io.github.apexjcl.entities.Row;

import java.io.IOException;

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
    long registerAmount();

    /**
     * Returns the file pointer to where a new row can be written to.
     *
     * @return
     */
    long getNextRegisterPosition();

    /**
     * Returns a register by position, ej 1, 2... 4
     *
     * @return
     */
    Row getRegisterByPosition(int position) throws IOException;

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
    Row fetchRow(long position) throws IOException;

    /**
     * Allows for fetching of a row and the specified columns
     *
     * @param columns Columns to get from the row
     * @param position File position to find and fetch the row
     * @return Row Object
     */
    Row fetchRow(Column[] columns, long position) throws IOException;

    /**
     * Drops the table. This means it will delete itself and all related data, relations
     * and so on that can exist.
     *
     * @param db    Database instance so it can search for it's relations and delete pertinent data
     * @return
     * @throws Exception
     */
    boolean drop(Database db) throws Exception;

    /**
     * Basic Operation
     */

    /**
     * Inserts a row into the table
     * @param data
     * @return
     * @throws Exception
     */
    boolean insert(Row data) throws Exception;

    /**
     * Deletes a row from the table
     * @param data
     * @return
     * @throws Exception
     */
    boolean delete(Row data) throws Exception;

    /**
     * Updates a row from the table
     * @param data
     * @return
     * @throws Exception
     */
    boolean update(Row data) throws Exception;

    /**
     * Returns the columns that comprise a table
     *
     * @return
     */
    Column[] getColumns();

}
