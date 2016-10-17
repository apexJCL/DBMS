package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.ColumnInterface;
import io.github.apexjcl.interfaces.TableInterface;
import io.github.apexjcl.utils.RandomIO;
import io.github.apexjcl.utils.StringConstants;
import io.github.apexjcl.utils.Utils;

import java.io.IOException;
import java.util.HashMap;

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
 * <li>Size of row - 4 bytes</li>
 * <li>Header size (including column definitions) - 4 bytes</li>
 * </ul>
 * <strong>Total size of header is 18 bytes</strong>
 * <p>
 * <p>
 * Created by José Carlos López on 11/10/2016.
 */
public class Table implements TableInterface {

    private String name;
    private String filePath;
    private long registerAmount = 0;
    private Column[] columns;
    private int utid;
    private int rowSize = -1;
    private byte indexAmount = 0;
    private byte colAmount = 0;
    private byte[] CRLF = {0x0D, 0X0A};
    /**
     * Unique Column Identifier
     */
    private byte ucid;

    /**
     * File for table configuration
     */
    private RandomIO f_conf;
    /**
     * File for table description
     */
    private RandomIO f_desc;
    /**
     * HashMap for indices
     */
    private HashMap<String, RandomIO> f_idx;
    /**
     * File for table raw data
     */
    private RandomIO f_tbl;

    /**
     * Loads an existing file
     *
     * @param filePath
     * @param name
     */
    public Table(String filePath, String name, int tableID) throws Exception {
        this.name = name.substring(0, name.indexOf('.'));
        this.filePath = filePath;
        this.utid = tableID;
        _loadDescription();
        _loadConfiguration();
        if (indexAmount > 0)
            _loadIndices();
        f_tbl = new RandomIO(Utils.concatFilepath(filePath, this.name, StringConstants.TABLE_EXTENSION), RandomIO.FileMode.RW, false);
    }

    /**
     * Creates a new table physically, creating the necessary files.
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
        this.utid = tableID;
        this.columns = columns;
        this.colAmount = (byte) columns.length;
        this.rowSize = getRowSize();
        _setupDescription();
        _setupConfiguration();
        if (indexAmount > 0)
            _setupIndices();
        f_tbl = new RandomIO(Utils.concatFilepath(filePath, name, StringConstants.TABLE_EXTENSION), RandomIO.FileMode.RW, true);
    }

    private void _setupIndices() {
        for (byte i = 0; i < indexAmount; i++) {
            // TODO: write indices definition and write process
        }
    }

    private void _loadIndices() {
        for (byte i = 0; i < indexAmount; i++) {
            // TODO: write indices definition and write process
        }
    }

    /**
     * Setups description file
     *
     * @throws IOException
     */
    private void _setupDescription() throws IOException {
        f_desc = new RandomIO(Utils.concatFilepath(filePath, name, StringConstants.TABLE_DESC_EXTENSION), RandomIO.FileMode.RW, true);
        f_desc.file.writeByte(colAmount);
        ucid = 0; // Unique Column Identifier
        byte offset = 0;
        for (Column c : columns) {
            f_desc.file.writeByte(c.getTypeAsByte()); // Write Type
            f_desc.file.writeByte(c._getNameCharAmount());
            f_desc.file.writeChars(c.getName()); // Write name
            f_desc.file.writeInt(this.utid); // Write Table ID
            f_desc.file.writeInt(ucid++); // Write Column ID
            f_desc.file.writeByte(c.getRegisterSize()); // Write registerSize
            f_desc.file.writeByte(offset);
            offset += c.getRegisterSize(); // Increase offset
            if (c.hasReferences()) { // If the column is a reference to another
                f_desc.file.writeInt(c.getTableReference()); // Write it
                f_desc.file.writeInt(c.getColumnReference());
            }
            f_desc.file.write(CRLF); // CR LF for column ending
        }
        f_desc.file.writeInt(rowSize); // Row Size
    }

