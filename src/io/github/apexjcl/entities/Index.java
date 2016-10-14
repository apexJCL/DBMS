package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.IndexInterface;

/**
 * Created by José Carlos López on 14/10/2016.
 */
public class Index implements IndexInterface {

    private Object value;
    private long position;

    @Override
    public long getRegisterPosition() {
        return 0;
    }

    @Override
    public void setRegisterPosition(long position) {
        this.position = position;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }
}
