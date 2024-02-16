 package com.geniusapk.ageguru

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.geniusapk.ageguru.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

 class MainActivity : AppCompatActivity() {
     private val binding: ActivityMainBinding by lazy {
         ActivityMainBinding.inflate(layoutInflater)
     } // this is for viewbinding , we can also use other way
     private var appOpenAd: AppOpenAd? = null
     private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null

     private var tvselceteddate: TextView? = null
     private var tvAgeInMint: TextView? = null
     private var tvAgeInHr: TextView? = null
     private var tvAgeInDay: TextView? = null
     private var tvAgeInYear: TextView? = null

     @SuppressLint("SuspiciousIndentation")
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


         MobileAds.initialize(this) {} // this for Admob
         val adRequest = AdRequest.Builder().build()  // this is for reload the ad
         binding.adView.loadAd(adRequest)
         binding.adView.adListener = object : AdListener() {
             override fun onAdClicked() {
                 // Code to be executed when the user clicks on an ad.
             }

             override fun onAdClosed() {
                 // Code to be executed when the user is about to return
                 // to the app after tapping on an ad.
             }

             override fun onAdFailedToLoad(adError: LoadAdError) {
                 // Code to be executed when an ad request fails.
                 super.onAdFailedToLoad(adError);
                 binding.adView.loadAd(adRequest);
             }

             override fun onAdImpression() {
                 // Code to be executed when an impression is recorded
                 // for an ad.
             }

             override fun onAdLoaded() {
                 // Code to be executed when an ad finishes loading.
                 super.onAdLoaded();
             }

             override fun onAdOpened() {
                 // Code to be executed when an ad opens an overlay that
                 // covers the screen.
                 super.onAdOpened();
             }
         }
         loadAppOpenAd()


     }

     private fun clickDatePicker() {
         val mycalender = Calendar.getInstance()
         val year = mycalender.get(Calendar.YEAR)
         val month = mycalender.get(Calendar.MONTH)
         val day = mycalender.get(Calendar.DAY_OF_MONTH)
         val dpd = DatePickerDialog(
             this,
             DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDayOfMonth ->

                 //  Toast.makeText(this ,  "year was $selectedYear" , Toast.LENGTH_SHORT).show()
                 //in next line ${selectedMonth+1} , i add 1 due to kotlin use to count number from 0123...  like this but our konth start wwith 1 for jan ,2 feb ....
                 val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                 binding.tvselceteddate.text = selectedDate
                 //sdf will change the data format
                 val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                 val thedate = sdf.parse(selectedDate)
                 val selectedDateInMin =
                     thedate!!.time / 60000 //in the code i added !! for non-null
                 val cruntDate =
                     sdf.parse(sdf.format(System.currentTimeMillis())) // the code will give me current date
                 val cruntDateInMin =
                     cruntDate!!.time / 60000 // i got current data but i want in minunte so i add / 60000 sec
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
         dpd.datePicker.maxDate =
             System.currentTimeMillis() - 86400000   // the for user can not pick futur date , user can onle pick yester day date
         dpd.show()

     }

     private fun loadAppOpenAd() {
         AppOpenAd.load(
             this,
             getString(R.string.admob_app_open_ad_unit_id),
             AdRequest.Builder().build(),
             AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, // or APP_OPEN_AD_ORIENTATION_LANDSCAPE
             object : AppOpenAd.AppOpenAdLoadCallback() {
                 override fun onAdLoaded(ad: AppOpenAd) {
                     appOpenAd = ad
                 }

                 override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                     // Handle ad loading failure
                 }
             }
         )
     }

     override fun onResume() {
         super.onResume()
         if (appOpenAd != null) {
             val fullScreenContentCallback = object : FullScreenContentCallback() {
                 override fun onAdDismissedFullScreenContent() {
                     // Ad dismissed - load the next ad
                     loadAppOpenAd()
                 }


                 override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                     // Ad failed to show - load the next ad
                     loadAppOpenAd()
                 }
             }
             appOpenAd?.show(this)
         } else {
             loadAppOpenAd()
         }
     }









 }