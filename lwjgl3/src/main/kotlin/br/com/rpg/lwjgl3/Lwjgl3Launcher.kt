@file:JvmName("Lwjgl3Launcher")

package br.com.rpg.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import br.com.rpg.RPG

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(RPG(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("RPG")
        setWindowedMode(640, 480)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
