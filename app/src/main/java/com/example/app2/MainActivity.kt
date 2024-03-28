package com.example.app2

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import java.util.Locale

class Handler : View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener,
    DialogInterface.OnClickListener
{

    private var am_stations = arrayOf("wfan","kabc","wbap","wls","karnam")
    private var fm_stations = arrayOf("wxyt","kurb","wkim","wwtn","kdxe","karnfm","klal")
    private var fm_nums = arrayOf(97.1,98.5,98.9,99.7,101.9,102.9,107.7)
    private var am_nums = arrayOf(660,790,820,890,920)
    private var setting = "am"
    private var current = "" //current station. 'wxytfm' 'wabcam'
    private var imageView = MainActivity.getInstance().findViewById<ImageView>(R.id.imageView)
    private var textView = MainActivity.getInstance().findViewById<TextView>(R.id.textView)


    override fun onClick(p0: DialogInterface?, p1: Int) {
        if (p1 == DialogInterface.BUTTON_NEGATIVE)
        { }
        else if (p1 == DialogInterface.BUTTON_POSITIVE)
        { }
    }

    override fun onClick(p0: View?) {
        var wv : WebView = MainActivity.getInstance().findViewById<WebView>(R.id.webView)
        if (current != ""){
            var radio = "https://playerservices.streamtheworld.com/api/livestream-redirect/" +
                    current + setting + ".mp3"
            //wv?.loadUrl("google.com")
            wv.settings.javaScriptEnabled = true
            wv.settings.userAgentString = "Mozilla/5.0 (Windows NT 5.1; rv:40.0) Gecko/20100101 Firefox/40.0"



            wv?.loadUrl(radio)
            //wv?.loadUrl("http://static.france24.com/live/F24_EN_LO_HLS/live_web.m3u8")
        }

    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        var progress = p1 - 15
        var newProgress : Double = 0.0
        var margin = 6
        if (setting == "am") {
            newProgress = (540 + (11.6 * progress)) //convert 0-100 scale to am scale
            progress = newProgress.toInt()
            var index:Int = 0
            var isChannel : Boolean = false
            for(value in am_nums)
            {
                isChannel = (progress >= value - margin && progress < value + margin)
                if(!isChannel)
                    current = ""
                if(isChannel)
                {
                    current = am_stations[index]
                    if(current.equals("karnam"))
                        current = "karn"
                    break
                }
                index++
            }
            /*when (progress) {
                am_nums[0] -> current = am_stations[0]
                am_nums[1] -> current = am_stations[1]
                am_nums[2] -> current = am_stations[2]
                am_nums[3] -> current = am_stations[3]
                am_nums[4] -> current = am_stations[3]
                else -> current = ""
            }*/
        }
        else if (setting == "fm"){
            progress = p1 + 6
            newProgress = (88 + (0.2 * progress))//convert 0-100 scale to am scale
            progress = newProgress.toInt()
            margin = 1
            var index:Int = 0
            var isChannel : Boolean = false
            for(value in fm_nums)
            {
                isChannel = (progress >= value - margin && progress < value + margin)
                if(!isChannel)
                    current = ""
                if(isChannel)
                {
                    current = fm_stations[index]
                    if(current.equals("karnfm"))
                        current = "karn"
                    break
                }
                index++
            }
            /*when (progress) {
                fm_nums[0] as Int -> current = fm_stations[0]
                fm_nums[1] as Int-> current = fm_stations[1]
                fm_nums[2] as Int-> current = fm_stations[2]
                fm_nums[3] as Int-> current = fm_stations[3]
                fm_nums[4] as Int-> current = fm_stations[4]
                fm_nums[5] as Int-> current = fm_stations[5]
                fm_nums[6] as Int-> current = fm_stations[6]
                else -> current = ""
            }*/
        }
        if (current != "") {
            var id = MainActivity.getInstance().resources.getIdentifier(current, "drawable",MainActivity.getInstance().packageName)
            imageView.setImageResource(id)
            textView.setText(current.toUpperCase(Locale.getDefault()))
        }
        else {
            var id = MainActivity.getInstance().resources.getIdentifier("launch", "drawable",MainActivity.getInstance().packageName)
            imageView.setImageResource(id)
            textView.setText("Now Playing...")
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        var text = p0?.getText()
        if (text == "AM") {
            p0?.setText("FM")
            setting = "fm"
        }
        else {
            p0?.setText("AM")
            setting = "am"
        }
        current = ""
        var id = MainActivity.getInstance().resources.getIdentifier("launch", "drawable",MainActivity.getInstance().packageName)
        imageView.setImageResource(id)
        textView.setText("Now Playing...")
    }


}
class MainActivity : AppCompatActivity() {

    companion object
    {
        private var instance : MainActivity? = null

        public fun getInstance() : MainActivity
        {
            return instance!!
        }

    }

    /*override fun onResume(){
        super.onResume()
        //Change image
        var id = MainActivity.getInstance().resources.getIdentifier("launch", "drawable",MainActivity.getInstance().packageName)
        var image = findViewById<ImageView>(R.id.imageView)
        image.setImageResource(id)

        //Change text
        var text = findViewById<TextView>(R.id.textView)
        text.text = "Now Playing..."

        //Change Seekbar
        var seekBar = findViewById<SeekBar>(R.id.slider)
        seekBar.progress = 0

        //Change Switch
        var switch = findViewById<Switch>(R.id.switch1)
        switch.isChecked = true


    }*/
    fun checkInternetConnection(): Boolean
    {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Test for connection
        cm.isDefaultNetworkActive
        val networkInfo = isNetworkAvailable(this)//cm.isDefaultNetworkActive
        if (networkInfo)
            return true
        else
            return false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportActionBar != null)
            supportActionBar?.hide() //Hide Title Bar
        setContentView(R.layout.activity_main)

        instance = this

        //create handler
        var handler = Handler()

        //if user doesn't have internet connection, create a warning dialog
        var result = checkInternetConnection()
        if (!result) {
            //Display alert box
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Not Connected To Internet")
            dialogBuilder.setPositiveButton("OK",handler)
            dialogBuilder.setNegativeButton("Cancel", handler)
            val alert1 = dialogBuilder.create()
            alert1.setTitle("No Internet")
            alert1.show()

        }

        val slider = findViewById<SeekBar>(R.id.slider)
        val switch1 = findViewById<Switch>(R.id.switch1)
        val button = findViewById<Button>(R.id.button)
        //Register Handler with UI
        slider.setOnSeekBarChangeListener(handler)
        switch1.setOnCheckedChangeListener(handler)
        button.setOnClickListener(handler)

    }

    fun isNetworkAvailable(context: Context): Boolean {
        var connectivityManager =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network =
                connectivityManager.activeNetwork // network is currently in a high power state for performing data transmission.
            Log.d("Network", "active network $network")
            network ?: return false // return false if network is null
            val actNetwork = connectivityManager.getNetworkCapabilities(network)
                ?: return false // return false if Network Capabilities is null
            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> { // check if wifi is connected
                    Log.d("Network", "wifi connected")
                    true
                }

                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> { // check if mobile dats is connected
                    Log.d("Network", "cellular network connected")
                    true
                }

                else -> {
                    Log.d("Network", "internet not connected")
                    false
                }
            }
        }
        return false;
    }
}



