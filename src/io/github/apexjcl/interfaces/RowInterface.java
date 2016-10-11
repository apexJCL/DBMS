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

    Cell[] fetchData();

}
