package com.comsouls.smssender

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText

import android.widget.TextView
import android.widget.Toast
import com.comsouls.smssender.msgSender.LocalBinder
import kotlinx.android.synthetic.main.content_main.*

import java.sql.Connection

class MainActivity : AppCompatActivity() {


    internal var i: Intent?= null
    private val mchk = 0
    private var mService: msgSender? = null
    internal var mBound = false
    //    private String name;
    internal var ip: String? = null
    internal var db: String? = null
    internal var un: String? = null
    internal var passwords: String? = null



    internal var msgofLife: Long = 0

    private val mConnection = object : ServiceConnection {
        // Called when the connection with the service is established
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocalBinder
            mService = binder.service
            Log.e("nbnb", "nbnb")
            mBound = true

        }

        // Called when the connection with the service disconnects unexpectedly
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e("activity", "onServiceDisconnected")
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        no_of_msgs_in_Life = findViewById(R.id.no_of_msgs_in_Life)
//        msgsForActivity = findViewById(R.id.noofmsgs)


        c = applicationContext
        sh = PreferenceManager.getDefaultSharedPreferences(c) as SharedPreferences?
        editor = sh!!.edit() as SharedPreferences.Editor?
//        var   ip = sh!!.getString("ipAddress", null)
//        var db = sh!!.getString("databaseName", null)
//        var un = sh!!.getString("userName", null)
//        var passwords = sh!!.getString("password", null)
        if (sh!!.getBoolean("is_first_time", true)) {
            Log.d("TAG", "First time")

            editor!!.putLong("NoOfmsgsInLife", 0).commit()
            editor!!.putBoolean("is_first_time", false).commit()


        } else {
            msgofLife = sh!!.getLong("msgCount", 0)
            no_of_msgs_in_Life.text = msgofLife.toString()
        }
        val intent = getIntent()

        if (intent.getStringExtra("ipAddress") != null && intent.getStringExtra("databaseName") != null && intent.getStringExtra("userName") != null && intent.getStringExtra("password") != null) {
            ip = intent.getStringExtra("ipAddress")
         db = intent.getStringExtra("databaseName")
         un = intent.getStringExtra("userName")
         passwords = intent.getStringExtra("password")

        } else {

         ip = sh!!.getString("ipAddress", null)
         db = sh!!.getString("databaseName", null)
         un = sh!!.getString("userName", null)
         passwords = sh!!.getString("password", null)

        }

        var name:String? = sh!!.getString("name", null)
        if (name.toString() != null) {
            if (name == "Stop") {
                on_or_off.setText(name)
                state = 0

            } else {
                on_or_off.setText("Start")
                state = 1


            }
        } else {
            on_or_off.setText("Start")

        }
        connecttoNetwork.setOnClickListener {
            val intent = Intent(applicationContext, GetConfig::class.java)
            startActivity(intent)
        }
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val requestSmsPermission = requestSmsPermission()


