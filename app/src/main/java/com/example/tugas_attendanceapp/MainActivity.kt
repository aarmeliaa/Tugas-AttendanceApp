package com.example.tugas_attendanceapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tugas_attendanceapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var jenisPresensi: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        jenisPresensi = resources.getStringArray(R.array.jenisPresensi)
        with(binding){

            datePicker.init(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth
            ) { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                Toast.makeText(this@MainActivity, selectedDate, Toast.LENGTH_SHORT).show()
            }

            timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                Toast.makeText(this@MainActivity, selectedTime, Toast.LENGTH_SHORT).show()
            }

            val adapterPresensi = ArrayAdapter(this@MainActivity,
                android.R.layout.simple_spinner_item, jenisPresensi)
            adapterPresensi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPresensi.adapter = adapterPresensi
            spinnerPresensi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()

                    if (selectedItem == "Sakit" || selectedItem == "Terlambat" || selectedItem == "Izin") {
                        edittextKet.visibility = View.VISIBLE
                    } else {
                        edittextKet.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            btnSubmit.setOnClickListener {
                val day = datePicker.dayOfMonth
                val month = datePicker.month + 1
                val year = datePicker.year
                val tanggal = "$day/$month/$year"

                val hour = timePicker.hour
                val minute = timePicker.minute
                val waktu = String.format("%02d:%02d", hour, minute)

                val pilihanPresensi = spinnerPresensi.selectedItem.toString()

                var keterangan = ""
                if (edittextKet.visibility == View.VISIBLE) {
                    keterangan = edittextKet.text.toString()
                }

                val message = "Presensi: $pilihanPresensi\nTanggal: $tanggal\nWaktu: $waktu\nKeterangan: $keterangan"

                val toastView = layoutInflater.inflate(R.layout.custom_toast, null)
                val toastText: TextView = toastView.findViewById(R.id.toast_text)
                toastText.text = message

                val customToast = Toast(applicationContext)
                customToast.duration = Toast.LENGTH_LONG
                customToast.view = toastView
                customToast.show()
            }

            btnKeluar.setOnClickListener {
                val dialog = DialogExit()
                dialog.show(supportFragmentManager, "dialogExit")
            }
        }
    }
}