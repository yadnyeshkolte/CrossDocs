package com.yadnyeshkolte.crossdocs

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform