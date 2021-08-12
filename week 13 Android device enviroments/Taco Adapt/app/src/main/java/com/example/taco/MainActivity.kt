package com.example.taco

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_taco.*

class MainActivity : AppCompatActivity() {
    private var myTacoShop = TacoShop();
    private var selectedLocationPosition = 0
    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //event listener
        locationButton.setOnClickListener {
            selectedLocationPosition = spinner.selectedItemPosition
            myTacoShop.suggestTacoShop(selectedLocationPosition)
            Log.i("shop suggested", myTacoShop.name);
            Log.i("url suggested", myTacoShop.url);

            //create intent
            val intent = Intent(this, TacoActivity::class.java)
            intent.putExtra("tacoShopName", myTacoShop.name)
            intent.putExtra("tacoShopURL", myTacoShop.url)

//            val intent = Intent(this, TacoActivity::class.java).apply{
//                putExtra("tacoShopName", myTacoShop.name);
//                putExtra("tacoShopURL", myTacoShop.url);
//            }
            //startActivity(intent)

            //starting an intent that will pass data back
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    fun createTaco(view: View) {
        var filling : CharSequence = ""
        var toppinglist = "" //String

        //radio buttons
        val fillingId = radioGroup.checkedRadioButtonId

        if (fillingId == -1){
            //Snackbar
            val fillingSnackbar = Snackbar.make(root_layout, getString(R.string.snackbarFillingMessage), Snackbar.LENGTH_SHORT)
            fillingSnackbar.show()
            //Snackbar with chaining
            //Snackbar.make(root_layout, "Please select a filling", Snackbar.LENGTH_SHORT).show()
        } else {
            filling = findViewById<RadioButton>(fillingId).text

            //checkboxes
            if (checkBox1.isChecked) {
                toppinglist += " " + checkBox1.text
            }
            if (checkBox2.isChecked) {
                toppinglist += " " + checkBox2.text
            }
            if (checkBox3.isChecked) {
                toppinglist += " " + checkBox3.text
            }
            if (checkBox4.isChecked) {
                toppinglist += " " + checkBox4.text
            }
            if (toppinglist.isNotEmpty()) {
                toppinglist = getString(R.string.withFilling) + toppinglist
            }

            //conditional expression
            //toppinglist = (if (toppinglist.isNotEmpty()) "with$toppinglist" else "").toString()

            //spinner
            val location = getString(R.string.atLocation) + " " + spinner.selectedItem

            //switch
            if (glutenSwitch.isChecked) {
                filling = glutenSwitch.text.toString() + " $filling"
            }

            //textview
            messageTextView.text = getString(R.string.message) + "$filling tacos $toppinglist $location"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode == REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
          reviewTextView.setText(data?.let{data.getStringExtra("feedback")})
        }
    }
}