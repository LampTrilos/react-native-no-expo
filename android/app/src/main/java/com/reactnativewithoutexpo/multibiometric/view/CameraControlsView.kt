package com.reactnativewithoutexpo.multibiometric.view

import android.view.LayoutInflater
import android.widget.LinearLayout
import com.reactnativewithoutexpo.R

class CameraControlsView(context: android.content.Context, listener: CameraControlsListener) : LinearLayout(context) {
    // ===========================================================
    // Public types
    // ===========================================================
    interface CameraControlsListener {
        fun onSwitchCamera()
        fun onChangeFormat()
    }

    // ===========================================================
    // Private fields
    // ===========================================================
    private val mListener: CameraControlsListener

    // ===========================================================
    // Public constructor
    // ===========================================================
    init {
        if (listener == null) throw java.lang.NullPointerException("listener")
        mListener = listener
        val mInflater: LayoutInflater =
            context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: android.view.View = mInflater.inflate(R.layout.camera_controls, this, true)
        val switchCamera = view.findViewById<android.view.View>(R.id.switch_camera) as android.widget.ImageView
        switchCamera.setOnClickListener { mListener.onSwitchCamera() }

        val changeFormat = view.findViewById<android.view.View>(R.id.change_format) as android.widget.ImageView
        changeFormat.setOnClickListener { mListener.onChangeFormat() }
    }
}