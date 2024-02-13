package ru.taratonov.conveyor.exception

class IllegalArgumentOfEnumException() : RuntimeException() {
    private lateinit var values: List<String>

    private constructor(values: List<String>) : this() {
        this.values = values
    }

    companion object {
        fun createWith(values: List<String>) = IllegalArgumentOfEnumException(values)
    }

    private fun makeMessage(values: List<String>) =
        StringBuilder()
            .append("Illegal argument, must be one of: ")
            .append(values.joinToString(", "))
            .toString()

    override val message: String
        get() = makeMessage(values)
}