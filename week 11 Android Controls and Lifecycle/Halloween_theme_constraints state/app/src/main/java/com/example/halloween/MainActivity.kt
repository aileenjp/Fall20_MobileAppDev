package com.example.halloween

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var message: String = ""
    var imageId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sayBoo(view: View) {
        //EditText
        val name = editTextName.text

        //message
        message = "Happy Halloween $name !"

        //image
        imageId = R.drawable.ghost
        updateUI()
    }

    fun updateUI(){
        //TextView
        textMessage.text = message
        //ImageView
        imageId?.let { imageView.setImageResource(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("message", message)
        imageId?.let { outState.putInt("image", it) }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        message = savedInstanceState.getString("message", "")
        imageId = savedInstanceState.getInt("image")
        updateUI()
    }
}