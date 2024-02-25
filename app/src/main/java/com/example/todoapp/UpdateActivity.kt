package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdateBinding
    private var task_id:Int=0
    lateinit var task_description:String
    lateinit var db :DatabaseHelper
    private var task_status :Int = 0
    lateinit var intentReceivedData:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intentReceivedData = intent

        updateData()
        db = DatabaseHelper(applicationContext)
        binding.updateBtn.setOnClickListener {

            task_description = binding.task.text.toString()
            db.updateData(task_description,task_id,task_status)

            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()

        }

//        binding.cancelBtn.setOnClickListener {
//            val intent = Intent(applicationContext,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }

    fun updateData() {
        if (intentReceivedData.hasExtra("task_id") && intentReceivedData.hasExtra("task_description") && intentReceivedData.hasExtra("task_status") ){

             task_id = intentReceivedData.getIntExtra("task_id",0)
            task_description = intentReceivedData.getStringExtra("task_description")!!
            task_status = intentReceivedData.getIntExtra("task_status",0)

            binding.task.setText(task_description)

        }else   {
        }
    }
}