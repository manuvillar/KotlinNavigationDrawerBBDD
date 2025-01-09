package es.iesoretania.navigationdrawerbbddkotlin.adaptador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import es.iesoretania.navigationdrawerbbddkotlin.R
import es.iesoretania.navigationdrawerbbddkotlin.databinding.EmpleadoItemBinding

class MiAdaptadorEmpleados(
    private val context: Context,
    val resource: Int,
    private val listaempleados: List<Empleado>
) : ArrayAdapter<Empleado>(context, resource, listaempleados) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Reutilizamos la vista si convertView no es null
        val binding: EmpleadoItemBinding
        val view: View

        if (convertView == null) {
            // Si convertView es null, inflamos el layout y creamos el binding
            binding = EmpleadoItemBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding // Guardamos el binding en el tag de la vista para reutilizarlo después
        } else {
            // Si convertView no es null, reutilizamos la vista y el binding
            binding = convertView.tag as EmpleadoItemBinding
            view = convertView
        }

        // Obtenemos el empleado en la posición actual
        val elementoActual = listaempleados[position]

        // Establecemos los valores en las vistas
        binding.textViewID.text = elementoActual.id.toString()
        binding.textViewNombre.text = elementoActual.nombre
        binding.textViewApellidos.text = elementoActual.apellidos
        binding.textViewSalario.text = elementoActual.salario.toString()

        // Puedes actualizar la imagen aquí si es necesario
        binding.imageView.setImageResource(R.drawable.avatar)

        return view
    }
}
