package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class CashRegisterTest {
    @Test
    fun `expected change successful`() {
        val cashInRegister = Change()
            .add(Coin.TWO_EURO, 5)
            .add(Bill.TEN_EURO, 4)
            .add(Coin.FIFTY_CENT, 3)
            .add(Coin.TWENTY_CENT, 2)

        val price = 1_00L
        val amountPaid = Change()
            .add(Coin.TWO_EURO, 1)

        val expectedChange = Change()
            .add(Coin.FIFTY_CENT,2)

        val cashRegister = CashRegister(cashInRegister)
        val actualChange = cashRegister.performTransaction(price,amountPaid)
        println(actualChange)

        assertEquals(expectedChange, actualChange)
    }

    @Test
    fun `amount paid less than price`() {
        val cashInRegister = Change()
            .add(Coin.TWO_EURO, 5)
            .add(Bill.TEN_EURO, 4)
            .add(Coin.FIFTY_CENT, 3)
            .add(Coin.TWENTY_CENT, 2)

        val price = 3_00L

        val amountPaid = Change()
            .add(Coin.TWO_EURO, 1)

        val cashRegister = CashRegister(cashInRegister)

        val exception = assertThrows(CashRegister.TransactionException::class.java) {
            cashRegister.performTransaction(price,amountPaid)
        }

        assertEquals(CashRegister.AMOUNT_LESS_THAN_PRICE, exception.message)
    }

    @Test
    fun `insufficient change in cash register`() {
        val cashInRegister = Change()
            .add(Bill.TEN_EURO, 4)
            .add(Coin.FIFTY_CENT, 1)
            .add(Coin.TWENTY_CENT, 2)

        val price = 2_00L

        val amountPaid = Change()
            .add(Bill.FIVE_EURO, 1)

        val cashRegister = CashRegister(cashInRegister)

        val exception = assertThrows(CashRegister.TransactionException::class.java) {
            cashRegister.performTransaction(price,amountPaid)
        }

        assertEquals(CashRegister.INSUFFICIENT_CHANGE, exception.message)
    }
}
