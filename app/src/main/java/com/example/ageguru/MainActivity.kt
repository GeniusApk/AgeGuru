 package com.example.ageguru

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.ageguru.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

 class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)} // this is for viewbinding , we can also use other way

     private var tvselceteddate : TextView? = null
     private var tvAgeInMint : TextView? = null
     private var tvAgeInHr: TextView? = null
     private var tvAgeInDay: TextView? = null
     private var tvAgeInYear: TextView? = null


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
         tvAgeInHr = binding.tvAgeInHr
         tvAgeInDay = binding.tvAgeInDay
         tvAgeInYear = binding.tvAgeInYear
        tvAgeInMint = binding.tvAgeInMint
         tvselceteddate = binding.tvselceteddate
        binding.btnDatePiker.setOnClickListener {
            clickDatePicker()
        }

    }
     private fun clickDatePicker(){
         val mycalender = Calendar.getInstance()
         val year = mycalender.get(Calendar.YEAR)
         val month = mycalender.get(Calendar.MONTH)
         val day = mycalender.get(Calendar.DAY_OF_MONTH)
         val dpd = DatePickerDialog(this , DatePickerDialog.OnDateSetListener{view , selectedYear , selectedMonth , selectedDayOfMonth ->

             //  Toast.makeText(this ,  "year was $selectedYear" , Toast.LENGTH_SHORT).show()
                //in next line ${selectedMonth+1} , i add 1 due to kotlin use to count number from 0123...  like this but our konth start wwith 1 for jan ,2 feb ....
             val selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"
             binding.tvselceteddate.text = selectedDate
             //sdf will change the data format
             val sdf = SimpleDateFormat("dd/MM/yyyy" , Locale.ENGLISH)
             val thedate = sdf.parse(selectedDate)
             val selectedDateInMin = thedate!!.time / 60000 //in the code i added !! for non-null
             val cruntDate = sdf.parse(sdf.format(System.currentTimeMillis())) // the code will give me current date
             val cruntDateInMin = cruntDate!!.time / 60000 // i got current data but i want in minunte so i add / 60000 sec
             val diffrenceInMin = cruntDateInMin - selectedDateInMin
             // Calculate age in hours, days, and years
             val ageInHr = diffrenceInMin / 60
             val ageInDay = diffrenceInMin / (60 * 24)
             val ageInYear = ageInDay / 365

             binding.tvAgeInMint.text = diffrenceInMin.toString()
             binding.tvAgeInHr.text = ageInHr.toString()
             binding.tvAgeInDay.text = ageInDay.toString()
             binding.tvAgeInYear.text = ageInYear.toString()


         },
             year,
             month,
             day

         )
         dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000   // the for user can not pick futur date , user can onle pick yester day date
         dpd.show()

     }

}