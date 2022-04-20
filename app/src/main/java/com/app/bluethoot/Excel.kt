package com.app.bluethoot

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_excel.*
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private val nombre= arrayListOf<String>()
private val registrof = arrayListOf<String>()
private val phonef= arrayListOf<String>()
private val cedulaf = arrayListOf<String>()
private val curso = arrayListOf<String>()
private val temp = arrayListOf<String>()

private val nombre2 = arrayListOf<String>()
private val registrof2 = arrayListOf<String>()
private val phonef2 = arrayListOf<String>()
private val cedulaf2 = arrayListOf<String>()
private val curso2 = arrayListOf<String>()
private val temp2 = arrayListOf<String>()

var T=0
var C=1
var T1 = 0
class Excel : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    private var etTexto: EditText? = null
    private var btnGenerar: Button? = null
    val error: String = "Por favor escoge una fecha*"
    var cont = 0
    var comp: String = ""

    //-----------------------------------------------------------------------------------------
    private val buscar1 = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            if (C == 1) {
                nombre.clear()
                phonef.clear()
                cedulaf.clear()
                registrof.clear()
                temp.clear()
                curso.clear()
            } else {
                nombre2.clear()
                phonef2.clear()
                cedulaf2.clear()
                registrof2.clear()
                temp2.clear()
                curso2.clear()
            }

            if (snapshot.exists()) {
                for (postSnapshot in snapshot.children) {

                    if (edit_fecha.text.isNotEmpty() && edit_fecha2.text.isNotEmpty()) {

                        val mensaje: String? = postSnapshot.key
                        cont = 0
                        comp = ""

                        for (num in mensaje!!.indices) {
                            if (mensaje[num] == '-') {
                                cont += 1

                            }

                            if (cont != 3) {
                                if (cont == 1 && mensaje[num + 1] == '0') {
                                    comp += mensaje[num]
                                    comp += mensaje[num + 2]
                                } else if (cont < 1 || cont > 1) {
                                    comp += mensaje[num]
                                }
                            } else if (cont == 3) {
                                println(comp)
                                if (edit_fecha.text != edit_fecha2.text) {

                                    if (comp in edit_fecha.text.toString()..edit_fecha2.text.toString()) {

                                        val datos = postSnapshot.getValue<Post>() //Toma los datos de firebase en el nodo seleccioado

                                        Log.w("Funciona: ", " ${postSnapshot.key}")

                                        if (datos != null) {
                                            if (C == 1) {
                                                nombre.add(datos.Nombre)
                                                phonef.add(datos.Celular)
                                                cedulaf.add(datos.Cedula)
                                                registrof.add(datos.Registro)
                                                curso.add(datos.Curso)
                                                temp.add(datos.Temperatura)

                                            } else {
                                                nombre2.add(datos.Nombre)
                                                phonef2.add(datos.Celular)
                                                cedulaf2.add(datos.Cedula)
                                                registrof2.add(datos.Registro)
                                                curso2.add(datos.Curso)
                                                temp2.add(datos.Temperatura)
                                            }

                                        }

                                    }
                                } else if (edit_fecha.text == edit_fecha2.text) {
                                    if (comp == edit_fecha.text.toString()) {
                                        val datos = postSnapshot.getValue<Post>() //Toma los datos de firebase en el nodo seleccioado

                                        Log.w("Funciona: ", " ${postSnapshot.key}")

                                        if (datos != null) {
                                            if (C == 1) {
                                                nombre.add(datos.Nombre)
                                                phonef.add(datos.Celular)
                                                cedulaf.add(datos.Cedula)
                                                registrof.add(datos.Registro)
                                                curso.add(datos.Curso)
                                                temp.add(datos.Temperatura)
                                            } else {
                                                nombre2.add(datos.Nombre)
                                                phonef2.add(datos.Celular)
                                                cedulaf2.add(datos.Cedula)
                                                registrof2.add(datos.Registro)
                                                curso2.add(datos.Curso)
                                                temp2.add(datos.Temperatura)
                                            }

                                        }
                                    }
                                }

                                cont = 0
                                comp = ""

                            }

                        }

                    } else if (edit_fecha == null) {
                        Log.d("Error", "Los campos estan vacios")
                    }
                }//Fin del for

            }
            Log.w("Registro s: ", " $registrof2")
            rv_rangoE.adapter = MyCustomAdapter(this@Excel)
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    //-----------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel)

        database = Firebase.database.reference
        //var refBuscador = database.child("Aforo").child("Registro-Entrada")
        val txRactivity = findViewById<TextView>(R.id.txRactivity)

        //boton generar excel
        btnGuardarExcel.setOnClickListener {
            guardar()
        }


        //Botones para el rango de fecha
        edit_fecha.setOnClickListener {
            showDatePickerDialog()
        }

        edit_fecha2.setOnClickListener {
            showDatePickerDialog2()
        }

        btnBuscar.setOnClickListener {

            var textMostrar1 = edit_fecha.text.toString()
            var textMostrar2 = edit_fecha2.text.toString()

            if (textMostrar2 == "" && textMostrar1 == "") {
                er1.text = error
                er2.text = error

            } else if (textMostrar2 == "" && textMostrar1 != "") {
                er2.text = error
                er1.text = ""

            } else if (textMostrar1 == "" && textMostrar2 != "") {
                er1.text = error
                er2.text = ""

            } else {
                er1.text = ""
                er2.text = ""
                C = 1
                txRactivity.text = "Entrada"
                initView()
            }


        }

        btnBuscar2.setOnClickListener {

            var textMostrar1 = edit_fecha.text.toString()
            var textMostrar2 = edit_fecha2.text.toString()

            if (textMostrar2 == "" && textMostrar1 == "") {
                er1.text = error
                er2.text = error

            } else if (textMostrar2 == "" && textMostrar1 != "") {
                er2.text = error
                er1.text = ""

            } else if (textMostrar1 == "" && textMostrar2 != "") {
                er1.text = error
                er2.text = ""

            } else {
                er1.text = ""
                er2.text = ""
                //refBuscador = database.child("Aforo").child("Registro-Salida")
                C = 2
                txRactivity.text = "Salida"
                initView2()
            }

        }

        imv_Volver.setOnClickListener {
            database.child("Aforo").child("Registro-Entrada").removeEventListener(buscar1)
            database.child("Aforo").child("Registro-Salida").removeEventListener(buscar1)
            val intent12 = Intent(this, Activity_maestro::class.java)
            startActivity(intent12)
            finish()
        }
    }


    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
        //fragmentManager?.let { datePicker.show(it, "datePicker") }
    }

    private fun showDatePickerDialog2() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected2(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
        //fragmentManager?.let { datePicker.show(it,"datePicker") }
    }

    @SuppressLint("SetTextI18n")
    fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes: Int = month + 1
        edit_fecha.setText("$year-$mes-$day")
        //datei = "$day-$month-$year"
    }

    @SuppressLint("SetTextI18n")
    fun onDateSelected2(day: Int, month: Int, year: Int) {
        val mes: Int = month + 1
        edit_fecha2.setText("$year-$mes-$day")
        //datef = "$day-$month-$year"
    }


    private fun initView() {

        rv_rangoE.adapter = MyCustomAdapter(this)//función principal
        database.child("Aforo").child("Registro-Entrada").addValueEventListener(buscar1)

    }//fin del initView

    //Segundo initView
    private fun initView2() {

        rv_rangoE.adapter = MyCustomAdapter(this)//función principal
        database.child("Aforo").child("Registro-Salida").addValueEventListener(buscar1)

    }//fin del initView


    //Funcion del adaptador listview

    private class MyCustomAdapter(context: Context) : BaseAdapter() {

        private val mContext: Context = context

        //Recuentos
        override fun getCount(): Int {

            return if (C == 1) {
                nombre.size
            } else {
                nombre2.size
            }
        }

        override fun getItemId(position: Int): Long {

            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return "TEXT STRING"
        }

        //responsable del renderizado de cada fila
        @SuppressLint("ViewHolder", "CutPasteId")
        override fun getView(position: Int, convertView: View?, ViewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val item_enter = layoutInflater.inflate(R.layout.item_enter, ViewGroup, false)

            val nameTextView = item_enter.findViewById<TextView>(R.id.tv_Nombre)
            val phoneTextView = item_enter.findViewById<TextView>(R.id.tv_phone)
            val cedulaTextView = item_enter.findViewById<TextView>(R.id.tv_cedula)
            val registroTextView = item_enter.findViewById<TextView>(R.id.tv_registro)
            val cursoTextView = item_enter.findViewById<TextView>(R.id.tv_Curso)
            val txTemp = item_enter.findViewById<TextView>(R.id.txTemp)

            if (C == 1) {

                nameTextView.text = nombre[position]
                phoneTextView.text = phonef[position]
                cedulaTextView.text = cedulaf[position]
                registroTextView.text = registrof[position]
                cursoTextView.text = curso[position]
                txTemp.text = temp[position]

            } else {
                nameTextView.text = nombre2[position]
                phoneTextView.text = phonef2[position]
                cedulaTextView.text = cedulaf2[position]
                registroTextView.text = registrof2[position]
                cursoTextView.text = curso2[position]
                txTemp.text = temp2[position]
            }

            return item_enter

        }
    }
    //generar excel
    private fun guardar() {

        val wb: Workbook = HSSFWorkbook()
        var cell: Cell? = null
        val cellStyle = wb.createCellStyle()
        cellStyle.fillForegroundColor = HSSFColor.LIGHT_BLUE.index
        cellStyle.fillPattern = HSSFCellStyle.SOLID_FOREGROUND

        var sheet: Sheet? = null
        sheet = wb.createSheet("Registro entrada y salida")

        var row: Row? = null
        //Para crear excel usuario de entrada
        row = sheet.createRow(0)
        cell = row.createCell(0)
        cell.setCellValue("Registro de entrada")
        cell = row.createCell(6)
        cell.setCellValue("Registro de Salida")
        //Fila 0 copumna 0
        row = sheet.createRow(1)
        cell = row.createCell(0)
        cell.setCellValue("Usuario")

        //Fila 0 copumna 1
        cell = row.createCell(1)
        cell.setCellValue("Curso")

        //Fila 0 copumna 2

        cell = row.createCell(2)
        cell.setCellValue("Fecha")

        //Fila 0 copumna 3

        cell = row.createCell(3)
        cell.setCellValue("Temperatura")

        //Fila 0 copumna 0
        cell = row.createCell(6)
        cell.setCellValue("Usuario")

        //Fila 0 copumna 1
        cell = row.createCell(7)
        cell.setCellValue("Curso")

        //Fila 0 copumna 2

        cell = row.createCell(8)
        cell.setCellValue("Fecha")

        //Fila 0 copumna 3

        cell = row.createCell(9)
        cell.setCellValue("Temperatura")


        T = nombre.size
        T1 = nombre2.size

        for (i in 0..T-1) {

            row = sheet.createRow(i+2)
            cell = row.createCell(0)
            cell.setCellValue(nombre[i])

            cell = row.createCell(1)
            cell.setCellValue(curso[i])

            cell = row.createCell(2)
            cell.setCellValue(registrof[i])

            cell = row.createCell(3)
            cell.setCellValue(temp[i])
        }
        //para crear excel de salida

        for (i in 0..T1-1) {

            row = sheet.createRow(i+2)
            cell = row.createCell(6)
            cell.setCellValue(nombre2[i])

            cell = row.createCell(7)
            cell.setCellValue(curso2[i])

            cell = row.createCell(8)
            cell.setCellValue(registrof2[i])

            cell = row.createCell(9)
            cell.setCellValue(temp2[i])
        }

        val file = File(getExternalFilesDir(null), "Registro de usuarios.xls")
        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(file)
            wb.write(outputStream)
            Toast.makeText(applicationContext, "Se guardó correctamente", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "NO SE GUARDO", Toast.LENGTH_LONG).show()
            try {
                outputStream!!.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

}