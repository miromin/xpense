package com.xpense.android.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xpense.android.R
import com.xpense.android.databinding.FragmentTransactionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment : Fragment() {

    // use Koin to retrieve the ViewModel instance
    private val _viewModel: TransactionViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentTransactionBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_transaction, container, false)

        binding.viewModel = _viewModel

        binding.lifecycleOwner = this

        _viewModel.navigateExit.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack()
                _viewModel.doneNavigating()
            }
        }

        val args = TransactionFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "${args.transactionId}", Toast.LENGTH_SHORT).show()
        if (args.transactionId == 0L) {
            // create new transaction - default empty fields
        } else {
            // edit existing transaction - pre-populate fields
        }

        return binding.root
    }
}