    /**
     * Loads description file into memory
     *
     * @throws IOException
     */
    private void _loadDescription() throws Exception {
        f_desc = new RandomIO(Utils.concatFilepath(filePath, name, StringConstants.TABLE_DESC_EXTENSION), RandomIO.FileMode.RW, false);
        this.colAmount = f_desc.file.readByte();
        columns = new Column[colAmount]; // Loading column definition
        for (byte b = 0; b < colAmount; b++) { // Here we load each column from the file

            ColumnInterface.Type type = Column.calculateType(f_desc.file.readByte()); // Calculate column type
            // Read name
            byte amount = f_desc.file.readByte(); // Amount of characters
            String n = "";
            for (byte i = 0; i < amount; i += 2) {
                n += f_desc.file.readChar();
            }
            switch (type) {
                case INTEGER:
                    columns[b] = Column.newInteger(n, f_desc.file.readInt(), f_desc.file.readInt());
                    columns[b].setRegisterSize(f_desc.file.readByte()); // Size
                    break;
                case DOUBLE:
                    columns[b] = Column.newDouble(n, f_desc.file.readInt(), f_desc.file.readInt());
                    columns[b].setRegisterSize(f_desc.file.readByte()); // Size
                    break;
                case STRING:
                    columns[b] = Column.newString(n, f_desc.file.readInt(), f_desc.file.readInt(), f_desc.file.readByte());
                    break;
                case UNASSIGNED:
                case DELETED:
                default:
                    throw new IOException("Data type could not be found or is missing");
            }
            columns[b].setOffset(f_desc.file.readByte());
            byte[] tmp = {f_desc.file.readByte(), f_desc.file.readByte()}; // If applies a reference
            if (tmp[0] != 0x0D && tmp[1] != 0x0A) { // CR LF means end of the column
                f_desc.file.seek(f_desc.file.getFilePointer() - tmp.length); // Go back
                columns[b].setTableReference(f_desc.file.readInt()); // Read Table Reference
                columns[b].setColumnReference(f_desc.file.readInt()); // Read Column ID reference
            }
        }
        this.rowSize = f_desc.file.readInt();
    }

    /**
     * Setups configuration file
     *
     * @throws IOException
     */
    private void _setupConfiguration() throws IOException {
        f_conf = new RandomIO(Utils.concatFilepath(filePath, name, StringConstants.TABLE_CONF_EXTENSION), RandomIO.FileMode.RW, true);
        f_conf.file.writeInt(utid); // Write Unique table identifier
        f_conf.file.writeLong(registerAmount); // Actual amount of registers
        f_conf.file.writeByte(indexAmount); // AMount of indices defined
        _updateUCID();
    }

