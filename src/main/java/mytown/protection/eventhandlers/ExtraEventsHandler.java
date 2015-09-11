package mytown.protection.eventhandlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import myessentials.utils.WorldUtils;
import mytown.datasource.MyTownUniverse;
import mytown.entities.TownBlock;
import mytown.entities.Wild;
import mytown.entities.flag.ProtectionFlagType;
import myessentials.entities.ChunkPos;
import net.minecraftforge.event.world.ExplosionEvent;

import java.util.List;

/**
 * Handling any events that are not yet compatible with the most commonly used version of forge.
 */
public class ExtraEventsHandler {

    private static ExtraEventsHandler instance;
    public static ExtraEventsHandler getInstance() {
        if(instance == null)
            instance = new ExtraEventsHandler();
        return instance;
    }

    /**
     * Forge 1254 is needed for this
     */
    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Start ev) {
        if(ev.world.isRemote)
            return;
        if (ev.isCanceled())
            return;
        List<ChunkPos> chunks = WorldUtils.getChunksInBox(ev.world.provider.dimensionId, (int) (ev.explosion.explosionX - ev.explosion.explosionSize - 2), (int) (ev.explosion.explosionZ - ev.explosion.explosionSize - 2), (int) (ev.explosion.explosionX + ev.explosion.explosionSize + 2), (int) (ev.explosion.explosionZ + ev.explosion.explosionSize + 2));
        for(ChunkPos chunk : chunks) {
            TownBlock block = MyTownUniverse.instance.blocks.get(ev.world.provider.dimensionId, chunk.getX(), chunk.getZ());
            if(block == null) {
                if(!(Boolean)Wild.instance.flagsContainer.getValue(ProtectionFlagType.EXPLOSIONS)) {
                    ev.setCanceled(true);
                    return;
                }
            } else {
                if (!(Boolean) block.getTown().flagsContainer.getValue(ProtectionFlagType.EXPLOSIONS)) {
                    ev.setCanceled(true);
                    block.getTown().notifyEveryone(ProtectionFlagType.EXPLOSIONS.getLocalizedTownNotification());
                    return;
                }
            }
        }
    }
}
