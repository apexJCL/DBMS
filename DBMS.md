# Archivos


## \<nombre tabla>.desc : **Metadatos**

- **Número de columnas** - 1 byte
- **Columnas** (delimitados por `0x0D0A`):
    + **Nombre** - De 2 a 50 bytes
    + **UTID** (_unique table identifier_) - 4 bytes (int)
    + **UCID** (_unique column identifier_) - 4 bytes (int)
    + **Tipo** - 1 byte
    + ** Tamaño**  1 byte
    + **RTID** (_reference table identifier_) - 4 bytes (int)
    + **RCIF** (_reference column identifier_) - 4 bytes (int)
- **Tamaño del registro** - 4 bytes (int)

## \<nombre tabla>.conf : **Configuración**

- **UTID** (_unique table identifier_) - 4 bytes (int)
- **Registros Actuales** - 8 bytes (long)
- **Índices Definidos** - 1 byte
- **UCID** (_unique column identifier_) - 1 byte (Offset 0x0D)

## \<nombre tabla>\<nombre columna>.idx : **Índices*

- Índices (delimitados por 0x0D0A):
- Tipo - 1 byte
- Valor a indexar - depende del tipo
- Posición del registro - 8 bytes (long)
		
## \<nombre tabla>.tbl : Datos crudos

+ Registros

