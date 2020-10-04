package com.example.tasks.view.viewholder

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository

class TaskViewHolder(itemView: View, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemView) {

    private val mPriorityRepository = PriorityRepository(itemView.context)

    private var mTextDescription: TextView = itemView.findViewById(R.id.text_description)
    private var mTextPriority: TextView = itemView.findViewById(R.id.text_priority)
    private var mTextDueDate: TextView = itemView.findViewById(R.id.text_due_date)
    private var mImageTask: ImageView = itemView.findViewById(R.id.image_task)

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    fun bindData(taskModel: TaskModel) {

        this.mTextDescription.text = taskModel.description
        this.mTextPriority.text = mPriorityRepository.get(taskModel.priorityId).description
        this.mTextDueDate.text = taskModel.dueDate

        if (taskModel.complete) {
            this.mImageTask.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_done
                )
            )
        } else {
            this.mImageTask.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_todo
                )
            )
        }

        // Eventos
        // mTextDescription.setOnClickListener { listener.onListClick(task.id) }
        // mImageTask.setOnClickListener { }

        mTextDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { dialog, which ->
                    // listener.onDeleteClick(task.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()
            true
        }

    }

}