package com.example.tasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants.BUNDLE.TASKID
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_register.button_save
import kotlinx.android.synthetic.main.activity_task_form.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    // Dependence Injection
    private val mViewModel: TaskFormViewModel by viewModel()

    private var mTaskId: Int = 0
    private val mListPriorityId: MutableList<Int> = arrayListOf()
    private val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        // Inicializa eventos
        listeners()
        observe()

        mViewModel.listPriorities()

        loadDataFromActivity()
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras

        if (bundle != null) {
            mTaskId = bundle.getInt(TASKID)
            mViewModel.load(mTaskId)

            button_save.setText(R.string.altera_tarefa)
        } else {
            button_save.setText(R.string.update_task)
        }

    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.button_save -> {
                handleSave()
            }
            R.id.button_date -> {
                showDatePicker()
            }
        }
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = mTaskId
            this.description = edit_description.text.toString()
            this.dueDate = button_date.text.toString()
            this.priorityId = mListPriorityId[spinner_priority.selectedItemPosition]
            this.complete = check_complete.isChecked
        }

        mViewModel.save(task)
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        DatePickerDialog(
            this,
            this,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun observe() {
        mViewModel.priorities.observe(this, {
            val list: MutableList<String> = arrayListOf()

            for (priorityModel in it) {
                list.add(priorityModel.description)
                mListPriorityId.add(priorityModel.id)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            spinner_priority.adapter = adapter
        })

        mViewModel.taskCreated.observe(this, {
            if (it.success()) {
                if (mTaskId == 0) {
                    toast(R.string.task_created)
                } else {
                    toast(R.string.task_updated)
                }
                finish()
            } else {
                toast(it.failure())
            }
        })

        mViewModel.task.observe(this, {
            edit_description.setText(it.description)
            check_complete.isChecked = it.complete
            spinner_priority.setSelection(getIndexPriority(it.priorityId))

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.dueDate)
            button_date.text = mDateFormat.format(date!!)
        })
    }

    private fun toast(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show()
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun getIndexPriority(priorityId: Int): Int {
        var index = 0
        for (i in 0 until mListPriorityId.count()) {
            if (mListPriorityId[i] == priorityId) {
                index = i
                break
            }
        }

        return index
    }

    private fun listeners() {
        button_save.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        button_date.text = mDateFormat.format(calendar.time)
    }

}
