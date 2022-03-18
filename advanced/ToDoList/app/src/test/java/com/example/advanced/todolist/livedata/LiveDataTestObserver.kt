package com.example.advanced.todolist.livedata

import androidx.lifecycle.Observer
import java.lang.AssertionError
import kotlin.math.exp

class LiveDataTestObserver<T> : Observer<T> {

    private val values: MutableList<T> = mutableListOf()

    override fun onChanged(t: T) {      // 값이 바뀔 때마다
        values.add(t)
    }

    fun assertValueSequence(sequence: List<T>): LiveDataTestObserver<T> {
        var i = 0
        val actualIterator = values.iterator()
        val expectedIterator = sequence.iterator()

        var actualNext: Boolean
        var expectedNext: Boolean

        while (true) {      // 순서가 같은지 판단
            actualNext = actualIterator.hasNext()
            expectedNext = expectedIterator.hasNext()

            if (!actualNext || !expectedNext) break     // 둘 중 하나라도 값이 바뀌지 않았다면 break

            //  actual 값과 expected 값이 모두 변경되었을 시
            val actual: T = actualIterator.next()
            val expected: T = expectedIterator.next()

            if (actual != expected) {       // actual 값과 expected 값이 같지 않다면
                throw AssertionError("actual: ${actual}, expected: ${expected}, index: $i")     // 에러
            }
            i++     //  같다면 순회하면서 돌다가
        }

        if (actualNext) {
            throw AssertionError("More values received than expected ($i)")
        }
        if (expectedNext) {
            throw AssertionError("Fewer values received than expected ($i)")
        }

        // 에러가 없을 시 Test Success
        return this
    }
}