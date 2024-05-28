package gg.norisk.ffa.event

import net.minecraft.entity.Entity
import net.minecraft.entity.data.TrackedData
import net.silkmc.silk.core.event.Event

object EntityEvents {
    class EntityTrackedDataSetEvent(val entity: Entity, val data: TrackedData<*>)

    val onTrackedDataSetEvent = Event.onlySync<EntityTrackedDataSetEvent>()
}
