import java.io.File

abstract class Solution {

    val inputFile = File("src/${this::class.simpleName!!.lowercase()}/input.txt")

    abstract fun compute()

}