package com.asapp.churros

import io.reactivex.Single
import org.junit.Test

class RunIfUnitTest {
    @Test
    fun testRunIf() {
        Single.just(1)
            .runIf(condition = true) {
                map { 2 * it }
            }
            .test()
            .assertValue(2)

        Single.just(1)
            .runIf(condition = false) {
                map { 2 * it }
            }
            .test()
            .assertValue(1)
    }
}