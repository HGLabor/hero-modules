package gg.norisk.ffa.utils

inline fun <reified T: Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}

inline fun <reified T: Enum<T>> T.previous(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal - 1  + values.size) % values.size
    return values[nextOrdinal]
}
