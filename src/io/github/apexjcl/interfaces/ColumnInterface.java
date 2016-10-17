package io.github.apexjcl.interfaces;

import io.github.apexjcl.entities.Column;

/**
 * This interface defines the behaviour of the column, as basic methods that must be there.
 * <p>
 * Created by José Carlos López on 11/10/2016.
 */
public interface ColumnInterface {

    void setName(String name);

    String getName();

    void setType(Type type);

    /**
     * Returns the data type this column handles
     *
     * @return
     */
    Type getType();

    void setTableID(int id);

    int getTableID();

    void setColumnID(int id);

    int getColumnID();

    void setTableReference(int id);

    int getTableReference();

    void setColumnReference(int id);

    int getColumnReference();

    void setRegisterSize(byte size);

    /**
     * Returns the max amount of bytes the column has designed for storage in file per register
     *
     * @return
     */
    byte getRegisterSize();

    /**
     * Returns if the column has any reference to another in another table or the same table
     *
     * @return
     */
    boolean hasReferences();

    /**
     * Within each register, lays an absolute position for each value.
     * <p>
     * This method returns the offset from the register position to the beginning of the same
     *
     * @return
     */
    byte getOffset();

    void setOffset(byte offset);

    byte _getNameCharAmount();

    // Types

    /**
     * Types of data that can be stored
     */
    enum Type {
        INTEGER, DOUBLE, STRING, UNASSIGNED, DELETED
    }

}
