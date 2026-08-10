@file:JvmName("Log")

package android.util

fun e(tag: String, msg: String, t: Throwable): Int {
    println("ERROR: $tag: $msg")
    return 0
}

fun e(tag: String, msg: String): Int {
    println("ERROR: $tag: $msg")
    return 0
}

fun w(tag: String, msg: String): Int {
    println("WARN: $tag: $msg")
    return 0
}

fun isLoggable(tag: String, level: Int): Boolean {
    return true
}

fun println(priority: Int, tag: String, msg: String): Int {
    println("$priority: $tag: $msg")
    return 1
}