package com.example.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.constants.TaskConstants.BUNDLE.TASKFILTER
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.view.adapter.TaskAdapter
import com.example.tasks.viewmodel.AllTasksViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllTasksFragment : Fragment() {

    // Dependence Injection
    private val mViewModel: AllTasksViewModel by viewModel()
    private lateinit var mPriorityRepository: PriorityRepository

    private var mTaskFilter: Int = 0

    private lateinit var mListener: TaskListener
    private lateinit var mAdapter: TaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_all_tasks, container, false)

        mTaskFilter = requireArguments().getInt(TASKFILTER, 0)

        mPriorityRepository = get()
        mAdapter = TaskAdapter(mPriorityRepository)

        val recycler = root.findViewById<RecyclerView>(R.id.recycler_all_tasks)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = mAdapter

        // Eventos disparados ao clicar nas linhas da RecyclerView
        mListener = object : TaskListener {
            override fun onListClick(id: Int) {
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDeleteClick(id: Int) {
                mViewModel.delete(id)
            }

            override fun onCompleteClick(id: Int) {
                mViewModel.complete(id)
            }

            override fun onUndoClick(id: Int) {
                mViewModel.undo(id)
            }
        }

        // Cria os observadores
        observe()

        // Retorna view
        return root
    }

    override fun onResume() {
        super.onResume()
        mAdapter.attachListener(mListener)
        mViewModel.list(mTaskFilter)
    }

    private fun observe() {
        mViewModel.tasks.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                mAdapter.updateList(it)
            }
        })

        mViewModel.taskRemoved.observe(viewLifecycleOwner, {
            if (it.success()) {
                Toast.makeText(context, R.string.task_removed, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_LONG).show()
            }
        })

        mViewModel.taskUpdate.observe(viewLifecycleOwner, {
            if (it.success()) {
                Toast.makeText(context, R.string.task_updated, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_LONG).show()
            }
        })
    }

}
