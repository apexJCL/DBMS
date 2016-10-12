package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.ColumnInterface;
import io.github.apexjcl.interfaces.TableInterface;
import io.github.apexjcl.utils.RandomIO;

import java.io.IOException;

/**
 * Creates a new Table object based on a file that was previoulsy created with specific data
 * for handling the table definition.
 * <p>
 * File structure goes as following:<br/>
 * Header:
 * <ul>
 * <li>Unique ID - 4 bytes</li>
 * <li>Amount of current registers - 8 bytes</li>
 * <li>Amount of defined indices - 1 byte</li>
 * <li>Amount of columns - 1 byte</li>
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
     * Creates a new Table Object loading it from the file or creating a new one
     *
     * @param filePath
     * @param name
     */
    public Table(String filePath, String name, int tableID) throws Exception {
        if (name.length() > 25)
            throw new Exception("Table name is too long (limit, 25 characters)");
        this.name = name;
        this.filePath = filePath;
        this.tid = tableID;
        _setupTable();
    }

    private void _setupTable() throws IOException {
        _file = new RandomIO(filePath + name, RandomIO.FileMode.RW, true);
        if (!_file.newFile) {
            this.tid = _file.file.readInt();
            this.registerAmount = _file.file.readLong();
            this.indexAmount = _file.file.readByte();
            this.colAmount = _file.file.readByte();
            _loadTable();
        } else {
            // If the file is new, we need to write the header
            _file.file.writeInt(this.tid); // Table ID
            _file.file.writeLong(this.registerAmount); // Register amount is 0
            _file.file.writeByte(this.indexAmount); // Amount of defined indices
            _file.file.writeByte(this.colAmount); // Amount of columns that comprise the table
        }
    }

    /**
     * Loads the Column definitions that comprise the table from the file
     */
    private void _loadTable() throws IOException {
        columns = new Column[colAmount];
        for (byte b = 0; b < colAmount; b++) { // Here we load each column from the file
            columns[b] = new Column();
            _file.file.read(columns[b].getName().getBytes()); // Reads column name into column bytes
            columns[b].setTableID(_file.file.readInt());
            columns[b].setColumnID(_file.file.readInt());
            columns[b].setType(Column.calculateType(_file.file.readByte()));
            columns[b].setRegisterSize(_file.file.readByte());
            byte[] tmp = {_file.file.readByte(), _file.file.readByte()};
            if (tmp[0] != 0x0D && tmp[1] != 0x0A) { // CR LF
                _file.file.seek(_file.file.getFilePointer() - tmp.length);
                columns[b].setTableReference(_file.file.readInt());
                columns[b].setColumnID(_file.file.readInt());
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
}