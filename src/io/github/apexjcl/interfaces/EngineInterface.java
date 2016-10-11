package io.github.apexjcl.interfaces;

/**
 * Defines the Engine
 *
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



}
