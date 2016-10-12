package io.github.apexjcl.interfaces;

/**
 * This interface defines the behaviour of the column, as basic methods that must be there.
 *
 * Created by José Carlos López on 11/10/2016.
 */
public interface ColumnInterface {

    /**
     * Returns the max amount of bytes the column has designed for storage in file per register
     *
     * @return
     */
    byte getSize();

    void setName(String name);
    String getName();

    void setType(Type type);

    /**
     * Returns the data type this column handles
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
    byte getRegisterSize();

    /**
     * Types of data that can be stored
     */
    enum Type{
        INTEGER, DOUBLE, STRING, UNASSIGNED
    }

}
