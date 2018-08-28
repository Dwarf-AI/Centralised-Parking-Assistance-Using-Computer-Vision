package com.android.abhishekvats.parkingsystem

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import kotlinx.android.synthetic.main.activity_spot_status.*

class SpotStatus : AppCompatActivity() {

    var cols=0
    var rows=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_status)

        title=intent.getStringExtra("name")
        distance.text=intent.getStringExtra("dist")
        vacant.text=intent.getStringExtra("spots")
        price.text=intent.getStringExtra("price")
//        cols=intent.getIntExtra("cols",0)
//        rows=intent.getIntExtra("rows",0)

        reserve.setOnClickListener {
            var intent=Intent(this,Reserve::class.java)
//            intent.putExtra("rows",rows)
//            intent.putExtra("cols",cols)
            intent.putExtra("price",price.text.toString())
            startActivity(intent)
        }
        cancel.setOnClickListener {
            super.onBackPressed()
        }
    }
}
