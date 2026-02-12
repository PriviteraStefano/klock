package org.stefanoprivitera.klock

class CounterViewModel {
    private var count: Int = 0

    fun increment() {
        count++
    }

    fun decrement() {
        count--
    }

    fun getCount(): Int {
        return count
    }

    fun reset() {
        count = 0
    }
}
