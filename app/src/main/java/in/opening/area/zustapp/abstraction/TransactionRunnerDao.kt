package `in`.opening.area.zustapp.abstraction

import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Transaction

@Dao
interface  TransactionRunnerDao : TransactionRunner {

    @Transaction
    suspend fun runInTransaction(tx: suspend () -> Unit) = tx()

    @Ignore
    override suspend operator fun invoke(tx: suspend () -> Unit) {
        runInTransaction(tx)
    }

}

interface TransactionRunner {
    suspend operator fun invoke(tx: suspend () -> Unit)
}

