package br.com.rpg.component

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2


data class SpawnCfg(
    val model: AnimationModel
)

data class SpawnComponent(
    var type :String = "",
val location: Vector2 = vec2()
)