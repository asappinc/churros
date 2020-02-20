package com.asapp.churros.rx

import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class RetryWithDelayUnitTest {
    private lateinit var testScheduler: TestScheduler
    private val timeUnit = TimeUnit.SECONDS
    private val monotonicDelayList = listOf(100, 250, 5000)

    @Before
    fun setup() {
        testScheduler = TestScheduler()
    }

    private fun createSingle(numberOfErrors: Int, success: Int): Single<Int> {
        var count = numberOfErrors
        return Single
            .fromCallable {
                if (count > 0) {
                    count--

                    throw IllegalStateException("Fail")
                } else {
                    success
                }
            }
    }

    @Test
    fun testSuccessfulWithoutRetries() {
        val successValue = 1557
        val exponentialDelayList = listOf(1, 2, 4, 8)
        val testObserver = Single.just(successValue)
            .retryWithDelay(exponentialDelayList, timeUnit, testScheduler)
            .test()

        // testScheduler is NEVER set to anything so the retry was not needed.
        testObserver.assertValue(successValue)
    }

    @Test
    fun testSuccessfulRetryWithDelay() {
        val constantDelay = 3
        val numberOfErrors = 4
        val constantDelayList = generateSequence { constantDelay }.take(numberOfErrors).toList()
        val successValue = 42

        val testObserver = createSingle(numberOfErrors, successValue)
            .retryWithDelay(constantDelayList, timeUnit, testScheduler)
            .test()

        repeat(numberOfErrors) {
            testObserver.assertEmpty()
            testScheduler.advanceTimeBy(constantDelay.toLong(), timeUnit)
        }
        testObserver.assertValue(successValue)
    }

    @Test
    fun testTooManyRetries() {
        val numberOfErrors = 40

        val testObserver = createSingle(numberOfErrors, 2001)
            .retryWithDelay(monotonicDelayList, timeUnit, testScheduler)
            .test()

        for (delay in monotonicDelayList) {
            testObserver.assertEmpty()
            testScheduler.advanceTimeBy(delay.toLong(), timeUnit)
        }
        testObserver.assertError {
            it.message == "Too many retries" &&
                    it.cause is IllegalStateException &&
                    it.cause?.message == "Fail"
        }
    }

    @Test
    fun testCallback() {
        val throwable = Throwable("Fail")
        val testObserver = Single.error<Int>(throwable)
            .retryWithDelay(monotonicDelayList, timeUnit, testScheduler) {
                it == throwable
            }
            .test()

        testObserver.assertError {
            it.message == "Too many retries" && it.cause == throwable
        }
    }
}