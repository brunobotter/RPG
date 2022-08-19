package br.com.rpg.system

import br.com.rpg.RPG.Companion.UNIT_SCALE
import br.com.rpg.component.*
import br.com.rpg.component.PhysicComponent.Companion.physicCmpFromImage
import br.com.rpg.event.MapChangeEvent
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y


@AllOf([SpawnComponent::class])
class EntitySpawSystem(
    private val phWorld: World,
    private val atlas: TextureAtlas,
    private val spwnCmps: ComponentMapper<SpawnComponent>
) : EventListener, IteratingSystem(){

    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cacheSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spwnCmps[entity]){
            val cfg = spawnCfg(type)
            val relativeSize = size(cfg.model)
            world.entity {
               val imageCmp = add<ImageComponent>{
                    image = Image().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }
                add<AnimationComponent>{
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }
                physicCmpFromImage(phWorld, imageCmp.image, DynamicBody){ phCmp, width, height ->
                    box(width, height){
                        isSensor = false

                    }
                }
            }
        }
        world.remove(entity)
    }

    private fun spawnCfg(type: String) : SpawnCfg = cachedCfgs.getOrPut(type){
       when(type){
            "Player" -> SpawnCfg(AnimationModel.PLAYER)
            "Slime" -> SpawnCfg(AnimationModel.SLIME)
           else -> gdxError("Type $type has no SpawnCfg setup")
       }
    }

    private fun size(model: AnimationModel) = cacheSizes.getOrPut(model){
        val regions = atlas.findRegions("${model.atlasKey}/${AnimationType.IDLE.atlasKey}")
        if(regions.isEmpty){
            gdxError("There are no regions for the idle animation of model $model")
        }
        val firstFrame = regions.first()
        vec2(firstFrame.originalWidth * UNIT_SCALE, firstFrame.originalHeight * UNIT_SCALE)
    }

    override fun handle(event: Event): Boolean {
        when(event){
            is MapChangeEvent -> {
                val entityLayer = event.map.layer("entities")
                entityLayer.objects.forEach { mapObj ->
                    val type = mapObj.type ?: gdxError("Map Object $mapObj does not gave a type")
                    world.entity{
                        add<SpawnComponent>{
                            this.type = type
                            location.set(mapObj.x * UNIT_SCALE, mapObj.y * UNIT_SCALE)
                        }
                    }
                }
                return true
            }
        }
        return false
    }


}