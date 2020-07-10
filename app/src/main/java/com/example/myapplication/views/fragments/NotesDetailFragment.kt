package com.example.myapplication.views.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.data.dataClasses.Note
import com.example.myapplication.data.enums.TextSizes
import com.example.myapplication.util.Utils
import com.example.myapplication.util.extensions.getViewModelFactory
import com.example.myapplication.viewModels.NotesDetailViewModel
import kotlinx.android.synthetic.main.fragment_notes_detail.*
import kotlinx.android.synthetic.main.fragment_notes_list.*


class NotesDetailFragment : Fragment() {
    private val viewModel by viewModels<NotesDetailViewModel> { getViewModelFactory() }
    private val args: NotesDetailFragmentArgs by navArgs()
    private var currentSize = TextSizes.SIZE_6

    private fun getNextSize(): TextSizes {
        if (currentSize.size < TextSizes.SIZE_10.size) {
            for (size in TextSizes.values()) {
                if (size.size > currentSize.size) {
                    return size
                }
            }
        }
        return TextSizes.SIZE_6
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_notes_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start(args.noteId)
        observeNote()
    }

    private fun observeNote() {
        viewModel.noteStateLiveData.observe(this.viewLifecycleOwner, Observer {
            when (it) {
                is NotesDetailViewModel.GetNoteState.Loaded -> {
                    setData(it.note)
                }
            }
        })
    }

    private fun setData(note: Note) {
        txt_title.visibility = if (note.title.isBlank()) View.INVISIBLE else View.VISIBLE
        txt_title.text = note.title
        txt_desc.text = note.description
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notes_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> {
                navigateToEditNote()
            }
            R.id.menu_delete -> {
                showDeleteConfirmationDialog()
            }
            R.id.menu_text_size -> {
                currentSize = getNextSize()
                txt_title.textSize =
                    Utils.getPxForDp(requireContext(), currentSize.size.toFloat())
                txt_desc.textSize =
                    Utils.getPxForDp(requireContext(), currentSize.size.toFloat())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToEditNote() {
        val action =
            NotesDetailFragmentDirections.actionNotesDetailFragmentToAddEditNoteFragment(args.noteId)
        findNavController().navigate(action)
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteNote(args.noteId)
                findNavController().popBackStack(R.id.notesListFragment, true)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
