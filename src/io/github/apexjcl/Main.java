package io.github.apexjcl;

import io.github.apexjcl.entities.Cell;
import io.github.apexjcl.entities.Database;
import io.github.apexjcl.entities.Row;
import io.github.apexjcl.entities.Table;

public class Main {

    public static void main(String[] args) throws Exception {
        String home = "/home/apex/";
        Database db = new Database(home, "testdb/");
//        TableDescriptor td = new TableDescriptor("test", new Column[]{
//                Column.newInteger("id", 0),
//                Column.newString("nombre", (byte) 20, 1),
//                Column.newInteger("edad", 2)
//        });
//        db.createTable(td);
        Table test = db.getTableByName("test");
//        Column[] columns = test.getColumns();
//        Row t = new Row();
//        t.setFilePosition(test.getNextRegisterPosition());
//        t.setCells(new Cell[]{
//                new Cell(columns[0], 2),
//                new Cell(columns[1], "Apolo"),
//                new Cell(columns[2], 2)
//        });
//        test.insert(t);
        Row registerByPosition = test.getRegisterByPosition(0);
        registerByPosition.fetchData()[1].setValue("Pancho Pancho");
        test.update(registerByPosition);
        Row[] rows = new Row[(int) test.registerAmount()];
        for (long l = 0; l < test.registerAmount(); l++) {
            rows[(int) l] = test.fetchRow(l);
        }
        for (Row r : rows) {
            for (Cell c : r.fetchData()) {
                System.out.print("Column Name: " + c.getColumnDefinition().getName() + ", Value: " + c.getValue());
                System.out.println();
            }
        }
        // TODO: Add indices, handle primary keys, fix update method, maybe filepointer is missing?
    }
}
