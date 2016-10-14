package io.github.apexjcl.interfaces;

import io.github.apexjcl.entities.Cell;

/**
 * Created by José Carlos López on 11/10/2016.
 */
public interface RowInterface {

    /**
     * Returns its position on the table file
     *
     * @return
     */
    long getFilePosition();
    void setFilePosition(long position);

    /**
     * Returns the cells contained in a row
     *
     * @return
     */
    Cell[] fetchData();

    /**
     * Defines the content of the row.
     *
     * Be noted that this DOES NOT writes to file, only modifies the object in memory,
     * thus you must call an update or insert method on the table that this row belongs to
     *
     * @param cells
     */
    void setCells(Cell[] cells);

}
