package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.RowInterface;

/**
 * Created by José Carlos López on 11/10/2016.
 */
public class Row implements RowInterface {

    /**
     * Position in file
     */
    private long position;
    private Cell[] cells;

    public Row() {
    }

    public Row(Cell[] cells, long position) {
        this.position = position;
        this.cells = cells;
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
    public Cell[] fetchData() {
        return cells;
    }

    @Override
    public void setCells(Cell[] cells) {

    }

}
