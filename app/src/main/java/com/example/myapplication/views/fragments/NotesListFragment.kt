package com.example.myapplication.views.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.dataClasses.Note
import com.example.myapplication.util.extensions.getViewModelFactory
import com.example.myapplication.util.extensions.ids
import com.example.myapplication.viewModels.NotesViewModel
import com.example.myapplication.views.adapters.NotesAdapter
import kotlinx.android.synthetic.main.fragment_notes_list.*
import java.util.*

class NotesListFragment : Fragment(), NotesAdapter.NoteClickListener, ActionMode.Callback {

    private val viewModel by viewModels<NotesViewModel> { getViewModelFactory() }
    private var adapter: NotesAdapter? = null
    private var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setURefreshLayout()
        add_note_fab.setOnClickListener {
            viewModel.addNote(
                Note(
                    title = "test title",
                    description = "test desc",
                    createdTime = Date().time,
                    updatedTime = Date().time
                )
            )
        }
        add_note_fab.setOnClickListener {
            navigateToAddNote()
        }
        observeNotes()
    }

    private fun setURefreshLayout() {
        refresh_layout.setOnRefreshListener {
            refresh_layout.isRefreshing = false
        }
    }

    private fun setUpRecyclerView() {
        notes_list.layoutManager = LinearLayoutManager(requireContext())
        notes_list.setHasFixedSize(true)
        adapter = NotesAdapter(requireContext(), emptyList(), this)
        notes_list.adapter = adapter
    }

    private fun observeNotes() {
        viewModel.notesStateLiveData.observe(this.viewLifecycleOwner, Observer {
            when (it) {
                is NotesViewModel.GetAllNotesState.Loaded -> {
                    txt_no_notes.visibility = if (it.notes.isEmpty()) View.VISIBLE else View.GONE
                    adapter?.notifyDataSetChanged(it.notes)
                }
            }
        })
        viewModel.start()
    }

    override fun onNoteClick(note: Note) {
        navigateToNoteDetail(note.id)
    }

    override fun onNoteLongPress(note: Note): Boolean {
        adapter?.startSelectionMode()
        view?.startActionMode(this)
        return false
    }

    override fun onSelectedCountChanged(count: Int) {
        if (count <= 0) {
            actionMode?.finish()
        } else {
            actionMode?.title = "$count selected"
        }
    }

    private fun navigateToNoteDetail(noteId: String) {
        val action =
            NotesListFragmentDirections.actionNotesListFragmentToNotesDetailFragment(noteId)
        findNavController().navigate(action)
    }

    private fun navigateToAddNote() {
        val action =
            NotesListFragmentDirections.actionNotesListFragmentToAddEditNoteFragment(null)
        findNavController().navigate(action)
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_delete -> {
                adapter?.getSelected()?.let {
                    if (it.isNotEmpty()) {
                        showDeleteConfirmationDialog(it.count())
                    }
                }

            }
        }
        return true
    }

    private fun finishActionMode() {
        actionMode?.finish()
        actionMode = null
    }

    private fun showDeleteConfirmationDialog(count: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete notes")
            .setMessage("Delete $count notes?")
            .setPositiveButton("Yes") { _, _ ->
                adapter?.getSelected()?.let {
                    viewModel.deleteNotes(it.ids())
                }
                finishActionMode()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        actionMode = mode
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.menu_action_mode, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        adapter?.finishSelectionMode()
        adapter?.unSelectAll()
    }

    override fun onDestroyView() {
        finishActionMode()
        super.onDestroyView()
    }

}
