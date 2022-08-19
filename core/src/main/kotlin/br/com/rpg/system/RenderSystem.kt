package br.com.rpg.system

import br.com.rpg.RPG.Companion.UNIT_SCALE
import br.com.rpg.component.ImageComponent
import br.com.rpg.event.MapChangeEvent
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.collection.compareEntity
import ktx.assets.disposeSafely
import ktx.graphics.use
import ktx.tiled.forEachLayer

@AllOf([ImageComponent::class])
class RenderSystem(
    private val stage: Stage,
    private val imageCmps: ComponentMapper<ImageComponent>
) : EventListener,IteratingSystem(
    comparator = compareEntity { e1, e2 -> imageCmps[e1].compareTo(imageCmps[e2]) }
) {

    private val bgdLayers = mutableListOf<TiledMapTileLayer>()
    private val fgdLayers = mutableListOf<TiledMapTileLayer>()
    private val mapRender = OrthogonalTiledMapRenderer(null,UNIT_SCALE, stage.batch)
    private val orthoCamera = stage.camera as OrthographicCamera

    override fun onTick() {
        super.onTick()
        with(stage) {
            viewport.apply()
            AnimatedTiledMapTile.updateAnimationBaseTime()
            mapRender.setView(orthoCamera)
            if(bgdLayers.isNotEmpty()){
                stage.batch.use(orthoCamera.combined){
                    bgdLayers.forEach { mapRender.renderTileLayer(it) }
                }
            }
            act(deltaTime)
            draw()
            if(fgdLayers.isNotEmpty()){
                stage.batch.use(orthoCamera.combined){
                    fgdLayers.forEach { mapRender.renderTileLayer(it) }
                }
            }
        }
    }


    override fun onTickEntity(entity: Entity) {
        imageCmps[entity].image.toFront()
    }

    override fun handle(event: Event): Boolean {
        when(event){
             is MapChangeEvent ->{
                 bgdLayers.clear()
                 fgdLayers.clear()
                 event.map.forEachLayer<TiledMapTileLayer> { layer ->
                     if(layer.name.startsWith("fgd_")){
                        fgdLayers.add(layer)
                     }else{
                         bgdLayers.add(layer)
                     }
                 }
                return true
            }
        }
        return false
    }

    override fun onDispose() {
        mapRender.disposeSafely()
    }
}