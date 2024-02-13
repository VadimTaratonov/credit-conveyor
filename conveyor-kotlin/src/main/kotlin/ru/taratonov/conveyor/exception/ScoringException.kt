package ru.taratonov.conveyor.exception

class ScoringException() : RuntimeException() {
    private lateinit var exceptions: List<String>

    companion object {
        fun createWith(exceptions: List<String>) = ScoringException(exceptions)
    }

    private constructor(exceptions: List<String>) : this() {
        this.exceptions = exceptions
    }

    private fun makeMessage(exceptions: List<String>) = exceptions.joinToString(", ")

    override val message: String
        get() = makeMessage(exceptions)
}