package io.github.apexjcl.interfaces;

import io.github.apexjcl.entities.Table;

/**
 * Describes the behaviour of a database
 *
 * Created by José Carlos López on 11/10/2016.
 */
public interface DatabaseInterface {

    /**
     * Returns the table based on it's unique identifier
     *
     * @param id
     * @return
     */
    Table getTableByID(int id);

    /**
     * Returns a table based on it's name
     *
     * @param name
     * @return
     */
    Table getTableByName(String name);

}
