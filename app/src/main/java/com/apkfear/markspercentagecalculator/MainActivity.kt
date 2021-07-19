package com.apkfear.markspercentagecalculator


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.AudienceNetworkAds
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var obtain:EditText
    private lateinit var total:EditText
    private lateinit var calculate:Button
    private lateinit var clear:Button
    private var obtainMarks:String = ""
    private var totalMarks:String = ""
    private var percentage:Double = 1.5
    private lateinit var percentageShow:TextView
    lateinit var adView: AdView
    lateinit var adContainer: LinearLayout
    lateinit var copy:Button



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        obtain = findViewById(R.id.obtain)
        total = findViewById(R.id.total)
        calculate = findViewById(R.id.calculate)
        clear = findViewById(R.id.clear)
        adContainer = findViewById(R.id.banner_container)
        percentageShow = findViewById(R.id.percentage)
        copy = findViewById(R.id.btCopyText)

        AudienceNetworkAds.initialize(this)

        /* Make cursor blinkable */
        total.requestFocus()
        total.isCursorVisible = true

        obtain.requestFocus()
        obtain.isCursorVisible = true


        adView = AdView(
            this,
            "12345",
            AdSize.BANNER_HEIGHT_50
        )

        // Add the ad view to your activity layout
        adContainer.removeView(adView)
        adContainer.addView(adView)

        // Request an ad
        adView.loadAd()

        calculate.setOnClickListener{

            obtainMarks = obtain.text.toString()

            totalMarks = total.text.toString()

            if(obtainMarks.isBlank()){

                obtain.error = "Obtain marks is Missing"
            }

            if(totalMarks.isBlank()){

                total.error = "Total marks is Missing"

            }

            if(obtainMarks.isBlank() || totalMarks.isBlank()) {

                Toast.makeText(this,"Please fill Marks First",Toast.LENGTH_SHORT).show()

            }else{

            if(obtainMarks.toInt() > totalMarks.toInt()){

                val alterDialog= AlertDialog.Builder(this)

                alterDialog.setTitle("Alert!")
                alterDialog.setMessage("Total marks should be greater than obtained marks!!")
                alterDialog.setPositiveButton("Ok"){ _, _ ->

                }

                alterDialog.create()
                alterDialog.show()

            }else{

                val oM = obtainMarks.toDouble()

                val mul = oM * 100.0

                val tM = totalMarks.toDouble()

                percentage = mul / tM

                val per = String.format("%.2f",percentage)


                val form = DecimalFormat("0.##")

                if(per.toDouble() > 50.0){
                    percentageShow.setTextColor(Color.rgb(2,174,0))
                    percentageShow.text = "${form.format(per.toDoubleOrNull())}%"

                }else{

                    percentageShow.setTextColor(Color.rgb(255,1,0))
                    percentageShow.text = "${form.format(per.toDoubleOrNull())}%"
                }


            }

            }
            numberPad()

        }


        copy.setOnClickListener {

            val textToCopy = percentageShow.text
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Percentage copied successfully", Toast.LENGTH_LONG).show()

            }


        clear.setOnClickListener {

            obtain.text.clear()
            total.text.clear()
            percentageShow.text = ""

            obtain.requestFocus()
            obtain.isCursorVisible = true

        }


    }



   private fun numberPad()
    {

        val view: View? = this.currentFocus

        if (view != null){

            val im: InputMethodManager =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(view.windowToken,0)
        }

    }


}