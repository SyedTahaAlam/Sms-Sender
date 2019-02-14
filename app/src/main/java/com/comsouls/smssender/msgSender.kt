package com.comsouls.smssender

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.Random

import com.comsouls.smssender.MainActivity.Companion.editor
import com.comsouls.smssender.MainActivity.Companion.sh

/**
 * Created by Syed Taha Alam on 4/19/2018.
 */

class msgSender : Service() {
    private var isRunning = false
    private var looper: Looper? = null
    private var myServiceHandler: MyServiceHandler? = null
    //    private ConnectionClass connectionClass = new ConnectionClass();
    private var no: Int = 0


    private var ip: String? = null
    private var password: String? = null
    private var un: String? = null
    private var db: String? = null
    private var connectionClass: ConnectionClass? = null

    // Binder given to clients
    private val mBinder = LocalBinder()
    // Random number generator
    private val mGenerator = Random()

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal// Return this instance of LocalService so clients can call public methods
        val service: msgSender
            get() {
                Log.e("in get", "method")
                return this@msgSender
            }
    }

    override fun onCreate() {
        val handlerthread = HandlerThread("MyThread", Process.THREAD_PRIORITY_BACKGROUND)
        handlerthread.start()
        looper = handlerthread.looper as Looper
        myServiceHandler = MyServiceHandler(this.looper!!)
        isRunning = true

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val msg = myServiceHandler!!.obtainMessage()
        msg.arg1 = startId
        no = 1

        if (intent != null) {
            noOfMesseagesTillLife = 0
            ip = intent.getStringExtra("ip")
            db = intent.getStringExtra("db")
            no = intent.getIntExtra("no", 0)
            un = intent.getStringExtra("un")
            password = intent.getStringExtra("password")
            connectionClass = ConnectionClass(ip!!, db!!, un!!, password!!)
            myServiceHandler!!.sendMessage(msg)
        }

        //        Toast.makeText(this, "MyService Started.", Toast.LENGTH_SHORT).show();
        //If service is killed while starting, it restarts.
        return Service.START_REDELIVER_INTENT
    }

    fun getNoOfMessegesSend(): Long {
        return noOfMessegesSend
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onDestroy() {
        isRunning = false
        no = 0
        noOfMessegesSend = 0
        MainActivity.editor!!.putString("NoOfMessages", noOfMessegesSend.toString()).commit()
        Log.e("IN DEstroy", "yes")
        //        Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }


    private inner class MyServiceHandler(looper: Looper) : Handler(looper) {

        @SuppressLint("UnlocalizedSms")
        override fun handleMessage(msg: Message) {

            synchronized(this) {
                var conn: Connection?
                val toGetStatus = "status"
                val toGetPhoneNo = "pNo"
                val toGetMessage = "message"
                val mContext = applicationContext
                //no==1

                while (no == 1) {
                    //no=0;
                    //                    Log.e("service","is running");
                    conn = connectionClass!!.CONN()

                    var rs: ResultSet? = null
                    if (conn != null) {
                        try {
                            conn.isReadOnly = false
                            Log.e("no ", "matched")
                            val str = "select * from MESSAGES"
                            val statement = conn.createStatement()
                            rs = statement.executeQuery(str)

                            while (rs!!.next()) {
                                Log.i("match Status", "un matched")
                                Log.e("status", rs.getString(toGetStatus))
                                if (rs.getString(toGetStatus) == "Sent") {
                                    Log.i("match Status", "matched")

                                    //
                                    val SENT = "SMS_SENT"
                                    val DELIVERED = "SMS_DELIVERED"


                                    //---when the SMS has been sent---

                                    val sms = SmsManager.getDefault()
                                    noOfMessegesSend++
//                                    sms.sendTextMessage(rs.getString(toGetPhoneNo), null, rs.getString(toGetMessage), null, null)
//                                        var name ="Pending"
                                    //                                    String name = "Sent";
                                    //                                    String query = "UPDATE Messages SET  " + toGetStatus + " =\'" + name + "\' WHERE msgID = " + rs.getInt("msgID") + ";";
                                    //                                    Statement stmt = conn.createStatement();
                                    //                                    stmt.executeUpdate(query);
                                    Log.e("scghcghchg status", rs.getString(toGetStatus))
                                    Thread.sleep((1000 * 5).toLong())
                                Log.e("msgSender", noOfMessegesSend.toString());
                                }


                            }
                        } catch (e: Exception) {
                            Log.e("error", e.message)
                        }

                    }
                    //                stopSelfResult(msg.arg1);

                }
                //stops the service for the start id.
                if (no == 0) {
                    stopSelfResult(msg.arg1)
                    Log.e("In Stop", "asjdhajskfh")
                    Toast.makeText(applicationContext, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }

    companion object {
        private val TAG = "MyService"
        var noOfMessegesSend: Long = 0
            get() = field
        private var noOfMesseagesTillLife: Long = 0

    }

}