package client.main

import client.engine.io.Window

fun main() {
    Main().start()
}

class Main: Runnable {

    lateinit var game: Thread

    fun start() {
        game = Thread(this, "game")
        game.start()
    }

    override fun run() {
        val window = Window.get()
        window.setBackgroundColor(0F, 0.75F, 1F)
        window.start()
    }
}