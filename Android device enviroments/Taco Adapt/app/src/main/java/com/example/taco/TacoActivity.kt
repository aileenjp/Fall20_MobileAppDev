package com.example.taco

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_taco.*

class TacoActivity : AppCompatActivity() {
        private var tacoShopName : String? = null
        private var tacoShopUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taco)
        setSupportActionBar(findViewById(R.id.toolbar))

        tacoShopName = intent.getStringExtra("tacoShopName")
        tacoShopUrl = intent.getStringExtra("tacoShopURL")

        tacoShopName?.let { Log.i("shop received", it) };
        tacoShopUrl?.let { Log.i("url received", it) };

        tacoShopName?.let {tacoShopTextView.text = getString(R.string.tacoShopMessage) + " $tacoShopName"}

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            loadWebSite()
        }

        //using Kotlin extension plugin
//        fab.setOnClickListener {
//            loadWebSite()
//        }
    }

    private fun loadWebSite(){
        //create intent

        var intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = tacoShopUrl?.let{Uri.parse(tacoShopUrl)}

//        var intent = Intent().apply {
//            action = Intent.ACTION_VIEW;
//            data = tacoShopUrl?.let{Uri.parse(tacoShopUrl)};
//        }

//        val intent = Intent(Intent.ACTION_VIEW, tacoShopUrl?.let{Uri.parse(tacoShopUrl)})

        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val data = Intent()
        data.putExtra("feedback", feedbackEditText.text.toString())
        setResult(Activity.RESULT_OK, data) //must be set before super.onBackPressed()
        super.onBackPressed()
        finish()
    }
}