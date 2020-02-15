package com.asapp.churros

/**
 * exhaustive is meant to be used in combination with `when` to ensure all options are considered.
 */
fun Any.exhaustive() {}

/**
 * runIf preserves the chain in fluent APIs such as RxJava or the Builder pattern.
 */
fun <T> T.runIf(condition: Boolean, block: T.() -> T): T {
    return if (condition) {
        block()
    } else {
        this
    }
}