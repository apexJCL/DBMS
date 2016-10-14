package io.github.apexjcl.entities;

import io.github.apexjcl.interfaces.DatabaseInterface;
import io.github.apexjcl.objects.TableDescriptor;
import io.github.apexjcl.utils.RandomIO;
import io.github.apexjcl.utils.StringConstants;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Database handles all files related to a config schema, tables, and so on.
 * <p>
 * The Database has a config.db file, that has the following structure:
 * <p>
 * <ul>
 * <li>Current tableID - 4 bytes</li>
 * </ul>
 * <p>
 * Created by José Carlos López on 12/10/2016.
 */
public class Database implements DatabaseInterface {

    private File workdir;

    private String workpath;
    private String dbname;

    private HashMap<String, Table> tableMap;
    private HashMap<Integer, Table> tableIDMap;

    private int tableID;
    RandomIO dbdata;

    /**
     * Creates or opens a new Database.
     *
     * @param path Path to the database, ex: C:\\Users\\user\\db\\
     * @param name Name of the database, this case is the folder containing other data, ex: myDB\
     * @throws Exception
     */
    public Database(String path, String name) throws Exception {
        dbname = name;
        workpath = path + name;
        workdir = new File(path, name);
        if (!workdir.exists()) { // If database does not exists, creates it
            workdir.mkdir();
            _setupDB();
        } else
            _loadDB();
    }

    private void _loadDB() throws Exception {
        dbdata = new RandomIO(this.workpath + StringConstants.DB_DATA_FILENAME, RandomIO.FileMode.RW, false);
        this.tableID = _readUniqueTableID();
        String[] table_files = workdir.list((dir, name) -> name.matches(".*\\.tbl"));
        tableMap = new HashMap<>(table_files.length);
        tableIDMap = new HashMap<>(table_files.length);
        for (byte i = 0; i < table_files.length; i++) {
            Table tmp = new Table(this.workpath, table_files[i], i);
            tableMap.put(tmp.getName(), tmp);
            tableIDMap.put(tmp.getTableID(), tmp); // TODO: fix this inefficient mess
        }
    }

    private void _setupDB() throws IOException {
        dbdata = new RandomIO(this.workpath + StringConstants.DB_DATA_FILENAME, RandomIO.FileMode.RW, true);
        // Write initial DB data
        dbdata.file.writeInt(0); // Current Unique Table Identifier
        dbdata.file.writeInt(0); // Current Amount of Tables
        tableIDMap = new HashMap<>(0);
        tableMap = new HashMap<>(0);
    }

    @Override
    public Table getTableByID(int id) throws Exception {
        if (!tableIDMap.containsKey(id))
            throw new Exception("Invalid Table ID");
        return tableIDMap.get(id);
    }

    @Override
    public Table getTableByName(String name) throws Exception {
        if (!tableMap.containsKey(name))
            throw new Exception("Table not found");
        return tableMap.get(name);
    }

    @Override
    public boolean createTable(TableDescriptor tableDescriptor) throws Exception {
        Table t = new Table(this.workpath, tableDescriptor.getName(), ++this.tableID, tableDescriptor.getColumns());
        this.tableID++; // Really updates the value
        _writeUniqueTableID(); // Writes it to persistent file storage
        tableMap.put(t.getName(), t);
        tableIDMap.put(t.getTableID(), t); // TODO: fix this inefficient mess
        // Now begin column management
        return true;
    }

    @Override
    public boolean dropTable(String tableName) throws Exception {
        Table t = getTableByName(tableName);
        return t.drop(this);
    }

    /**
     * This method will update the Unique Table ID counter in the file, based on the
     * actual number stored in memory.
     * <p>
     * This method it's meant to keep current dbdata file pointer at place
     */
    private void _writeUniqueTableID() throws IOException {
        long t = dbdata.file.getFilePointer();
        dbdata.file.seek(0);
        dbdata.file.writeInt(this.tableID);
        dbdata.file.seek(t);
    }

    /**
     * Reads current table ID from file.
     * <p>
     * This method it's meant to keep current dbdata file pointer at place.
     *
     * @return
     */
    private int _readUniqueTableID() throws IOException {
        long t = dbdata.file.getFilePointer();
        dbdata.file.seek(0);
        int tmp = dbdata.file.readInt();
        dbdata.file.seek(t);
        return tmp;
    }
}
