package com.android.abhishekvats.parkingsystem

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_reserve.*
import android.os.VibrationEffect
import android.os.Build
import android.content.Context.VIBRATOR_SERVICE
import android.os.Vibrator



class Reserve : AppCompatActivity() {

    lateinit var layout:LinearLayout
    lateinit var buttonparams: LinearLayout.LayoutParams
    lateinit var layoutparams: LinearLayout.LayoutParams
    lateinit var listener:View.OnClickListener
    var buttonList=ArrayList<Button>()
    var array=arrayOf(0,1,1,1,0,0,0,1,0,1,0,1,0,1,0)
    var col=4
    var row=2
    var price=""
    var clicks=0
    fun onClick(view: View){

    }
    fun getCost(x:Int){
        cost.text="Price=Rs."+(price.toInt()*x).toString()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)

        listener=View.OnClickListener {
            if(it.tag==0) {
                it.setBackgroundResource(R.drawable.booked_spot)
                it.tag=1
                clicks++
                getCost(clicks)
            }
            else if(it.tag==1) {
                it.setBackgroundResource(R.drawable.empty_spot)
                it.tag=0
                clicks--
                getCost(clicks)
            }
        }
        supportActionBar!!.title="Pick Your Spot"
//        col=intent.getIntExtra("cols",0)
//        row=intent.getIntExtra("rows",0)
        price=intent.getStringExtra("price")

        for(i in 0..row-1){
            layout= LinearLayout(this)
            layout.orientation=LinearLayout.HORIZONTAL
            for(j in 0..col-1)
            {
                var startIndex=i*4
                var button=Button(this)
                buttonparams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                buttonparams.setMargins(5,5,5,5)
                buttonparams.weight=1f
                button.layoutParams = buttonparams
                if(array[startIndex+j]==0){
                    button.setBackgroundResource(R.drawable.empty_spot)
                    button.tag=0
                }
                else if(array[startIndex+j]==1){
                    button.setBackgroundResource(R.drawable.booked_spot)
                }
                buttonList.add(button)
                layout.addView(button)
            }
            layoutparams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutparams.setMargins(5,5,5,5)
            layout.layoutParams=layoutparams
            layoutparams.setMargins(150,0,150,0)
            layout.gravity=Gravity.CENTER
            linearLayout.addView(layout)
        }
        for(i in buttonList){
            i.setOnClickListener(listener)
        }
    }
}
