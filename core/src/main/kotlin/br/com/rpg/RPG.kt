package br.com.rpg

import br.com.rpg.screen.GameScreen
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import ktx.app.KtxGame
import ktx.app.KtxScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class RPG : KtxGame<KtxScreen>(){


    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }

    companion object{
        const val UNIT_SCALE = 1/16f
    }
}
