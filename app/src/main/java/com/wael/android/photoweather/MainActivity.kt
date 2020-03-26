package com.wael.android.photoweather

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val login = com.wael.android.photoweather.home.fragments.InitFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val shareIntent = Intent(Intent.ACTION_SEND)

/*
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, login)
        fragmentTransaction.commit()
*/
    }

}