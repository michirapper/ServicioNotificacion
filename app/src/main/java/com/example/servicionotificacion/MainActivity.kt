package com.example.servicionotificacion

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.servicionotificacion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var numeroTouches: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onNewIntent(intent)

        binding.buttonStart.setOnClickListener {
            if (!servicioArrancado()) {
                numeroTouches += 1
                if (numeroTouches == 3){
                    binding.buttonStart.isEnabled = false
                }
                arrancarServicio()
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val extras = intent.extras
        var msg:String?
        if (extras != null) {
            if (extras.containsKey("NotificationMessage")) {
                msg = extras.getString("NotificationMessage")
                //binding.tvNotify.text = msg
                if (msg.equals("true")){
                    numeroTouches -= 1
                    if (numeroTouches <= 3){
                        binding.buttonStart.isEnabled = true
                    }
                }
                Toast.makeText(this, numeroTouches.toString(), Toast.LENGTH_SHORT).show()
            }
        }



    }


    private fun arrancarServicio(){
        startService(Intent(applicationContext, NotificacionService::class.java))
    }

    private fun servicioArrancado():Boolean{
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)){
            if (service.service.className == NotificacionService::class.java.name){
                return true
            }
        }
        return false
    }
}