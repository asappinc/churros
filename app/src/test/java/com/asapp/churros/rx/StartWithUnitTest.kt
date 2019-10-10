package com.asapp.churros.rx

import io.reactivex.Observable
import org.junit.Test

class StartWithUnitTest {
    @Test
    fun testWhenTrue() {
        val testObserver = Observable.just(1, 2)
            .startWith(0, condition = true)
            .test()

        testObserver.assertValues(0, 1, 2)
    }

    @Test
    fun testWhenFalse() {
        val testObserver = Observable.just(1, 2)
            .startWith(0, condition = false)
            .test()

        testObserver.assertValues(1, 2)
    }
}