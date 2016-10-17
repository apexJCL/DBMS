# Archivos


### `<nombre tabla>.desc` : **Metadatos**

- **Número de columnas** - 1 byte
- **Columnas** (delimitados por `0x0D0A`):
    + **Tipo** - 1 byte
    + **Nombre**
        * Longitud nombre: 1 byte
        * Nombre: 0 a 255 caracteres
    + **UTID** (_unique table identifier_) - 4 bytes (int)
    + **UCID** (_unique column identifier_) - 4 bytes (int)
    + **Tamaño** - 1 byte
    + **Offset** - 1 byte
    + **RTID** (_reference table identifier_) - 4 bytes (int)
    + **RCIF** (_reference column identifier_) - 4 bytes (int)
- **Tamaño del registro** - 4 bytes (int)

### `<nombre tabla>.conf` : **Configuración**

- **UTID** (_unique table identifier_) - 4 bytes (int)
- **Registros Actuales** - 8 bytes (long)
- **Índices Definidos** - 1 byte
- **UCID** (_unique column identifier_) - 1 byte (Offset 0x0D)

### `<nombre tabla><nombre columna>.idx` : **Índices**

- Índices (delimitados por 0x0D0A):
- Tipo - 1 byte
- Valor a indexar - depende del tipo
- Posición del registro - 8 bytes (long)
		
## `<nombre tabla>.tbl` : **Datos crudos**

+ Registros

# Creación de la base de datos

Para crear una base de datos, sólo falta con hacer una instancia del objeto `Database`, de la siguiente manera:

```java
String home = "C:\\Users\\zero_\\";
Database db = new Database(home, "testdb\\");
```

En el momento en que se crea la instancia del objeto, así se ejecuta la creación de archivos pertinentes.

# Construyendo una tabla

Se puede hacer de la siguiente manera:

```java
TableDescriptor td = new TableDescriptor("test", new Column[]{
        Column.newInteger("id", 0),
        Column.newString("nombre", (byte) 20, 1),
        Column.newInteger("edad", 2)
});
```

Aquí se hace uso de la clase `TableDescriptor` para generar una nueva base para la tabla, la cual recibe dos parámetros: `nombre` y `columnas[]`.

Las columnas pueden ser definidas con anterioridad o en la misma declaración, para las columnas, existen los siguientes métodos en la clase `Column`:

```java
// Crea una columna del tipo entero
Column.newInteger(String name, int UCID);
Column.newInteger(String name, int UTID, int UCID);
// Crea una columna del tipo String
Column.newString(String name, byte size, int UCID);
Column.newString(String name, int UTID, int UCID, byte length);
// Crea una columna del tipo Double
Column.newDouble(String name, int UCID);
Column.newDouble(String name, int UTID, int UCID);
```

Se cuenta con dos variantes de los métodos, los que reciben y los que no reciben el `Unique Table IDentifier`, esto es para facilitar la creación de columnas nuevas, o la creación de los objetos al cargarlos por tabla (que es donde se aplica la versión que incluye el `UTID`).

---

Finalmente, para poder crear la tabla, se le pasa el objeto `TableDescriptor` a la instancia actual de `Database`:

```java
Database db = new Database(String filePath, String databaseName);
db.createTable(td);
```

# Inserción a una tabla

Para la inserción, primero se debe modelar un objeto del tipo `Row`, el cual describe una **tupla**, la cual puede contener **todas o algunas** de las columnas que componen la tabla.

Para esto, se puede crear el objeto y hacer uso de las instancias de las columnas como objetos `Column` para crear la definición de la tupla:

```java
Row t = new Row();
t.setFilePosition(test.getNextRegisterPosition());
t.setCells(new Cell[]{
        new Cell(columns[0], 0),
        new Cell(columns[1], "Diana Rivas"),
        new Cell(columns[2], -1)
});
```

Aquí el objeto `Cell` cuenta con un constructor específico:

```java
public Cell(Column columnReference, Object value);
```

Finalmente, una vez creada la tupla, se puede insertar de la siguiente manera: 
```java
Table test = db.getTableByName("test");
//--- Creación de la tupla
test.insert(t);
```

Este método del objeto `Table` se encarga de colocar, según el tipo de columna y tamaño, los datos de manera correcta.