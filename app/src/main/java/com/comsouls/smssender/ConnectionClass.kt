package com.comsouls.smssender

/**
 * Created by Syed Taha Alam on 4/19/2018.
 */

import android.annotation.SuppressLint
import android.os.StrictMode
import android.util.Log

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Created by Syed Taha Alam on 4/16/2018.
 */

class ConnectionClass(internal var ip: String, internal var db: String, internal var un: String, internal var password: String) {
    internal var classs = "net.sourceforge.jtds.jdbc.Driver"

    internal var conn: Connection?=null


    //    String ip="70.35.202.41";
    //    String db="NEW_GARMENTS_DB";
    //    String un="helahi";
    //    String password="Cst42866800123456789";
    @SuppressLint("NewApi")
    fun CONN(): Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {

            Class.forName(classs)
            val ConnURL = ("jdbc:jtds:sqlserver://" + ip + "/"
                    + db +
                    ";user=" + un +
                    ";password=" + password + ";")
            conn = DriverManager.getConnection(ConnURL)
        } catch (e: ClassNotFoundException) {
            Log.e("class nahi mli", "blkl")
            Log.e("error ", "" + e.message)
            e.printStackTrace()
        } catch (e: SQLException) {

            Log.e("sql nahi chalega", "")
            e.printStackTrace()
        }

        return conn
    }
}
