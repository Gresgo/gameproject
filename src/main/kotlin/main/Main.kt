package main

import engine.io.Window

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
        window.setBackgroundColor(1F, 0F, 0F)
        window.run()
    }
}