import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Prints grid of values
 */
fun Map<Int, Map<Int, *>>.print() {
    for (x in 0 until this.size) {
        for (y in 0 until this[x]!!.size) {
            print("${this[x]!![y]!!}")
        }
        println("")
    }
}
