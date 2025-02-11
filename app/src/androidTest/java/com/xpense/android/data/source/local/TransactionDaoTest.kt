package com.xpense.android.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.xpense.android.data.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TransactionDaoTest {

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TransactionDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TransactionDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTransactionAndGetById() = runBlockingTest {
        // GIVEN - Insert a transaction
        val txn = Transaction(transactionId = 1, amount = 12.34, description = "description")
        database.transactionDao().insert(txn)

        // WHEN - Get the transaction by id from the database
        val loaded = database.transactionDao().getTransactionWithId(txn.transactionId)

        // THEN - The loaded data contains the expected values
        assertThat(loaded as Transaction, notNullValue())
        assertThat(loaded.transactionId, `is`(txn.transactionId))
        assertThat(loaded.amount, `is`(txn.amount))
        assertThat(loaded.description, `is`(txn.description))
    }

    @Test
    fun updateTransactionAndGetById() = runBlockingTest {
        // GIVEN - Insert a transaction into the DAO
        val txn = Transaction(transactionId = 1, amount = 12.34, description = "description")
        database.transactionDao().insert(txn)

        // WHEN - Update transaction by creating new transaction with same ID but
        // different attributes, and get by id
        val txnNew = txn.copy(description = "updated")
        database.transactionDao().update(txnNew)
        val loaded = database.transactionDao().getTransactionWithId(txn.transactionId)

        // THEN - The loaded data contains the updated values
        assertThat(loaded as Transaction, notNullValue())
        assertThat(loaded.transactionId, `is`(txn.transactionId))
        assertThat(loaded.amount, `is`(txn.amount))
        assertThat(loaded.description, `is`(txnNew.description))
    }
}
