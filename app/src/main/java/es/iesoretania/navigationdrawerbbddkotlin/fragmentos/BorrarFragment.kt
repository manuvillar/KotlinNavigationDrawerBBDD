package es.iesoretania.navigationdrawerbbddkotlin.fragmentos

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import es.iesoretania.navigationdrawerbbddkotlin.AdminSQLiteOpenHelper
import es.iesoretania.navigationdrawerbbddkotlin.R
import es.iesoretania.navigationdrawerbbddkotlin.databinding.FragmentBorrarBinding

class BorrarFragment : Fragment() {
    private lateinit var binding: FragmentBorrarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBorrarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Borrar Empleado"

        binding.BotonAceptarBorrar.setOnClickListener {
            val idEmpleado = binding.edIDBorrar.text.toString().trim()
            if (idEmpleado.isEmpty()) {
                binding.edIDBorrar.error = "Por favor, introduce un ID válido"
            } else {
                mostrarConfirmacionBorrado(idEmpleado)
            }
        }

        binding.BotonCancelarBorrar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun mostrarConfirmacionBorrado(id: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmar Borrado")
        builder.setMessage("¿Estás seguro de que deseas borrar al empleado con ID: $id?")
        builder.setPositiveButton("Sí") { _, _ ->
            borrarEmpleado(id)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun borrarEmpleado(id: String) {
        val dbHelper = AdminSQLiteOpenHelper(requireContext(), "empleados", null, 1)
        val success = dbHelper.borrarEmpleado(id)

        if (success) {
            Toast.makeText(context, "Empleado eliminado con éxito", Toast.LENGTH_SHORT).show()
            binding.edIDBorrar.setText("")
        } else {
            Toast.makeText(context, "No se encontró un empleado con ese ID", Toast.LENGTH_SHORT).show()
        }
    }
}