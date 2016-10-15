package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.ColumnInterface;

/**
 * Creates a new Column definition.<br/>
 * The column has the following structure on a file: <br/>
 * <ul>
 * <li>Name - 25 chars (50 bytes)</li>
 * <li>Table Identifier - 4 bytes</li>
 * <li>Unique ID - 4 bytes</li>
 * <li>Type - 1 byte</li>
 * <li>Table reference ID* - 4 bytes</li>
 * <li>Column ID reference* - 4 bytes</li>
 * </ul><br/>
 * <p>
 * Size of the register, based on data type:
 * <p>
 * <ul>
 * <li><strong>String:</strong> size = [0-25]</li>
 * <li><strong>Integer:</strong> size = 4</li>
 * <li><strong>Double:</strong> size = 8</li>
 * </ul><br/>
 * <p>
 * This can be stored in 1 byte.<br/>
 * <p>
 * <p>
 * Notes:
 * <ul>
 * <li>Fields labelled with (*) come after the data type and size declaration, because they are optional.</li>
 * <li>Columns are delimited in the file by the CRLF (0xD0A)</li>
 * </ul>
 * <p>
 * <p>
 * <p>
 * <p>
 * TODO: Optimize registerSize value only if the type is a String, also check it on {@link Table}
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
    private int rtid = -100;
    private int rcid = -100;

    /**
     * Size of the register in the file
     */
    private byte size;
    /**
     * Offset from the register position
     */
    private byte offset = 0;

    private int typeAsByte;


    /**
     * Creates a new Column definition.<br/>
     * The column has the following structure on a file: <br/>
     * <ul>
     * <li>Name - 25 chars (50 bytes)</li>
     * <li>Table Identified - 4 bytes</li>
     * <li>Unique ID - 4 bytes</li>
     * <li>Type - 1 byte</li>
     * <li>Table reference ID* - 4 bytes</li>
     * <li>Column ID reference* - 4 bytes</li>
     * </ul><br/>
     * <p>
     * Size of the register, based on data type:
     * <p>
     * <ul>
     * <li><strong>String:</strong> size = [0-25]</li>
     * <li><strong>Integer:</strong> size = 4</li>
     * <li><strong>Double:</strong> size = 8</li>
     * </ul><br/>
     * <p>
     * This can be stored in 1 byte.<br/>
     * <p>
     * Notes:
     * <ul>
     * <li>Fields labelled with (*) come after the data type and size declaration, because they are optional.</li>
     * <li>Columns are delimited in the file by the CRLF (0xD0A)</li>
     * </ul>
     * <p>
     * <p>
     * <p>
     * Created by José Carlos López on 11/10/2016.
     */
    private Column() {
    }

    /**
     * Creates a new instance of the Column type for a String type
     *
     * @param name
     * @param length
     * @param UCID
     * @return
     * @throws Exception
     */
    public static Column newString(String name, byte length, int UCID) throws Exception {
        if (length > 25)
            throw new Exception("String size can't be more than 25 characters");
        Column tmp = _newColumn(name, Type.STRING, UCID);
        tmp.size = length;
        return tmp;
    }


    /**
     * Creates a new Column of String type, for the given table id.
     *
     * @param name
     * @param UTID
     * @param UCID
     * @param length
     * @return
     * @throws Exception
     */
    public static Column newString(String name, int UTID, int UCID, byte length) throws Exception {
        Column tmp = newString(name, length, UCID);
        tmp.tid = UTID;
        return tmp;
    }

    /**
     * Creates a new instance of the Column type for an Integer Type
     *
     * @param name
     * @param UCID
     * @return
     * @throws Exception
     */
    public static Column newInteger(String name, int UCID) throws Exception {
        return _newColumn(name, Type.INTEGER, UCID);
    }

    /**
     * Creates a new instance of the Column type for an Integer Type
     *
     * @param name
     * @param UTID
     * @param UCID
     * @return
     * @throws Exception
     */
    public static Column newInteger(String name, int UTID, int UCID) throws Exception {
        Column tmp = newInteger(name, UCID);
        tmp.tid = UTID;
        return tmp;
    }

    /**
     * Creates a new instance of the Column type for a Double type
     *
     * @param name
     * @param UCID
     * @return
     * @throws Exception
     */
    public static Column newDouble(String name, int UCID) throws Exception {
        return _newColumn(name, Type.DOUBLE, UCID);
    }

    /**
     * Creates a new instance of the Column type for a Double type
     *
     * @param name
     * @param UTID
     * @param UCID
     * @return
     * @throws Exception
     */
    public static Column newDouble(String name, int UTID, int UCID) throws Exception {
        Column tmp = newDouble(name, UCID);
        tmp.tid = UTID;
        return tmp;
    }

    /**
     * Creates a new column for the given type.
     *
     * @param name
     * @param type
     * @param UCID
     * @return
     * @throws Exception
     */
    private static Column _newColumn(String name, Type type, int UCID) throws Exception {
        Column tmp = new Column();
        StringBuilder sb = new StringBuilder(name.length() + 2);
        sb.append(name).append("\r\n");
        tmp.name = sb.toString(); // Column name is delimited by a CRLF
        switch (type) {
            case INTEGER:
                tmp.size = 4;
                break;
            case DOUBLE:
                tmp.size = 8;
                break;
            case STRING: // Size is defined in the parent method that defines
                break;
            case UNASSIGNED:
            case DELETED:
            default:
                throw new Exception("Type is missing");
        }
        tmp.type = type;
        tmp.cid = UCID;
        return tmp;
    }

    private byte _calculateSize() {
        switch (type) {
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

    public static Type calculateType(byte b) {
        switch (b) {
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
        this.size = size;
    }

    @Override
    public byte getRegisterSize() {
        return this.type == Type.STRING ? (byte) (this.size * 2) : this.size;
    }

    @Override
    public boolean hasReferences() {
        return !(rtid == -100 && rcid == -100);
    }

    @Override
    public byte getOffset() {
        return offset;
    }

    @Override
    public void setOffset(byte offset) {
        this.offset = offset;
    }

    public byte getTypeAsByte() {
        switch (this.type) {
            case INTEGER:
                return 0;
            case DOUBLE:
                return 1;
            case STRING:
                return 2;
            case UNASSIGNED:
                return -1;
        }
        return -1;
    }
}
