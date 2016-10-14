package io.github.apexjcl.interfaces;

import io.github.apexjcl.entities.Table;
import io.github.apexjcl.objects.TableDescriptor;

/**
 * Describes the behaviour of a database
 * <p>
 * Created by José Carlos López on 11/10/2016.
 */
public interface DatabaseInterface {

    /**
     * Returns the table based on it's unique identifier
     *
     * @param id
     * @return
     */
    Table getTableByID(int id) throws Exception;

    /**
     * Returns a table based on it's name
     *
     * @param name
     * @return
     */
    Table getTableByName(String name) throws Exception;

    /**
     * Creates a new table based on the Table Descriptor.
     * <p>
     * An exception occurs if the table already exists or if the data
     * is invalid for the generation of a table.
     *
     * @param tableDescriptor
     * @return
     */
    boolean createTable(TableDescriptor tableDescriptor) throws Exception;

    /**
     * Tries to drop (delete) a table
     *
     * @param tableName Name of the table
     * @return
     * @throws Exception If the table does not exists or any other reason not defined yet
     */
    boolean dropTable(String tableName) throws Exception;

}
