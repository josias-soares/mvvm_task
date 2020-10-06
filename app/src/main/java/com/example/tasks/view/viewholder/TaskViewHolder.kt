package com.example.tasks.view.viewholder

import android.app.AlertDialog
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder(itemView: View, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemView) {

    private val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private val mPriorityRepository = PriorityRepository(itemView.context)

    private var mTextDescription: TextView = itemView.findViewById(R.id.text_description)
    private var mTextPriority: TextView = itemView.findViewById(R.id.text_priority)
    private var mTextDueDate: TextView = itemView.findViewById(R.id.text_due_date)
    private var mImageTask: ImageView = itemView.findViewById(R.id.image_task)

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    fun bindData(task: TaskModel) {

        this.mTextDescription.text = task.description
        this.mTextPriority.text = mPriorityRepository.get(task.priorityId).description

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(task.dueDate)
        this.mTextDueDate.text = mDateFormat.format(date)

        if (task.complete) {
            mTextDescription.setTextColor(Color.GRAY)
            this.mImageTask.setImageResource(R.drawable.ic_done)
        } else {
            mTextDescription.setTextColor(Color.BLACK)
            this.mImageTask.setImageResource(R.drawable.ic_todo)
        }

        // Eventos
        mTextDescription.setOnClickListener { listener.onListClick(task.id) }
        mImageTask.setOnClickListener {
            if (task.complete){
                listener.onUndoClick(task.id)
            } else {
                listener.onCompleteClick(task.id)
            }
        }

        mTextDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { dialog, which ->
                    listener.onDeleteClick(task.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()
            true
        }

    }

}