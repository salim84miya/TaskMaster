package com.example.todoapp

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Adapters.Task_adapter
import com.example.todoapp.Models.Task
import com.example.todoapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var taskAdapter: Task_adapter
    lateinit var taskList: ArrayList<Task>
    lateinit var db :DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskList = ArrayList()

        binding.addBtn.setOnClickListener {
            openActivityForResult()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        taskAdapter = Task_adapter(applicationContext, this@MainActivity, taskList)
        binding.recyclerView.adapter = taskAdapter
        addAndReadData()


    }

    fun openActivityForResult() {
        startForResult.launch(Intent(this, AddActivity::class.java))
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                recreate()

                Toast.makeText(applicationContext, "recreated the activity", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    fun addAndReadData() {
        db = DatabaseHelper(applicationContext)
        val cursor: Cursor? = db.readData()

        if (cursor?.count == 0) {
            Toast.makeText(applicationContext, "no data", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor?.moveToNext() == true) {
                taskList.add(
                    Task(
                        cursor.getString(0).toInt(),
                        cursor.getString(1).toString(),
                        cursor.getString(2).toInt()
                    )
                )

            }

            taskAdapter.notifyDataSetChanged()

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    var deleteTask: Task? = null

    val itemTouchHelperCallback =
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = taskList.get(viewHolder.adapterPosition).id
                val pos = viewHolder.adapterPosition
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Delete item?")
                builder.setMessage("Are you sure you want to delete this item from list")
                builder.setIcon(R.drawable.baseline_delete_24)
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                    deleteTask = taskList.get(viewHolder.adapterPosition)
                    db.deleteData(position)
                    taskList.removeAt(viewHolder.adapterPosition)
                    taskAdapter.notifyDataSetChanged()

                    Snackbar.make(binding.recyclerView, deleteTask!!.task_name,Snackbar.LENGTH_LONG)
                        .setAction("Undo", View.OnClickListener {
                            taskList.add(pos,deleteTask!!)
                            db.insertDataAgain(deleteTask!!.id,deleteTask!!.task_name,deleteTask!!.task_status)
                            taskAdapter .notifyDataSetChanged()
                        }).show()

                })
                builder.setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->

                    taskAdapter.notifyDataSetChanged()
                })
                builder.show()
            }

        }




}