package org.hyperskill.app

import ru.nobird.app.core.model.PaginationDirection

class Greeting {
    fun greeting(): String {
        val array = longArrayOf(1, 2, 2).distinct()
        
        return "Hello, ${PaginationDirection.NEXT} ${array.joinToString()} ${Platform().platform}!"
    }
}