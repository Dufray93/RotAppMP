package com.aplimovil.rotappmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform