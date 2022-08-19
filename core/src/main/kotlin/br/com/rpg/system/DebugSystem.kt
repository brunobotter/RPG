package br.com.rpg.system

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import ktx.assets.disposeSafely

class DebugSystem(
    private val phWorld: World,
    private val stage: Stage
) : IntervalSystem(enabled = true) {

    private lateinit var physicRender: Box2DDebugRenderer

    init {
        if (enabled) {
            physicRender = Box2DDebugRenderer()
        }
    }


    override fun onDispose() {
        if (enabled) {
            physicRender.disposeSafely()
        }
    }

    override fun onTick() {
        physicRender.render(phWorld, stage.camera.combined)
    }
}