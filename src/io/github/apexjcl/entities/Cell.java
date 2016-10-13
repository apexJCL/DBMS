package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.CellInterface;
import io.github.apexjcl.interfaces.ColumnInterface;

import java.util.Objects;

/**
 * Cell implementation, the cell can do the following tasks:
 * <ul>
 * <li>Retrieve Value</li>
 * <li>Update value</li>
 * </ul>
 * <p>
 * Created by José Carlos López on 11/10/2016.
 */
public class Cell implements CellInterface {

    private long position;
    private Column column;
    private Object value;

    public Cell() {
    }

    public Cell(Column columnReference, Object value, long filePosition) {
        this.column = columnReference;
        this.value = value;
        this.position = filePosition;
    }

    @Override
    public long getFilePosition() {
        return position;
    }

    @Override
    public byte getSize() {
        return column.getRegisterSize();
    }

    @Override
    public void setColumnDefinition(Column column) {
        this.column = column;
    }

    @Override
    public Column getColumnDefinition() {
        return column;
    }

    @Override
    public ColumnInterface.Type getType() {
        return column.getType();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }
}
