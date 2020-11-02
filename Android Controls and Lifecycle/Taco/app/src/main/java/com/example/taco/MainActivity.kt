package com.example.taco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun createTaco(view: View) {
        var filling : CharSequence = ""
        var toppinglist = "" //String

        //radio buttons
        val fillingId = radioGroup.checkedRadioButtonId

        if (fillingId == -1){
            //Snackbar
            val fillingSnackbar = Snackbar.make(root_layout, "Please select a filling", Snackbar.LENGTH_SHORT)
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
                toppinglist = "with" + toppinglist
            }

            //conditional expression
            //toppinglist = (if (toppinglist.isNotEmpty()) "with$toppinglist" else "").toString()

            //spinner
            val location = "at " + spinner.selectedItem

            //switch
            if (glutenSwitch.isChecked) {
                filling = glutenSwitch.text.toString() + " $filling"
            }

            //textview
            messageTextView.text = "You'd like $filling tacos $toppinglist $location"
        }
    }
}