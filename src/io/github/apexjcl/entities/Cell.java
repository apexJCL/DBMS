package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.CellInterface;
import io.github.apexjcl.interfaces.ColumnInterface;
import io.github.apexjcl.utils.RandomIO;

import java.io.IOException;

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

    public Cell(Column columnReference, Object value) {
        this.column = columnReference;
        this.value = value;
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
    public void setFilePosition(long position) {
        this.position = position;
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

    @Override
    public void _read(RandomIO f_tbl) throws IOException {
        switch (column.getType()) {
            case INTEGER:
                value = f_tbl.file.readInt();
                break;
            case DOUBLE:
                value = f_tbl.file.readDouble();
                break;
            case STRING:
                byte size = (byte) (column.getRegisterSize() / 2);
                StringBuilder sb = new StringBuilder(size);
                for (byte i = 0; i < size; i++) {
                    char c = f_tbl.file.readChar();
                    if (c != 0x0)
                        sb.append(c);
                }
                value = sb.toString();
                break;
            case UNASSIGNED:
                break;
            case DELETED:
                break;
        }
    }
}
