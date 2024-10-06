package com.reactnativewithoutexpo.multibiometric.view

import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import com.neurotec.samples.view.BaseDialogFragment
import com.reactnativewithoutexpo.R

class EnrollmentDialogFragment  // ===========================================================
// Public constructor
// ===========================================================
    : BaseDialogFragment() {
    // ===========================================================
    // Public types
    // ===========================================================
    interface EnrollmentDialogListener {
        fun onEnrollmentIDProvided(id: String?)
    }

    // ===========================================================
    // Private fields
    // ===========================================================
    private var mListener: EnrollmentDialogListener? = null
    private var mEditText: EditText? = null

    // ===========================================================
    // Public methods
    // ===========================================================
    override fun onAttach(activity: android.app.Activity) {
        super.onAttach(activity)
        try {
            mListener = activity as EnrollmentDialogListener
        } catch (e: java.lang.ClassCastException) {
            throw java.lang.ClassCastException("$activity must implement EnrollmentDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: android.os.Bundle?): android.app.Dialog {
        val builder = android.app.AlertDialog.Builder(getActivity())
        val inflater: LayoutInflater = getActivity().getLayoutInflater()
        val view: android.view.View = inflater.inflate(R.layout.fragment_enrollment, null)
        mEditText = view.findViewById<android.view.View>(R.id.enrollment_id) as EditText
        mEditText!!.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: android.widget.TextView,
                actionId: Int,
                event: android.view.KeyEvent
            ): Boolean {
                if (event != null && (event.keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                    val `in` =
                        getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                    `in`.hideSoftInputFromWindow(
                        mEditText!!.getApplicationWindowToken(),
                        android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    return mEditText!!.getText().toString() != ""
                }
                return false
            }
        })

        builder.setView(view)
        builder.setTitle(R.string.msg_enter_id)
        builder.setPositiveButton(getString(R.string.msg_enroll), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id: Int) {
                mListener!!.onEnrollmentIDProvided(mEditText!!.getText().toString().trim { it <= ' ' })
            }
        })
        builder.setNegativeButton(getString(R.string.msg_cancel), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id: Int) {
                dialog.cancel()
            }
        })
        return builder.create()
    }
}