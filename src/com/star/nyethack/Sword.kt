package com.star.nyethack


class Sword(_name: String) {

    var name = _name
        get() = "The Legendary $field"
        set(value) {
            field = value.toLowerCase().reversed().capitalize()
        }

    init {
        name = _name
    }
}

fun main() {

    val sword = Sword("Excalibur")

    println(sword.name)

    sword.name = "Gleipnir"

    println(sword.name)
}