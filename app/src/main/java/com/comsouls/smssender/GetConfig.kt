package com.comsouls.smssender

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.comsouls.*
import kotlinx.android.synthetic.main.activity_get_config.*

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class GetConfig : AppCompatActivity() {




    // ...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_config)




        val connection = ConnectionClass(ip_address.text.toString(), database_name.text.toString(), username.text.toString(), password.text.toString())


        connect.setOnClickListener {
            val connection = ConnectionClass(ip_address.text.toString(), database_name.text.toString(), username.text.toString(), password.text.toString())

            if (ConnectionStatus.isNetworkConnectionAvailable(this@GetConfig)) {
                try {

                    creatConnection(connection)
                } catch (e: SQLException) {

                    Toast.makeText(this@GetConfig, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()

                }

            }
        }

    }

    @Throws(SQLException::class)
    private fun creatConnection(connection: ConnectionClass) {
        val connection1 = connection.CONN()
        if (connection1 != null) {
            val str = "select * from MESSAGES"
            val statement = connection1.createStatement()
            val rs = statement.executeQuery(str)
            if (rs.next()) {

                Toast.makeText(applicationContext, "Connection has been made", Toast.LENGTH_SHORT).show()

            } else {

                Toast.makeText(applicationContext, "No data is in the table", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(applicationContext, "Connection hasn't been made", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onBackPressed() {
//        (ip_address.text.toString(), database_name.text.toString(), username.text.toString(), password.text.toString())
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("ipAddress", ip_address.text.toString())
        intent.putExtra("databaseName", database_name.text.toString())
        intent.putExtra("userName", username.text.toString())
        intent.putExtra("password", password.text.toString())
        MainActivity.editor!!.putString("ipAddress", ip_address.text.toString()).commit()
        MainActivity.editor!!.putString("databaseName", database_name.text.toString()).commit()
        MainActivity.editor!!.putString("userName", username.text.toString()).commit()
        MainActivity.editor!!.putString("password", password.text.toString()).commit()
        startActivity(intent)
        finish()

    }
}
