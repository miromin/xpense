package com.xpense.android.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xpense.android.data.TransactionRepository

class TransactionsViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val transactions = transactionRepository.observeTransactions()

    /**
     * Variable that tells the Fragment to navigate to a specific
     * [com.xpense.android.ui.addedittransaction.AddEditTransactionFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private val _navigateToCreateTransaction = MutableLiveData<Boolean>()

    /**
     * If this is true, immediately navigate to [com.xpense.android.ui.addedittransaction.AddEditTransactionFragment]
     * and call [doneNavigating]
     */
    val navigateToCreateTransaction: LiveData<Boolean>
        get() = _navigateToCreateTransaction

    /**
     * Call this immediately after navigating to [com.xpense.android.ui.addedittransaction.AddEditTransactionFragment]
     *
     * It will clear the navigation request, so if the device is rotated it won't navigate twice.
     */
    fun doneNavigating() {
        _navigateToCreateTransaction.value = false
    }

    fun navigateToCreateTransaction() {
        _navigateToCreateTransaction.value = true
    }

    @Suppress("UNCHECKED_CAST")
    class TransactionsViewModelFactory (
        private val transactionRepository: TransactionRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            TransactionsViewModel(transactionRepository) as T
    }
}
