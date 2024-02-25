package com.example.todoapp.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.AddActivity
import com.example.todoapp.DatabaseHelper
import com.example.todoapp.Models.Task
import com.example.todoapp.UpdateActivity
import com.example.todoapp.databinding.TaskRowBinding

class Task_adapter(val context: Context, val activity: Activity, val notesList:ArrayList<Task>) :RecyclerView.Adapter<Task_adapter.ViewHolder>(){

    inner class ViewHolder(val binding:TaskRowBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = TaskRowBinding.inflate(LayoutInflater.from(context),parent,false)
            return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return notesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.taskDescription.text = notesList.get(position).task_name
        //updating the checkbox status
        if(notesList.get(position).task_status==1){
            holder.binding.taskStatus.isChecked = true
        }else{
            holder.binding.taskStatus.isChecked = false
        }
        //setting color of the task
        if(holder.binding.taskStatus.isChecked){
            holder.binding.taskBg.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.binding.taskBg.setBackgroundColor(Color.WHITE)
        }

        holder.binding.taskStatus.setOnCheckedChangeListener { button, b ->
            // Update task status in the database
            if(b){
                DatabaseHelper(context).updateData(notesList.get(position).task_name,notesList.get(position).id,1)
                notesList.get(position).task_status=1
            }else{
                DatabaseHelper(context).updateData(notesList.get(position).task_name,notesList.get(position).id,0)
                notesList.get(position).task_status=0
            }

            // Update the task status in the current list
            // Notify adapter about the change
            notifyDataSetChanged()
        }



        //opening the update Activity
        holder.binding.taskDescription.setOnClickListener {
            openActivityForResult(position)
        }
    }

    fun openActivityForResult(position: Int) {
        val intent = Intent(context,UpdateActivity::class.java)
        intent.putExtra("task_id",notesList.get(position).id)
        intent.putExtra("task_description",notesList.get(position).task_name)
        intent.putExtra("task_status",notesList.get(position).task_status)
        activity.startActivity(intent)
    }

}