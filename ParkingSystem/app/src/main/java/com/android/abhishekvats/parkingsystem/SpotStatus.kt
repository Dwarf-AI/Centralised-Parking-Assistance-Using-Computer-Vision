package com.android.abhishekvats.parkingsystem

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import kotlinx.android.synthetic.main.activity_spot_status.*

class SpotStatus : AppCompatActivity() {

    var cols=0
    var rows=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_status)

        var intent=intent
        var array=intent.getIntArrayExtra("array")
        var lat=intent.getStringExtra("lat")
        var long=intent.getStringExtra("long")

        title=intent.getStringExtra("name")
        distance.text=intent.getStringExtra("dist")
        vacant.text=intent.getStringExtra("slots")
//        Log.i("vacant",intent.getStringExtra("spots"))
        contactno.text=intent.getStringExtra("contact")
        price.text=intent.getStringExtra("price")
        cols=intent.getStringExtra("cols").toInt()
        Log.i("col",cols.toString())
        rows=intent.getStringExtra("rows").toInt()
        Log.i("row",rows.toString())

        reserve.setOnClickListener {
            var intent=Intent(this,Reserve::class.java)
            intent.putExtra("lat",lat)
            intent.putExtra("long",long)
            intent.putExtra("rows",rows)
            intent.putExtra("cols",cols)
            intent.putExtra("price",price.text.toString())
            intent.putExtra("array",array)
            startActivity(intent)
        }
        cancel.setOnClickListener {
            super.onBackPressed()
        }
    }
}
