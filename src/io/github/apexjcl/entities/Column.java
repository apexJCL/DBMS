package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.ColumnInterface;

/**
 * Creates a new Column definition.<br/>
 * The column has the following structure on a file: <br/>
 * <ul>
 *     <li>Name - 25 chars (50 bytes)</li>
 *     <li>Table Identified - 4 bytes</li>
 *     <li>Unique ID - 4 bytes</li>
 *     <li>Type - 1 byte</li>
 *     <li>Table reference ID* - 4 bytes</li>
 *     <li>Column ID reference* - 4 bytes</li>
 * </ul><br/>
 *
 * Size of the register, based on data type:
 *
 * <ul>
 *     <li><strong>String:</strong> size = [0-25]</li>
 *     <li><strong>Integer:</strong> size = 4</li>
 *     <li><strong>Double:</strong> size = 8</li>
 * </ul><br/>
 *
 * This can be stored in 1 byte.<br/>
 *
 * Notes:
 * <ul>
 *     <li>Fields labelled with (*) come after the data type and size declaration, because they are optional.</li>
 *     <li>Columns are delimited in the file by the CRLF (0xD0A)</li>
 * </ul>
 *
 *
 *
 * Created by José Carlos López on 11/10/2016.
 */
public class Column implements ColumnInterface {

    /**
     * Column name | type
     */
    private String name;
    private Type type;
    /**
     * Table | Column ID
     */
    private int tid;
    private int cid;
    /**
     * Reference table | column ID
     */
    private int rtid;
    private int rcid;

    /**
     * Size of the register in the file
     */
    private byte size;


    /**
     * Creates a new Column definition.<br/>
     * The column has the following structure on a file: <br/>
     * <ul>
     *     <li>Name - 25 chars (50 bytes)</li>
     *     <li>Table Identified - 4 bytes</li>
     *     <li>Unique ID - 4 bytes</li>
     *     <li>Type - 1 byte</li>
     *     <li>Table reference ID* - 4 bytes</li>
     *     <li>Column ID reference* - 4 bytes</li>
     * </ul><br/>
     *
     * Size of the register, based on data type:
     *
     * <ul>
     *     <li><strong>String:</strong> size = [0-25]</li>
     *     <li><strong>Integer:</strong> size = 4</li>
     *     <li><strong>Double:</strong> size = 8</li>
     * </ul><br/>
     *
     * This can be stored in 1 byte.<br/>
     *
     * Notes:
     * <ul>
     *     <li>Fields labelled with (*) come after the data type and size declaration, because they are optional.</li>
     *     <li>Columns are delimited in the file by the CRLF (0xD0A)</li>
     * </ul>
     *
     *
     *
     * Created by José Carlos López on 11/10/2016.
     */
    public Column(){}

    /**
     * Creates a new table column definition
     * @param name    Name of the column
     * @param type    Data type the column will handle
     * @param tableID   Table identifier
     * @param uniqueCID    Unique Column Identifier, based per table
     */
    public Column(String name, Type type, int tableID,int uniqueCID){
        this.name = name;
        this.cid = uniqueCID;
        this.tid = tableID;
        this.type = type;
        this.size = _calculateSize();
    }

    private byte _calculateSize(){
        switch (type){
            case INTEGER:
                return 4;
            case DOUBLE:
                return 8;
            case STRING:
                return 50 * 2;
            default:
                return -1;
        }
    }

    public static Type calculateType(byte b){
        switch (b){
            case 0:
                return Type.INTEGER;
            case 1:
                return Type.DOUBLE;
            case 2:
                return Type.STRING;
        }
        return Type.UNASSIGNED;
    }

    @Override
    public byte getSize() {
        return size;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setTableID(int id) {
        this.tid = id;
    }

    @Override
    public int getTableID() {
        return tid;
    }

    @Override
    public void setColumnID(int id) {
        this.cid = id;
    }

    @Override
    public int getColumnID() {
        return cid;
    }

    @Override
    public void setTableReference(int id) {
        this.rtid = id;
    }

    @Override
    public int getTableReference() {
        return rtid;
    }

    @Override
    public void setColumnReference(int id) {
        this.rcid = id;
    }

    @Override
    public int getColumnReference() {
        return rcid;
    }

    @Override
    public void setRegisterSize(byte size) {

    }

    @Override
    public byte getRegisterSize() {
        return 0;
    }
}
