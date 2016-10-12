package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.EngineInterface;

/**
 * Created by José Carlos López on 12/10/2016.
 */
public class Engine implements EngineInterface {

    @Override
    public boolean insert(String query) throws Exception {
        return false;
    }

    @Override
    public boolean update(String query) throws Exception {
        return false;
    }

    @Override
    public boolean delete(String query) throws Exception {
        return false;
    }

    @Override
    public Row query(String query) throws Exception {
        return null;
    }
}
