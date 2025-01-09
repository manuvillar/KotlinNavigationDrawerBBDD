package es.iesoretania.navigationdrawerbbddkotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import es.iesoretania.navigationdrawerbbddkotlin.adaptador.Empleado

class AdminSQLiteOpenHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        const val TABLE_EMPLEADO = "empleado"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_APELLIDO = "apellido"
        const val COLUMN_SALARIO = "salario"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL("""
                CREATE TABLE $TABLE_EMPLEADO (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_NOMBRE TEXT NOT NULL,
                    $COLUMN_APELLIDO TEXT NOT NULL,
                    $COLUMN_SALARIO REAL
                );
            """)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS empleado")
        onCreate(db)
    }

    fun insertarEmpleado(empleado: Empleado): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COLUMN_NOMBRE, empleado.nombre)
                put(COLUMN_APELLIDO, empleado.apellidos)
                put(COLUMN_SALARIO, empleado.salario)
            }
            db.insert(TABLE_EMPLEADO, null, values) != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.close()
        }
    }

    fun obtenerEmpleados(): List<Empleado> {
        val empleados = mutableListOf<Empleado>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM empleado", null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    val empleado = Empleado(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3)
                    )
                    empleados.add(empleado)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
            db.close()
        }
        return empleados
    }

    fun borrarEmpleado(id: String): Boolean {
        val db = this.writableDatabase
        val rowsAffected = db.delete("empleado", "id = ?", arrayOf(id))
        return rowsAffected > 0
    }

}