        /**
         * table name =MESSAGES;
         * column= pNo,status,Message
         */
//        var ips=ip;
//        var dbs=db;
//        var uns=un;
//        var pass=passwords;

//            val connectionClass = ConnectionClass(ip, db, un, passwords)
//            val con = connectionClass.CONN()

//            on_or_off.setOnClickListener {
//            val name = on_or_off.text as String
//            if (sh!!.getBoolean("permission", false) == true) {
//                if (name == "Start") {
//                    if (ip != null && db!= null && un!= null && passwords!= null) {
//
//                        i = Intent(applicationContext, msgSender::class.java) as Intent?
//                        state = 1
//                        i!!.putExtra("no", state)
//                        i!!.putExtra("ip", ip)
//                        i!!.putExtra("db", db)
//                        i!!.putExtra("un", un)
//                        i!!.putExtra("no", 1)
//                        i!!.putExtra("password", passwords)
//
//                        //                        startService(i);
//                        Log.e("name", "is")
//
//                        Log.e("have binded service", "")
//                        if (ConnectionStatus.isNetworkConnectionAvailable(this@MainActivity)) {
//
//                            bindService(i, mConnection, Context.BIND_AUTO_CREATE)
//                            startService(i)
//                            if (mBound) {
//                                noofmsgs.text = 0.toString()
//
//                            }
//                            on_or_off.text = "Stop"
//                            editor!!.putString("name", "Stop").commit()
//                        }
//
//                    } else {
//                        Toast.makeText(applicationContext, "Please Connect To Data Base", Toast.LENGTH_LONG).show()
//                    }
//                } else {
//                    val i = Intent(applicationContext, msgSender::class.java)
//
//                    if (mBound) {
//                        noofmsgs.text = mService!!.getNoOfMessegesSend().toString()
//                        msgofLife = java.lang.Long.valueOf(no_of_msgs_in_Life.text.toString())!! + java.lang.Long.valueOf(noofmsgs.text.toString())!!
//                        no_of_msgs_in_Life.text = msgofLife.toString()
//                    }
//                    intent.putExtra("no", 0)
//                    unbindService(mConnection)
//                    stopService(i)
//                    on_or_off.text = "Start"
//                    editor!!.putString("name", "Start").commit()
//                    editor!!.putInt("state", 0).commit()
//
//                }
//            } else
//                Toast.makeText(applicationContext, "Sms permission rejected", Toast.LENGTH_LONG)
//        }


    }

    override fun onDestroy() {
        Log.e("on destroy", "killed")
        super.onDestroy()
    }

    override fun onResume() {
        i = Intent(applicationContext, msgSender::class.java)

        bindService(i, mConnection, Context.BIND_AUTO_CREATE)

        super.onResume()


    }

    override fun onStop() {
        editor!!.putLong("msgCount", java.lang.Long.valueOf(no_of_msgs_in_Life.text.toString())!!).commit()

        super.onStop()
    }

    private fun requestSmsPermission(): Int {
        val permission = Manifest.permission.SEND_SMS
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permission_list = arrayOfNulls<String>(1)
            permission_list[0] = permission
            ActivityCompat.requestPermissions(this, permission_list, 1)
            return 1
        }
        return 0

    }
    fun buttonpressed(view: View){
Log.e("error","nahin===")
            val name = on_or_off.text as String
            if (sh!!.getBoolean("permission", false) == true) {
                if (name == "Start") {
                    if (ip != null && db!= null && un!= null && passwords!= null) {

                        i = Intent(applicationContext, msgSender::class.java) as Intent?
                        state = 1
                        i!!.putExtra("no", state)
                        i!!.putExtra("ip", ip)
                        i!!.putExtra("db", db)
                        i!!.putExtra("un", un)
                        i!!.putExtra("no", 1)
                        i!!.putExtra("password", passwords)

                        //                        startService(i);
                        Log.e("name", "is")

                        Log.e("have binded service", "")
                        if (ConnectionStatus.isNetworkConnectionAvailable(this@MainActivity)) {

                            bindService(i, mConnection, Context.BIND_AUTO_CREATE)
                            startService(i)
                            if (mBound) {
                                noofmsgs.text = 0.toString()

                            }
                            on_or_off.text = "Stop"
                            editor!!.putString("name", "Stop").commit()
                        }

                    } else {
                        Toast.makeText(applicationContext, "Please Connect To Data Base", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val i = Intent(applicationContext, msgSender::class.java)
                    Log.e("MainActivity","in stop")
                    if (mBound) {
                        noofmsgs.text = mService!!.getNoOfMessegesSend().toString()
                        msgofLife = java.lang.Long.valueOf(no_of_msgs_in_Life.text.toString())!! + java.lang.Long.valueOf(noofmsgs.text.toString())!!
                        no_of_msgs_in_Life.text = msgofLife.toString()
                    }
                    intent.putExtra("no", 0)
                    unbindService(mConnection)
                    stopService(i)
                    on_or_off.text = "Start"
                    editor!!.putString("name", "Start").commit()
                    editor!!.putInt("state", 0).commit()

                }
            } else
                Toast.makeText(applicationContext, "Sms permission rejected", Toast.LENGTH_LONG).show()
        }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "permission granted", Toast.LENGTH_SHORT).show()
                editor!!.putBoolean("permission", true).commit()

            } else {
                Toast.makeText(this@MainActivity, "permission not granted", Toast.LENGTH_SHORT).show()
                editor!!.putBoolean("permission", false).commit()

            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId


        if (id == R.id.action_settings) {
            val intent = Intent(applicationContext, GetConfig::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        var c: Context?=null
        var sh: SharedPreferences? =null
        var state = 0
        var editor: SharedPreferences.Editor? =null
    }
}
