package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.ColumnInterface;
import io.github.apexjcl.interfaces.TableInterface;
import io.github.apexjcl.utils.RandomIO;

import java.io.IOException;

/**
 * Creates a new Table object based on a file that was previously created with specific data
 * for handling the table definition.
 * <p>
 * File structure goes as following:<br/>
 * Header:
 * <ul>
 * <li>Unique ID - 4 bytes</li>
 * <li>Amount of current registers - 8 bytes</li>
 * <li>Amount of defined indices - 1 byte</li>
 * <li>Amount of columns - 1 byte</li>
 * <li>Size of row - 8 bytes</li>
 * </ul>
 * <strong>Total size of header is 14 bytes</strong>
 * <p>
 * <p>
 * Created by José Carlos López on 11/10/2016.
 */
public class Table implements TableInterface {

    private String name;
    private String filePath;
    private long registerAmount = 0;
    private Column[] columns;
    private int tid;
    private int rowSize = -1;
    private byte indexAmount = 0;
    private byte colAmount = 0;
    /**
     *
     */
    private RandomIO _file;

    /**
     * Creates a new Table Object, loading it from a file.
     *
     * @param filePath
     * @param name
     */
    public Table(String filePath, String name, int tableID) throws Exception {
        this.name = name;
        this.filePath = filePath;
        this.tid = tableID;
        _file = new RandomIO(filePath + name, RandomIO.FileMode.RW, false);
        _loadTable();
    }

    /**
     * Creates a new Table Object, writing it to a file for data persistence.
     *
     * @param filePath
     * @param name
     * @param tableID
     * @param columns
     * @throws Exception
     */
    public Table(String filePath, String name, int tableID, Column[] columns) throws Exception {
        if (name.length() > 25)
            throw new Exception("Table name is too long (limit, 25 characters)");
        this.name = name;
        this.filePath = filePath;
        this.tid = tableID;
        _file = new RandomIO(filePath + name, RandomIO.FileMode.RW, true);
        _setupTable(columns);
    }

    private void _setupTable(Column[] columns) throws IOException {
        // If the file is new, we need to write the header
        _file.file.writeInt(this.tid); // Table ID
        _file.file.writeLong(this.registerAmount); // Register amount is 0
        _file.file.writeByte(this.indexAmount); // Amount of defined indices
        _file.file.writeByte(this.colAmount); // Amount of columns that comprise the table
        for (Column c : columns) {
            _file.file.writeChars(c.getName()); // Write name
            _file.file.writeInt(c.getTableID()); // Write Table ID
            _file.file.writeInt(c.getColumnID()); // Write Column ID
            _file.file.writeByte(c.getTypeAsByte()); // Write Type ID
            _file.file.writeByte(c.getSize());
            if (c.hasReferences()){
                _file.file.writeInt(c.getTableReference());
                _file.file.writeInt(c.getColumnReference());
            }
            _file.file.write(new byte[]{0x0D, 0x0A}); // CR LF for column ending
        }
    }

    /**
     * Loads the Column definitions that comprise the table from the file
     */
    private void _loadTable() throws IOException {
        this.tid = _file.file.readInt();
        this.registerAmount = _file.file.readLong();
        this.indexAmount = _file.file.readByte();
        this.colAmount = _file.file.readByte();

        columns = new Column[colAmount]; // Loading column definition
        for (byte b = 0; b < colAmount; b++) { // Here we load each column from the file
            columns[b] = new Column();

            _file.file.read(columns[b].getName().getBytes()); // Name
            columns[b].setTableID(_file.file.readInt()); // Table ID
            columns[b].setColumnID(_file.file.readInt()); // Column ID
            columns[b].setType(Column.calculateType(_file.file.readByte())); // Type ID
            columns[b].setRegisterSize(_file.file.readByte()); // Size

            byte[] tmp = {_file.file.readByte(), _file.file.readByte()}; // If applies a reference
            if (tmp[0] != 0x0D && tmp[1] != 0x0A) { // CR LF means end of the column
                _file.file.seek(_file.file.getFilePointer() - tmp.length); // Go back
                columns[b].setTableReference(_file.file.readInt()); // Read Table Reference
                columns[b].setColumnReference(_file.file.readInt()); // Read Column ID reference
            }
        }
    }

    /*************************************************** Interface Behaviour ****************************************************/

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long registerAmount() {
        return registerAmount;
    }

    @Override
    public int columnAmount() {
        return columns.length;
    }

    @Override
    public void setFilePath(String path) {
        this.filePath = path;
    }

    @Override
    public String getFilePath() {
        return filePath;
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
    public int getRowSize() {
        if (this.rowSize >= 0)
            return this.rowSize;

        int size = 0;
        for (Column column :
                columns) {
            size += column.getSize();
        }
        this.rowSize = size;

        return rowSize;
    }

    @Override
    public byte getIndexAmount() {
        return indexAmount;
    }

    @Override
    public Row fetchRow(long position) {
        return null;
    }

    @Override
    public Row fetchRow(Column[] columns, long position) {
        return null;
    }

    /**
     * Deletes a table, does not care about relation, foreign keys or whatever
     *
     * @param database
     * @return
     * @throws Exception
     */
    @Override
    public boolean drop(Database database) throws Exception {
        // TODO: Add constraints like "DROP CASCADE" and "RESTRICT"
        this._file.delete();
        return false;
    }

    @Override
    public boolean insert(Row data) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Row data) throws Exception {
        return false;
    }

    @Override
    public boolean update(Row data) throws Exception {
        return false;
    }
}