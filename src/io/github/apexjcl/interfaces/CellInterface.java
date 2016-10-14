package io.github.apexjcl.interfaces;

import io.github.apexjcl.entities.Column;

/**
 * Defines the behaviour of a normal cell
 * Created by José Carlos López on 11/10/2016.
 */
public interface CellInterface {

    /**
     * Returns the position at were in the file this cell is placed
     * @return
     */
    long getFilePosition();

    void setFilePosition(long position);

    /**
     * Returns the (max) size (in bytes) that the cell can store, based on the data type
     *
     * @return
     */
    byte getSize();

    /**
     * Column definition contains what datatype does this cell has
     *
     * @param column
     */
    void setColumnDefinition(Column column);
    Column getColumnDefinition();

    /**
     * Returns the cell datatype, it can't be set, column must be directly manipuled
     * to achieve this
     * @return
     */
    ColumnInterface.Type getType();

    Object getValue();
    void setValue(Object value);



}
