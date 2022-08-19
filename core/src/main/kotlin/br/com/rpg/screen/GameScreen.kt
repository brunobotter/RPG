package br.com.rpg.screen

import br.com.rpg.component.ImageComponent.Companion.ImageComponentListener
import br.com.rpg.component.PhysicComponent
import br.com.rpg.component.PhysicComponent.*
import br.com.rpg.component.PhysicComponent.Companion.PhysicComponentListener
import br.com.rpg.event.MapChangeEvent
import br.com.rpg.event.fire
import br.com.rpg.system.*
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.World
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2

class GameScreen : KtxScreen {

    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
    private val textureAtlas = TextureAtlas("graphics/game.atlas")
    private var currentMap: TiledMap? = null
    //no nosso caso nao ha gravidade
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }
    private val eWorld: World = World {
        inject(stage)
        inject(textureAtlas)
        inject(phWorld)
        componentListener<ImageComponentListener>()
        componentListener<PhysicComponentListener>()
        system<EntitySpawSystem>()
        system<PhysicSystem>()
        system<AnimationSystem>()
        system<RenderSystem>()
        system<DebugSystem>()
    }

    override fun show() {
        log.debug { "Log screen is show" }
        eWorld.systems.forEach { system ->
            if(system is EventListener){
                stage.addListener(system)
            }
        }
        currentMap = TmxMapLoader().load("map/map1.tmx")
        stage.fire(MapChangeEvent(currentMap!!))
    }

    override fun render(delta: Float) {
        eWorld.update(delta.coerceAtMost(0.25f))
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.disposeSafely()
        textureAtlas.disposeSafely()
        eWorld.dispose()
        phWorld.disposeSafely()
        currentMap.disposeSafely()
     }

    companion object {
        private val log = logger<GameScreen>()
    }
}