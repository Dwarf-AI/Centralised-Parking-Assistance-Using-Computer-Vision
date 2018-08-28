package com.android.abhishekvats.parkingsystem

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

import org.json.JSONException
import java.lang.Math.round


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
                                         ,LocationListener{
    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(p0: Location?) {
    }


    private lateinit var mMap: GoogleMap
    private lateinit var client:GoogleApiClient
    var url="http://7cdd28c8.ngrok.io/?lat=28.334434&lon=77.317066"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.custom_map))
        var markerOptions=MarkerOptions()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient()
            mMap.isMyLocationEnabled = true

        }
        mMap.uiSettings.isMapToolbarEnabled=false
        mMap.setOnInfoWindowClickListener {

            var results=FloatArray(10)
            var intent = Intent(this, SpotStatus::class.java)
            try {
                Location.distanceBetween(mMap.myLocation.latitude, mMap.myLocation.longitude, it.position.latitude, it.position.longitude, results)
            }
            catch (Excep:Exception){
                Toast.makeText(this,"Turn On Loaction Services",Toast.LENGTH_LONG)
            }
            intent.putExtra("name", it.title)
            intent.putExtra("dist",round((results[0]/1000)).toString()+" km")
            intent.putExtra("spots",it.snippet.substring(0,1))
//            getLayoutDetails(it.tag.toString(),intent)
            intent.putExtra("price",it.tag.toString())
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
    fun getLayoutDetails(id:String,intent: Intent){
        var url= "https://7cdd28c8.ngrok.io/detail/?id=$id"
        var que=Volley.newRequestQueue(this)
        lateinit var available:IntArray
        val req = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    response ->  Log.i("Response" , response.toString())
                    var array = response.getJSONArray("available")
                    available=IntArray(array.length())
                    for(i in 0 until array.length()){
                       available[i]=array.getInt(i)
                    }
                    var cols=response.getInt("cols")
                    var row=response.getInt("rows")
                    var price=response.getString("price")
                    intent.putExtra("price",price)
                    intent.putExtra("cols",cols)
                    intent.putExtra("rows",row)
                },
                Response.ErrorListener { error->
                    Toast.makeText(this, "Exception ${error.message} ", Toast.LENGTH_LONG).show()
                })

        que.add(req)

    }
    fun getParkingDetails(){

        val que=Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->

                    try {
                        for (i in 0 until response.length()) {
                            // Get current json object
                            var location = response.getJSONObject(i)

                            // Get the current student (json object) data
                            var latitude = location.getString("latitude")
                            var longitude = location.getString("longitude")
                            var slots = location.getString("slot_available")
                            var price =location.getString("price")

                            var markerOptions=MarkerOptions()
                            markerOptions.position(LatLng(latitude.toDouble(),longitude.toDouble()))
                            markerOptions.title("ParkingSpot${i}")
                            markerOptions.snippet("${slots} slots available")
                            if(slots.toInt()>=3){
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            }
                            var mark= mMap.addMarker(markerOptions)
                            mark.tag=price
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this,"Unable to Fetch data", Toast.LENGTH_LONG)
                }
        )
        que.add(jsonArrayRequest)
    }
}
