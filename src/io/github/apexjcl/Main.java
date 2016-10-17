package io.github.apexjcl;

import io.github.apexjcl.entities.Database;
import io.github.apexjcl.entities.Row;
import io.github.apexjcl.entities.Table;

public class Main {

    public static void main(String[] args) throws Exception {
        String home = "C:\\Users\\zero_\\";
        Database db = new Database(home, "testdb\\");
//        TableDescriptor td = new TableDescriptor("test", new Column[]{
//                Column.newInteger("id", 0),
//                Column.newString("nombre", (byte) 20, 1),
//                Column.newInteger("edad", 2)
//        });
//        db.createTable(td);
        Table test = db.getTableByName("test");
//        Row registerByPosition = test.getRegisterByPosition(0);
//        Column[] columns = test.getColumns();
//        Row t = new Row();
//        t.setFilePosition(test.getNextRegisterPosition());
//        t.setCells(new Cell[]{
//                new Cell(columns[0], 0),
//                new Cell(columns[1], "Diana Rivas"),
//                new Cell(columns[2], -1)
//        });
//        test.insert(t);
        Row[] rows = new Row[(int) test.registerAmount()];
        for (long l = 0; l < test.registerAmount(); l++) {
            rows[(int) l] = test.fetchRow(l);
        }
        System.out.println("Yep");
    }
}
