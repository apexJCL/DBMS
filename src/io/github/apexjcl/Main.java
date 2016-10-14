package io.github.apexjcl;

import io.github.apexjcl.entities.*;
import io.github.apexjcl.interfaces.ColumnInterface;
import io.github.apexjcl.objects.TableDescriptor;

public class Main {

    public static void main(String[] args) throws Exception {
        String home = "C:\\Users\\zero_\\";
        Database db = new Database(home, "testdb\\");
        TableDescriptor td = new TableDescriptor("test", new Column[]{
                new Column("id", ColumnInterface.Type.INTEGER, 0, 0),
                new Column("name", ColumnInterface.Type.STRING, 0, 1, (byte) 10),
                new Column("age", ColumnInterface.Type.INTEGER, 0, 3)
        });
        //db.createTable(td);
        Table test = db.getTableByName("test");
        Row registerByPosition = test.getRegisterByPosition(0);
//        Column[] columns = test.getColumns();
//        Row t = new Row();
//        t.setFilePosition(test.getNextRegisterPosition());
//        t.setCells(new Cell[]{
//                new Cell(columns[0], 0),
//                new Cell(columns[1], "Diana"),
//                new Cell(columns[2], 2)
//        });
//        test.insert(t);
        System.out.println("Yep");
    }
}
