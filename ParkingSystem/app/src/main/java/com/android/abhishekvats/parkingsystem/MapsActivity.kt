package com.android.abhishekvats.parkingsystem

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import org.json.JSONException
import java.lang.Math.round


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
                                         ,LocationListener {
    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(p0: Location?) {
    }


    private lateinit var mMap: GoogleMap
    private lateinit var client: GoogleApiClient
    private lateinit var que: RequestQueue
    private lateinit var jsonArrayRequest: JsonArrayRequest
    lateinit var cu:CameraUpdate
    var builder = LatLngBounds.Builder()
    lateinit var bounds:LatLngBounds
    var url = "http://50666bd0.ngrok.io/?lat=28.334434&lon=77.317066"
    var spotList= hashMapOf<String,ParkingSpot>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onResume() {
        super.onResume()
//        que.add(jsonArrayRequest)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.custom_map))
        var markerOptions = MarkerOptions()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient()
            mMap.isMyLocationEnabled = true
        }
        mMap.uiSettings.isMapToolbarEnabled = false
//        mMap.setOnMarkerClickListener {
//            it.showInfoWindow()
//            true
//
//        }
        mMap.setOnInfoWindowClickListener {


            var results = FloatArray(10)
            var intent = Intent(this, SpotStatus::class.java)
            try {
                Location.distanceBetween(mMap.myLocation.latitude, mMap.myLocation.longitude, it.position.latitude, it.position.longitude, results)
            } catch (Excep: Exception) {
                Toast.makeText(this, "Turn On Location Services", Toast.LENGTH_LONG)
            }
            var id=it.tag.toString()

            intent.putExtra("lat",spotList[id]!!.latitude)
            intent.putExtra("long",spotList[id]!!.longitude)

            intent.putExtra("dist", round((results[0] / 1000)).toString() + " km")
            intent.putExtra("name", spotList[id]!!.name)
//            Log.i("test",spotList[id]!!.name)
            intent.putExtra("slots", spotList[id]!!.slotsAvail)
//            Log.i("slot", spotList[id]!!.slotsAvail)
            intent.putExtra("price", spotList[id]!!.price)
            intent.putExtra("contact", spotList[id]!!.contact)
            intent.putExtra("rows",spotList[id]!!.rows)
//            Log.i("rows",spotList[id]!!.rows)
            intent.putExtra("cols",spotList[id]!!.cols)
            intent.putExtra("array",spotList[id]!!.available)
            startActivity(intent)
        }
        getParkingDetails()
    }

    @Synchronized
    fun bulidGoogleApiClient() {
        client = GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        client.connect()

    }

    fun getAdditionalDetails(id: String,obj:ParkingSpot) {
        var url = "http://50666bd0.ngrok.io/detail/?id=$id"
        var req = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->

                    Log.i("2nd",response.toString())
                    try {
                        obj.name=response.getString("name")
                        obj.cols=response.getString("cols")
                        obj.rows=response.getString("rows")
                        obj.contact=response.getString("contact")
                        var array=response.getJSONArray("available")
                        obj.available= IntArray(obj.cols.toInt()*obj.rows.toInt())
                        for (i in 0 until array.length()){
                            obj.available[i]=array[i].toString().toInt()
                        }

                        for(i in 0 until array.length()){
                           Log.i("array${obj.id}",obj.available[i].toString())
                        }
                        spotList[obj.id]=obj
                        Log.i("obj",obj.name)

                        var markerOptions = MarkerOptions()
                        markerOptions.position(LatLng(obj.latitude.toDouble(), obj.longitude.toDouble()))
                        markerOptions.title(obj.name)
//                        markerOptions.snippet("${spotList[obj.id]!!.slotsAvail} slots available")
                        if (obj.slotsAvail.toInt() >= 3) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        }
                        var mark = mMap.addMarker(markerOptions)

                        builder.include(mark.position)
                        bounds=builder.build()
                        cu=CameraUpdateFactory.newLatLngBounds(bounds,100)
                        mMap.moveCamera(cu)
                        mark.tag = obj.id
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this, "Unable to Fetch data", Toast.LENGTH_LONG)
                }
        )
        Volley.newRequestQueue(this).add(req)
    }
    fun getParkingDetails() {

//        builder.include(LatLng(mMap.myLocation.latitude,mMap.myLocation.longitude))
        que = Volley.newRequestQueue(this)
        jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->

                    Log.i("1st",response.toString())
                    try {
                        for (i in 0 until response.length()) {
                            // Get current json object
                            var location = response.getJSONObject(i)
                            var obj=ParkingSpot()

                            // Get the current student (json object) data
                            obj.latitude = location.getString("latitude")
                            obj.longitude = location.getString("longitude")
                            obj.slotsAvail = location.getString("slot_available")
                            obj.price = location.getString("price")
                            obj.id = location.getString("id")

                            getAdditionalDetails(obj.id,obj)

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this, "Unable to Fetch data", Toast.LENGTH_LONG)
                }
        )
        val handler = Handler()
        val runnableCode = object : Runnable {
            override fun run() {
                Log.d("Handlers", "Called on main thread")
                que.add(jsonArrayRequest)
                handler.postDelayed(this, 5000 )
            }
        }
        handler.post(runnableCode)
    }



}
