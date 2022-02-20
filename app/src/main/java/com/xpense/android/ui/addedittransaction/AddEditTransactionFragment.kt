package com.xpense.android.ui.addedittransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.xpense.android.XpenseApplication
import com.xpense.android.databinding.FragmentAddEditTransactionBinding

class AddEditTransactionFragment : Fragment() {

    private val _viewModelAddEdit by viewModels<AddEditTransactionViewModel> {
        AddEditTransactionViewModel.AddEditTransactionViewModelFactory(
            AddEditTransactionFragmentArgs.fromBundle(requireArguments()).transactionId,
            (requireContext().applicationContext as XpenseApplication).transactionRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddEditTransactionBinding.inflate(inflater)

        binding.viewModel = _viewModelAddEdit

        // Make data binding lifecycle aware, to automatically update layout with LiveData
        binding.lifecycleOwner = this

        _viewModelAddEdit.navigateExit.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack()
                _viewModelAddEdit.doneNavigating()
            }
        }

        binding.composeView.setContent {
            MdcTheme {
                InputForm()
            }
        }

        return binding.root
    }
}

@Composable
fun InputForm() {
    val viewModel: AddEditTransactionViewModel = viewModel()

    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                viewModel.amount = it
            },
            label = { Text("Amount") }
        )
        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                viewModel.description = it
            },
            label = { Text("Description") }
        )
    }
}
