package com.example.mobile_mangli

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform