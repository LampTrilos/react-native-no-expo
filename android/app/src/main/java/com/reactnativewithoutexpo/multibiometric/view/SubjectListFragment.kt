package com.reactnativewithoutexpo.multibiometric.view

import com.reactnativewithoutexpo.R
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.neurotec.samples.view.BaseDialogFragment
import com.neurotec.biometrics.NSubject
import java.util.ArrayList

class SubjectListFragment  // ===========================================================
// Private constructor
// ===========================================================
    : BaseDialogFragment() {
    // ===========================================================
    // Public types
    // ===========================================================
    interface SubjectSelectionListener {
        fun onSubjectSelected(subject: NSubject?, bundle: android.os.Bundle?)
    }

    // ===========================================================
    // Private fields
    // ===========================================================
    private var mSubjectListView: android.widget.ListView? = null
    private var mListener: SubjectSelectionListener? = null
    private var mItems: MutableList<String>? = null

    // ===========================================================
    // Public methods
    // ===========================================================
    override fun onCreate(bundle: android.os.Bundle?) {
        super.onCreate(bundle)
        setCancelable(true)
        setStyle(android.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light)
    }

    override fun onAttach(activity: android.app.Activity) {
        super.onAttach(activity)
        try {
            mListener = activity as SubjectSelectionListener
        } catch (e: java.lang.ClassCastException) {
            throw java.lang.ClassCastException("$activity must implement SubjectSelectionListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: android.os.Bundle?): android.app.Dialog {
        val builder = android.app.AlertDialog.Builder(getActivity())
        val inflater: LayoutInflater = getActivity().getLayoutInflater()
        val view: android.view.View = inflater.inflate(R.layout.fragment_list, null)
        mItems = java.util.ArrayList()
        for (subject in mSubjects) {
            mItems.add(subject.getId())
        }
        if (mItems.isEmpty()) {
            mItems.add(getResources().getString(R.string.msg_no_records_in_database))
        }
        mSubjectListView = view.findViewById<android.view.View>(R.id.list) as android.widget.ListView
        mSubjectListView!!.adapter =
            ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mItems as ArrayList<String>)
        mSubjectListView!!.isEnabled = getArguments().getBoolean(EXTRA_ENABLED)
        mSubjectListView!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: android.view.View, position: Int, id: Long) {
                for (subject in mSubjects) {
                    if (subject.getId() == (mItems as ArrayList<String>).get(position)) {
                        mListener!!.onSubjectSelected(subject, getArguments())
                        break
                    }
                }
                dismiss()
            }
        })

        builder.setView(view)
        builder.setTitle(R.string.msg_database)
        return builder.create()
    }

    companion object {
        // ===========================================================
        // Private static fields
        // ===========================================================
        private const val EXTRA_ENABLED = "enabled"

        private lateinit var mSubjects: Array<NSubject>

        // ===========================================================
        // Public static methods
        // ===========================================================
        fun newInstance(
            subjects: Array<NSubject>,
            enabled: Boolean,
            bundle: android.os.Bundle
        ): android.app.DialogFragment {
            mSubjects = subjects
            bundle.putBoolean(EXTRA_ENABLED, true)
            val fragment = SubjectListFragment()
            fragment.setArguments(bundle)
            return fragment
        }
    }
}