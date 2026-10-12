package com.cse310.studentgradetracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform