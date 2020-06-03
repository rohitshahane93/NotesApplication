package com.example.myapplication.views.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.data.dataClasses.Note
import com.example.myapplication.util.Utils
import com.example.myapplication.util.extensions.getViewModelFactory
import com.example.myapplication.viewModels.NotesDetailViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_note.*
import java.util.*

class AddEditNoteFragment : Fragment() {

    private val viewModel by viewModels<NotesDetailViewModel> { getViewModelFactory() }
    private val args: AddEditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_edit_note, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utils.hideSoftKeyboard(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.noteId != null) {
            viewModel.start(args.noteId!!)
            observeNote()
        }
    }

    private fun observeNote() {
        viewModel.noteStateLiveData.observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is NotesDetailViewModel.GetNoteState.Loaded -> {
                    setData(it.note)
                }
            }
        })
    }

    private fun setData(note: Note) {
        txt_title.setText(note.title)
        txt_desc.setText(note.description)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_edit_notes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                if (!isInputValid()) {
                    return false
                }
                if (args.noteId == null) {
                    viewModel.addNote(getNote())
                    Toast.makeText(requireContext(), "Note added", Toast.LENGTH_SHORT).show()
                    navigateToList()
                } else {
                    viewModel.updateNote(getNote())
                    Toast.makeText(requireContext(), "Note updated", Toast.LENGTH_SHORT).show()
                    navigateToDetails()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isInputValid(): Boolean {
        if (txt_title.text.toString().isBlank() && txt_desc.text.toString().isBlank()) {
            Toast.makeText(requireContext(), "Note is blank", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun navigateToDetails() {
        findNavController().popBackStack()
    }

    private fun navigateToList() {
        findNavController().popBackStack()
    }

    private fun getNote(): Note {
        return Note(
            title = txt_title.text.toString(),
            description = txt_desc.text.toString(),
            createdTime = Date().time,
            updatedTime = null
        ).apply {
            if (args.noteId != null) {
                this.id = args.noteId!!
                this.updatedTime = Date().time
            }
        }
    }
}
