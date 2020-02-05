package com.star.nyethack.extensions


fun <T> Iterable<T>.random(): T = this.shuffled().first()

