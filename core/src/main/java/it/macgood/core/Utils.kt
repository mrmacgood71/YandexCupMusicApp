package it.macgood.core

object Utils {
    fun priceDelimiter(price: String): String {
        if (price.length == 4) {
            return price[0] + " " + price.subSequence(1..3)
        }
        return price.chunked(3) { it }.joinToString(" ")
    }
}