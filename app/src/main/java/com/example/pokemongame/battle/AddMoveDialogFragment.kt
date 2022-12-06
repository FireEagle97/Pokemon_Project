package com.example.pokemongame

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.pokemongame.pokemon.Move
import com.example.pokemongame.pokemon.Pokemon


class AddMoveDialogFragment() : DialogFragment() {

    internal lateinit var listener : AddMoveDialogListener
    lateinit var newMove: Move
    lateinit var pokemon: Pokemon
    var decision = false

    interface AddMoveDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        newMove = arguments?.getSerializable("newMove") as Move
        pokemon = arguments?.getSerializable("pokemon") as Pokemon
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
                .setTitle("Learn a move")
                .setMessage("Do you want to learn the move ${newMove.name}?")
                .setPositiveButton(R.string.yes,
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the positive button event back to the host activity
                        //listener.onDialogPositiveClick(this)
                        decision = true
                    })
                .setNegativeButton(R.string.no,
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the negative button event back to the host activity
                        //listener.onDialogNegativeClick(this)
                        decision = false
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface){
        super.onDismiss(dialog)
        setFragmentResult("newMove", bundleOf("newMove" to newMove, "pokemon" to pokemon, "decision" to decision))
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the AddMoveDialogListener so we can send events to the host
            listener = context as AddMoveDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement AddMoveDialogListener"))
        }
    }

}
