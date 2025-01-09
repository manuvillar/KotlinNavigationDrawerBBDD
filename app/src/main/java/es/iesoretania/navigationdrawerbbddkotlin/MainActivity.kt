package es.iesoretania.navigationdrawerbbddkotlin

import android.content.ContentValues
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import es.iesoretania.navigationdrawerbbddkotlin.adaptador.Empleado
import es.iesoretania.navigationdrawerbbddkotlin.databinding.ActivityMainBinding
import es.iesoretania.navigationdrawerbbddkotlin.databinding.NavHeaderMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val bindingHeader = NavHeaderMainBinding.bind(headerView)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            dialogoInsertar(view)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.contenedorPrincipal)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.inicioFragment, R.id.insertarFragment, R.id.borrarFragment, R.id.listarFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.title = "Inicio"
        bindingHeader.imageView.setImageResource(R.drawable.empresa)
        bindingHeader.TextView.text = "Empresa"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Acerca de...")
                builder.setMessage("Aplicación SQLite con Kotlin")
                builder.setNegativeButton("Aceptar") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                builder.show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.contenedorPrincipal)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun dialogoInsertar (view: View){
        val builder = AlertDialog.Builder(view.context)
        val inflater = layoutInflater
        builder.setTitle("Datos del nuevo empleado")

        val dialogLayout = inflater.inflate(R.layout.layout_insertar, null)
        val edNombreEmpleado = dialogLayout.findViewById<EditText>(R.id.edNombreEmpleado)
        val edApellidosEmpleado = dialogLayout.findViewById<EditText>(R.id.edApellidosEmpleado)
        val edSalarioEmpleado = dialogLayout.findViewById<EditText>(R.id.edSalarioEmepleado)

        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar") { _, _ ->
            val nombre = edNombreEmpleado.text.toString().trim()
            val apellidos = edApellidosEmpleado.text.toString().trim()
            val salarioTexto = edSalarioEmpleado.text.toString().trim()

            if (nombre.isEmpty() || apellidos.isEmpty() || salarioTexto.isEmpty()) {
                AlertDialog.Builder(view.context)
                    .setTitle("Error")
                    .setMessage("Todos los campos son obligatorios.")
                    .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                val salario = salarioTexto.toDoubleOrNull()
                if (salario == null || salario < 0) {
                    AlertDialog.Builder(view.context)
                        .setTitle("Error")
                        .setMessage("El salario debe ser un número positivo.")
                        .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
                        .show()
                } else {
                    insertar(view, nombre, apellidos, salario)
                }
            }
        }
        builder.setNegativeButton("Cancelar") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        builder.show()
    }

    private fun insertar(view: View, nombre: String, apellidos: String, salario: Double) {
        val dbHelper = AdminSQLiteOpenHelper(view.context, "empleados", null, 1)
        val empleado = Empleado(nombre = nombre, apellidos = apellidos, salario = salario)
        val resultado = dbHelper.insertarEmpleado(empleado)
        if (resultado) {
            Snackbar.make(view, "Empleado insertado correctamente", Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(view, "Error al insertar empleado", Snackbar.LENGTH_LONG).show()
        }
    }

}