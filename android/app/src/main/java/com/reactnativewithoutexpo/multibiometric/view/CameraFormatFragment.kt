package com.reactnativewithoutexpo.multibiometric.view

import android.R
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.neurotec.media.NMediaFormat
import com.neurotec.samples.view.BaseDialogFragment
import com.reactnativewithoutexpo.multibiometric.Model

class CameraFormatFragment  // ===========================================================
// Private constructor
// ===========================================================
    : BaseDialogFragment() {
    // ===========================================================
    // Public types
    // ===========================================================
    interface CameraFormatSelectionListener {
        fun onCameraFormatSelected(format: NMediaFormat?)
    }

    // ===========================================================
    // Private fields
    // ===========================================================
    private var mListView: android.widget.ListView? = null
    private var mListener: CameraFormatSelectionListener? = null
    private var mItems: List<String>? = null

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
            mListener = activity as CameraFormatSelectionListener
        } catch (e: java.lang.ClassCastException) {
            throw java.lang.ClassCastException("$activity must implement CameraFormatSelectionListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: android.os.Bundle?): android.app.Dialog {
        val builder = android.app.AlertDialog.Builder(getActivity())
        val inflater: LayoutInflater = getActivity().getLayoutInflater()
        val view: android.view.View = inflater.inflate(R.layout.fragment_list, null)
        mItems = getArguments().getStringArrayList(EXTRA_FORMATS)
        mListView = view.findViewById<android.view.View>(R.id.list) as android.widget.ListView
        mListView!!.adapter = ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, mItems)
        mListView!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: android.view.View, position: Int, id: Long) {
                for (format in Model.getInstance().getClient().getFaceCaptureDevice().getFormats()) {
                    if (format.toString() == mItems!![position]) {
                        mListener!!.onCameraFormatSelected(format)
                        break
                    }
                }
                dismiss()
            }
        })

        builder.setView(view)
        builder.setTitle(R.string.msg_camera_formats)
        return builder.create()
    }

    companion object {
        // ===========================================================
        // Private static fields
        // ===========================================================
        private const val EXTRA_FORMATS = "formats"

        // ===========================================================
        // Public static methods
        // ===========================================================
        fun newInstance(): android.app.DialogFragment {
            val records = java.util.ArrayList<String>()
            for (format in Model.getInstance().getClient().getFaceCaptureDevice().getFormats()) {
                records.add(format.toString())
            }

            val fragment = CameraFormatFragment()
            val bundle = android.os.Bundle()
            bundle.putStringArrayList(EXTRA_FORMATS, records)
            fragment.setArguments(bundle)
            return fragment
        }
    }
}