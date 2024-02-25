package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todoapp.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createBtn.setOnClickListener {
            val db = DatabaseHelper(applicationContext)
            db.insertData(binding.task.text.toString(),0)

            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

//        binding.cancelBtn.setOnClickListener {
//            binding.task.setText("")
//            val intent = Intent(applicationContext,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }


    }
}