package cn.backday.utils.player;

import cn.backday.utils.MinecraftInterface;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class PlayerUtils implements MinecraftInterface {
    public static Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
    }
}
