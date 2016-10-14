package io.github.apexjcl.interfaces;

/**
 * Indices store a value and a file position to the register that contains that data.
 *
 * This are ordered inside a binary tree, to reduce search access and make search of data in
 * O(log(n)) time
 *
 * Created by José Carlos López on 14/10/2016.
 */
public interface IndexInterface {

    /**
     * This returns the file position of the register that contains the data
     * that has been indexed
     *
     * @return
     */
    long getRegisterPosition();

    /**
     * Defines the register position in the data file
     *
     * @param position
     */
    void setRegisterPosition(long position);

    /**
     * Defines the value that is going to be indexed in the binary tree
     *
     * @param value
     */
    void setValue(Object value);

}
