package com.xpense.android.data

import androidx.lifecycle.LiveData

// TODO: consider moving to data.source package
interface TransactionDataSource {

    fun observeTransactions(): LiveData<Result<List<Transaction>>>

    suspend fun saveTransaction(transaction: Transaction)

    suspend fun getTransaction(transactionId: Long): Result<Transaction>

    suspend fun getTransactions(): Result<List<Transaction>>

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun flagTransaction(transactionId: Long, flagged: Boolean)

    suspend fun deleteAllTransactions()
}
