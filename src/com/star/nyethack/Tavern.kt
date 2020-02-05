package com.star.nyethack

import com.star.nyethack.extensions.random
import java.io.File

const val TAVERN_NAME = "Taernyl's Folly"
const val CASK = 5
const val PINTS_ONE_GALLON = 1 / .125
const val GOLDS_ONE_DRAGON_COIN = 1.43

var playerGold = 10
var playerSilver = 10
var playerDragonCoin = 5.0

var remainingQuantityOfDragonBreathInPints = (PINTS_ONE_GALLON * CASK).toInt()

val patronList = mutableListOf("Eli", "Mordoc", "Sophie")
val lastName = listOf("Ironfoot", "Fernsworth", "Baggins")
val uniquePatrons = mutableSetOf<String>()
val menuList = File("data/tavern-menu-items.txt").readText().split("\n")

val patronGold = mutableMapOf<String, Double>()

fun main(args: Array<String>) {

    println("Number of pints left in the cask: $remainingQuantityOfDragonBreathInPints")

    println(patronList)
    patronList.remove("Eli")
    patronList.add("Alex")
    patronList.add(0, "Alex")
    patronList[0] = "Alexis"
    println(patronList)

    if (patronList.contains("Eli")) {
        println("The tavern master says: Eli's in the back playing cards.")
    } else {
        println("The tavern master says: Eli isn't here.")
    }

    if (patronList.containsAll(listOf("Sophie", "Mordoc"))) {
        println("The tavern master says: Yea, they're seated by the stew kettle.")
    } else {
        println("The tavern master says: Nay, they departed hours ago.")
    }

    for (patron in patronList) {
        println("Good evening, $patron")
    }

    patronList.forEach { patron ->
        println("Good evening, $patron")
    }

    patronList.forEachIndexed { index, patron ->
        println("Good evening, $patron - you're #${index + 1} in line.")
        println("Welcome, $patron".frame(5))
    }

    menuList.forEachIndexed { index, data ->
        println("$index : $data")
    }

    repeat(10) {
        val first = patronList.random()
        val last = lastName.random()
        val name = "$first $last"
        uniquePatrons.add(name)
    }

    uniquePatrons.forEach { name ->
        patronGold[name] = 6.0
    }

    displayPatronBalances()

    var orderCount = 0

    while (orderCount <= 9) {

        if (uniquePatrons.isEmpty()) {
            break
        }

        placeOrder(
            uniquePatrons.random(),
            menuList.random()
        )
        orderCount++
    }

    displayFormattedTavernMenu(menuList)
    displayAdvancedFormattedTavernMenu(menuList)

    println(patronGold)

    displayPatronBalances()
}

private fun placeOrder(patronName: String, menuData: String) {

    val indexOfApostrophe = TAVERN_NAME.indexOf('\'')
    val tavernMaster = TAVERN_NAME.substring(0 until indexOfApostrophe)

    println("$patronName speaks with $tavernMaster about their order.")

    val (type, name, price) = menuData.split(',')
    val message = "$patronName buys a $name ($type) for $price"

    println(message)

    performPurchase(price.toDouble(), name, patronName)
}

//private fun toDragonSpeak(phrase: String) = phrase.replace(Regex("[aeiouAEIOU]")) {
//
//    when (it.value.toLowerCase()) {
//
//        "a" -> "4"
//        "e" -> "3"
//        "i" -> "1"
//        "o" -> "0"
//        "u" -> "|_|"
//        else -> it.value
//    }
//}

private fun String.toDragonSpeak() = this.replace(Regex("[aeiouAEIOU]")) {

    when (it.value.toLowerCase()) {

        "a" -> "4"
        "e" -> "3"
        "i" -> "1"
        "o" -> "0"
        "u" -> "|_|"
        else -> it.value
    }
}

private fun String.frame(padding: Int, formatChar: String = "*"): String {

    val greeting = "$this!"
    val middle = formatChar.padEnd(padding)
        .plus(greeting)
        .plus(formatChar.padStart(padding))
    val end = (middle.indices).joinToString("") { formatChar }

    return "$end\n$middle\n$end"
}

fun performPurchase(price: Double, name: String, patronName: String) {

//    displayBalance()

//    val totalPurse = playerGold + (playerSilver / 100.0)
//    val totalPurse = GOLDS_ONE_DRAGON_COIN * playerDragonCoin
    val totalPurse = patronGold.getValue(patronName)

    println("Total purse: ${"%.2f".format(totalPurse)}")
    println("Purchasing item for $price")

    if (totalPurse < price) {
        println("The customer is short on gold.")
        uniquePatrons.remove(patronName)
        patronGold.remove(patronName)
        return
    }

//    val remainingBalance = totalPurse - price
//
//    println("Remaining balance: ${"%.2f".format(remainingBalance)}")
//
//    val remainingGold = remainingBalance.toInt()
//    val remainingSilver = (remainingBalance % 1 * 100).roundToInt()
//    val remainingDragonCoin = remainingBalance / GOLDS_ONE_DRAGON_COIN
//
//    playerGold = remainingGold
//    playerSilver = remainingSilver
//    playerDragonCoin = remainingDragonCoin

    patronGold[patronName] = totalPurse - price

    val phrase = if (name == "Dragon's Breath") {
        remainingQuantityOfDragonBreathInPints--
        "$patronName exclaims: ${("Ah, delicious $name!").toDragonSpeak()}" + "\n" +
                "$patronName exclaims: ${("DRAGON'S BREATH: IT'S GOT WHAT ADVENTURERS CRAVE!").toDragonSpeak()}"
    } else {
        "$patronName says: Thanks for the $name"
    }

    println(phrase)

//    displayBalance()
}

fun displayBalance() {

//    println("Player's purse balance: Gold: $playerGold , Silver: $playerSilver")
    println("Player's purse balance: DragonCoin: ${"%.4f".format(playerDragonCoin)}")
}

fun displayPatronBalances() {

    patronGold.forEach { (patron, balance) ->
        println("$patron, balance: ${"%.2f".format(balance)}")
    }
}

fun displayFormattedTavernMenu(menuList: List<String>) {

    val welcome = "*** Welcome to Taernyl's Folly ***"

    println(welcome)
    println()

    menuList.forEach { menuData ->

        val (_, name, price) = menuData.split(',')
        val menuItem = capitalizeEachWord(name) + ".".repeat(welcome.length - name.length - price.length) + price

        println(menuItem)
    }
}

fun displayAdvancedFormattedTavernMenu(menuList: List<String>) {

    val welcome = "*** Welcome to Taernyl's Folly ***"

    println(welcome)
    println()

    val typeSet = mutableSetOf<String>()
    val menuMap = mutableMapOf<String, MutableList<String>>()

    menuList.forEach { menuData ->

        val (type, name, price) = menuData.split(',')
        val menuItem = capitalizeEachWord(name) + ".".repeat(welcome.length - name.length - price.length) + price
        val typeItem = " ".repeat((welcome.length - "~[$type]~".length) / 2) + "~[$type]~"

        if (type !in typeSet) {
            typeSet.add(type)
            menuMap[typeItem] = mutableListOf(menuItem)
        } else {
            menuMap[typeItem]?.add(menuItem)
        }
    }

    menuMap.forEach { item ->
        println(item.key)
        item.value.forEach(::println)
    }
}

private fun capitalizeEachWord(words: String): String {

    val answerList = mutableListOf<String>()

    words.split(" ").forEach { word ->
        answerList.add(word.capitalize())
    }

    return answerList.joinToString(" ")
}