    /**
     * Loads configuration file
     *
     * @throws IOException
     */
    private void _loadConfiguration() throws IOException {
        f_conf = new RandomIO(Utils.concatFilepath(filePath, name, StringConstants.TABLE_CONF_EXTENSION), RandomIO.FileMode.RW, false);
        this.utid = f_conf.file.readInt(); // Offset 0x0
        this.registerAmount = f_conf.file.readLong(); // Offset 0x04
        this.indexAmount = f_conf.file.readByte(); // Offset 0x0C
        this.ucid = f_conf.file.readByte(); // Offset 0x0D
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
    public long getNextRegisterPosition() {
        return registerAmount * rowSize;
    }

    @Override
    public Row getRegisterByPosition(int position) throws IOException {
        return fetchRow(position * rowSize);
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
        this.utid = id;
    }

    @Override
    public int getTableID() {
        return utid;
    }

    @Override
    public int getRowSize() {
        if (this.rowSize >= 0)
            return this.rowSize;

        int size = 0;
        for (Column column :
                columns) {
            size += column.getRegisterSize();
        }
        this.rowSize = size;

        return rowSize;
    }

    @Override
    public byte getIndexAmount() {
        return indexAmount;
    }

    @Override
    public Row fetchRow(long position) throws IOException {
        Row tmp = new Row();
        tmp.setFilePosition(position);
        Cell[] cells = new Cell[columnAmount()];
        for (byte i = 0; i < columnAmount(); i++) { // For each existing column
            cells[i] = new Cell(); // Create a new cell
            cells[i].setColumnDefinition(columns[i]);
            cells[i]._read(f_tbl); // read value
        }
        tmp.setCells(cells);
        return tmp;
    }

    @Override
    public Row fetchRow(Column[] columns, long position) throws IOException {
        Row tmp = new Row();  // New Row Object
        tmp.setFilePosition(position); // Base position for row
        Cell[] cells = new Cell[columns.length];
        for (byte i = 0; i < columns.length; i++) {
            //  Offset to file position
            this.f_tbl.file.seek(position);
            cells[i] = new Cell();
            // Seeks the offset based on the column type
            cells[i].setValue(_read(position + _getOffset(columns[i]), columns[i].getType())); // Fetches value
            cells[i].setColumnDefinition(columns[i]);
        }
        tmp.setCells(cells);
        return tmp;
    }

    /**
     * Returns an offset for reading a value based on a column type
     *
     * @param objective
     * @return
     */
    private long _getOffset(Column objective) {
        long offset = 0;
        for (Column c : columns) {
            if (objective.getName() != c.getName())
                offset += c.getRegisterSize();
            else
                return offset;
        }
        return offset;
    }

    private Object _read(long position, ColumnInterface.Type type) throws IOException {
        long tmp = this.f_tbl.file.getFilePointer();
        this.f_tbl.file.seek(position);
        Object object = null;
        switch (type) {
            case INTEGER:
                object = f_tbl.file.readInt();
                break;
            case DOUBLE:
                object = f_tbl.file.readDouble();
                break;
            case STRING:

                object = f_tbl.file.readLine();
                break;
            case UNASSIGNED:
                break;
            case DELETED:
                break;
        }
        f_tbl.file.seek(tmp);
        return object;
    }

    private Object _read(Column column, Cell cell) throws IOException {
        long tmp = f_tbl.file.getFilePointer();
        Object o = null;
        switch (column.getType()) {
            case INTEGER:
                o = f_tbl.file.readInt();
                break;
            case DOUBLE:
                o = f_tbl.file.readDouble();
                break;
            case STRING:
                char[] c = new char[column.getRegisterSize()];
                for (byte i = 0; i < column.getRegisterSize(); i++) {

                }
                break;
            case UNASSIGNED:
            case DELETED:
            default:
                throw new IOException("Data type was not assigned");
        }
        f_tbl.file.seek(tmp);
        return o;
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
        f_tbl.delete();
        return true;
    }

    @Override
    public boolean insert(Row data) throws Exception {
        long tmp = f_tbl.file.getFilePointer();
        this.registerAmount++;
        _updateRegisterAmount();
        for (Cell c : data.fetchData()) {
            c.setFilePosition(data.getFilePosition() + _getOffset(c.getColumnDefinition()));
            _writeData(c, c.getType());
        }
        f_tbl.file.seek(tmp);
        return true;
    }

    @Override
    public boolean delete(Row data) throws Exception {
        long tmp = f_tbl.file.getFilePointer();
        for (Cell c : data.fetchData())
            _writeData(c, ColumnInterface.Type.DELETED);
        f_tbl.file.seek(tmp);
        return true;
    }

    @Override
    public boolean update(Row data) throws Exception {
        long tmp = f_tbl.file.getFilePointer();
        for (Cell c : data.fetchData())
            _writeData(c, c.getType());
        f_tbl.file.seek(tmp);
        return true;
    }

    @Override
    public Column[] getColumns() {
        return columns;
    }

    private void _writeData(Cell cell, ColumnInterface.Type type) throws IOException {
        long tmp = f_tbl.file.getFilePointer();
        f_tbl.file.seek(cell.getFilePosition());
        switch (type) {
            case INTEGER:
                f_tbl.file.writeInt((Integer) cell.getValue());
                break;
            case DOUBLE:
                f_tbl.file.writeDouble((Double) cell.getValue());
                break;
            case STRING:
                f_tbl.file.writeChars((String) cell.getValue());
                break;
            case UNASSIGNED:
                break;
            case DELETED:
                switch (cell.getType()) {
                    case INTEGER:
                        f_tbl.file.writeInt(0);
                        break;
                    case DOUBLE:
                        f_tbl.file.writeDouble(0d);
                        break;
                    case STRING:
                        for (byte i = 0; i < cell.getSize(); i++) { // A character is unicode, thus 2 bytes are stored
                            f_tbl.file.writeShort(0); // A short it's two bytes wide
                        }
                        break;
                    case UNASSIGNED:
                        break;
                    case DELETED:
                        break;
                }
                break;
        }
        f_tbl.file.seek(tmp); // Restore file pointer
    }

    /**
     * Updates the UCID, keeping the original pointer position
     *
     * @throws IOException
     */
    private void _updateUCID() throws IOException {
        long tmp = f_conf.file.getFilePointer();
        f_conf.file.seek(0x0D);
        f_conf.file.writeByte(this.ucid);
        f_conf.file.seek(tmp);
    }

    private void _updateRegisterAmount() throws IOException {
        long tmp = f_conf.file.getFilePointer();
        f_conf.file.seek(0x04);
        f_conf.file.writeLong(this.registerAmount);
        f_conf.file.seek(tmp);
    }
}