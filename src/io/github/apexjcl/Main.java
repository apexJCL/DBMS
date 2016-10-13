package io.github.apexjcl;

import io.github.apexjcl.entities.Column;
import io.github.apexjcl.entities.Database;
import io.github.apexjcl.interfaces.ColumnInterface;
import io.github.apexjcl.objects.TableDescriptor;

public class Main {

    public static void main(String[] args) throws Exception {
        String home = "C:\\Users\\zero_\\";
        Database db = new Database(home, "testdb\\");
        TableDescriptor td = new TableDescriptor("test", new Column[]{
                new Column("id", ColumnInterface.Type.INTEGER, 0, 0),
                new Column("name", ColumnInterface.Type.STRING, 0, 1),
                new Column("age", ColumnInterface.Type.INTEGER, 0, 3)
        });
        db.createTable(td);
    }
}
