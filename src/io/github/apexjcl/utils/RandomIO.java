package io.github.apexjcl.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by José Carlos López on 23/08/2016.
 */
public class RandomIO {

    private String filename;
    private FileMode mode;
    public RandomAccessFile file;
    private File _file;
    public boolean newFile;

    /**
     * Input/Output object to the designed file
     *
     * @param filename Path to the file, including filename
     * @param mode     File open mode (read, write, both)
     * @param create   If file does not exists, create it
     * @throws IOException File could not be found
     */
    public RandomIO(String filename, FileMode mode, Boolean create) throws IOException {
        this.filename = filename;
        this.mode = mode;
        newFile = create ? this._createFile() : create;
        switch (mode) {
            case R:
                this.file = new RandomAccessFile(filename, "r");
                break;
            case W:
                this.file = new RandomAccessFile(filename, "w");
                break;
            case RW:
                this.file = new RandomAccessFile(filename, "rw");
                break;
        }
        this._file = new File(this.filename);
    }

    /**
     * Creates a new file (if it does not exists)
     *
     * @return If the file was created
     * @throws IOException
     */
    private boolean _createFile() throws IOException {
        File f = new File(this.filename);
        return !f.exists() && f.createNewFile();
    }

    /**
     * Returns the size of the file
     *
     * @return Returns the size of the file
     */
    public long getSize() {
        return _file.length();
    }

    public void reset() throws IOException {
        this.file.seek(0);
    }

    /**
     * Returns if the pointer is at EOF
     *
     * @return
     * @throws IOException
     */
    public boolean isEOF() throws IOException {
        return file.getFilePointer() >= getSize();
    }

    public void dispose() throws IOException {
        this.file.close();
    }

    public void delete() throws Exception {
        this.file.close();
        this._file.delete();
    }

    public enum FileMode {
        R, W, RW
    }
}
