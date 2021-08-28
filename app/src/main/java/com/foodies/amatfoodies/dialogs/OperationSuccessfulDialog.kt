package com.foodies.amatfoodies.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.databinding.OperationSuccessfulDialogBinding
import java.io.Serializable

class OperationSuccessfulDialog : DialogFragment() {
    private lateinit var binding: OperationSuccessfulDialogBinding
    private var successMsg: String? = null
    private var successButtonTxt: String? = null
    private var callback: (() -> Unit)? = null

    companion object {
        private const val SUCCESS_MSG_KEY = "successful_msg_key"
        private const val SUCCESS_BUTTON_TXT_KEY = "successful_button_key"
        private const val SUCCESS_BUTTON_CALLBACK_KEY = "successful_callback_key"

        @JvmStatic
        fun newInstance(
            successMsg: String? = null,
            successButtonTxt: String? = null,
            callback: (() -> Unit)? = null
        ) =
            OperationSuccessfulDialog().apply {
                arguments = Bundle().apply {
                    putString(SUCCESS_MSG_KEY, successMsg)
                    putString(SUCCESS_BUTTON_TXT_KEY, successButtonTxt)
                    putSerializable(SUCCESS_BUTTON_CALLBACK_KEY, callback as Serializable)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_dialog)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.operation_successful_dialog,
            container,
            false
        )

        setupUI()

        return binding.root
    }

    private fun setupUI() {
        binding.successfulOpTxt.text = successMsg
        binding.successfulOpBtn.text = successButtonTxt
        binding.successfulOpBtn.setOnClickListener {
            dismiss()
            callback?.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(SUCCESS_MSG_KEY)?.let { successMsg = it }
        arguments?.getString(SUCCESS_BUTTON_TXT_KEY)?.let { successButtonTxt = it }
        arguments?.getSerializable(SUCCESS_BUTTON_CALLBACK_KEY)?.let { callback = it as () -> Unit }
    }
}