package br.com.rpg.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

enum class AnimationModel{
    PLAYER, SLIME, CHEST, UNDEFINED;

    val atlasKey = this.toString().lowercase()
}


enum class AnimationType{
    IDLE, RUN, ATACK, DEATH, OPEN;

    val atlasKey = this.toString().lowercase()
}
class AnimationComponent(
    var model: AnimationModel = AnimationModel.UNDEFINED,
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) {

    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation: String = NO_ANIMATION

    fun nextAnimation(model: AnimationModel, type: AnimationType){
        this.model = model
        nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }

    companion object{
        val NO_ANIMATION = ""
    }
}