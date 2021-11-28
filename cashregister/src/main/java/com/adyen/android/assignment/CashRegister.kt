package com.adyen.android.assignment

import com.adyen.android.assignment.money.Change

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {
    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change {

        if(price > amountPaid.total) throw TransactionException(AMOUNT_LESS_THAN_PRICE)

        var customerChange = Change()
        var totalChangeToGive = amountPaid.total - price

        //add the amountPaid to the Cash Register
        change.addChange(amountPaid)

        return when{

            totalChangeToGive > change.total -> {
                change.removeChange(amountPaid)
                throw TransactionException(INSUFFICIENT_CHANGE)
            }

            totalChangeToGive == change.total -> {
                customerChange = change
                change.clear()
                customerChange
            }

            else -> {
                calculateChange(amountPaid,totalChangeToGive,customerChange)
            }

        }
    }

    private fun calculateChange(
        amountPaid: Change,
        totalChangeToGiveParam: Long,
        customerChange: Change
    ):Change{
        var totalChangeToGive = totalChangeToGiveParam
        change.getElements().forEach {
            if(totalChangeToGive>=it.minorValue){
                val changeCountPossible = (totalChangeToGive / it.minorValue).toInt()
                val changeCountAvailable = change.getCount(it)

                val totalChangeCount: Int =
                    if (changeCountAvailable <= changeCountPossible) changeCountAvailable else changeCountPossible
                val totalChangeAmount: Long = (totalChangeCount * it.minorValue).toLong()

                customerChange.add(it,totalChangeCount)
                totalChangeToGive -= totalChangeAmount
            }

        }

        if(totalChangeToGive>0) {
            change.removeChange(amountPaid)
            throw TransactionException(INSUFFICIENT_CHANGE)
        }

        change.removeChange(customerChange)
        return customerChange
    }

    class TransactionException(message: String, cause: Throwable? = null) : Exception(message, cause)

    companion object{
        const val AMOUNT_LESS_THAN_PRICE = "Amount paid less than price"
        const val INSUFFICIENT_CHANGE = "Insufficient Change in drawer"
    }
}
