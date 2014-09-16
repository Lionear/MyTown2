package mytown.protection;

import buildcraft.builders.TileAbstractBuilder;
import buildcraft.core.Box;
import mytown.MyTown;
import mytown.datasource.MyTownUniverse;
import mytown.entities.Block;
import mytown.entities.Town;
import mytown.entities.flag.Flag;
import mytown.entities.flag.FlagType;
import mytown.util.ChunkPos;
import mytown.util.Utils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

import java.util.List;

/**
 * Created by AfterWind on 9/15/2014.
 * All buildcraft protection
 */
public class BuildCraftProtection extends Protection {

    Class<? extends TileEntity> clsTileAbstractBuilder;


    public BuildCraftProtection() {
        // We need reflection only for loading the mod
        clsTileAbstractBuilder = TileAbstractBuilder.class;
        trackedTileEntities.add(clsTileAbstractBuilder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean checkTileEntity(TileEntity te) {

        if(clsTileAbstractBuilder.isAssignableFrom(te.getClass())) {
            // In case we add more tile entities to check
            Box box = null;
            try {
                box = (Box) clsTileAbstractBuilder.getMethod("getBox").invoke(te);
            } catch (Exception e) {
                MyTown.instance.log.info("Failed to get methods for a Buildcraft tile entity!");
                e.printStackTrace();
                return false;
            }
            List<ChunkPos> chunks = Utils.getChunksInBox(box.xMin, box.zMin, box.xMax, box.zMax);
            for(ChunkPos p : chunks) {
                Block block = MyTownUniverse.getInstance().getBlocksMap().get(String.format(Block.keyFormat, te.getWorldObj().provider.dimensionId, p.getX(), p.getZ()));
                if(block != null) {
                    // Directly from the town, not checking per plot since it's quite the pain
                    Flag<Boolean> bcFlag = block.getTown().getFlag(FlagType.bcBuildingMining);
                    if(!bcFlag.getValue()) {
                        block.getTown().notifyEveryone(getLocal().getLocalization("mytown.protection.bcBuildingMining"));
                        MyTown.instance.log.info("A buildcraft machine at coordonates " + te.xCoord + ", " + te.yCoord + ", " + te.zCoord + " in dimension " + te.getWorldObj().provider.dimensionId + " tried to bypass protection!");
                        return false;
                    }
                }
            }
        }

        return false;
    }
}
