package io.github.apexjcl.interfaces;

import io.github.apexjcl.entities.Row;

/**
 * Defines the Engine.
 * <p>
 * Tasks the engine must do:
 * <p>
 * <ul>
 * <li>Create table</li>
 * <li>Delete table</li>
 * <li>Insert into table</li>
 * <li>Update from table</li>
 * <li>Select from table(s)</li>
 * </ul>
 * <p>
 * Created by José Carlos López on 11/10/2016.
 */
public interface EngineInterface {

    /**
     * Tries to call a query to insert data, thus this query must begin with
     * the keyword INSERT, ex: INSERT INTO [table name] [column names] VALUES ([values])
     *
     * @param query
     * @return
     */
    boolean insert(String query) throws Exception;

    /**
     * Updates based on a query string, ex: UPDATE [table name] SET [column = value]
     *
     * @param query
     * @return
     */
    boolean update(String query) throws Exception;


    /**
     * Deletes based on a query string, ex: DELETE [options] FROM [table name]
     *
     * @param query
     * @return
     * @throws Exception
     */
    boolean delete(String query) throws Exception;


    /**
     * Receives a general query and tries to determine which action to take over the Database.
     * <p>
     * This method will deal with:
     * <ul>
     * <li>SELECT</li>
     * <li>INSERT</li>
     * <li>UPDATE</li>
     * <li>DELETE</li>
     * <li>CREATE</li>
     * <li>DROP</li>
     * </ul>
     *
     * @param query
     * @return
     * @throws Exception
     */
    Row query(String query) throws Exception;

}
