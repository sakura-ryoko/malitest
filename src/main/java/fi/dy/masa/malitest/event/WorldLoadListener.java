package fi.dy.masa.malitest.event;

import javax.annotation.Nullable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import fi.dy.masa.malilib.interfaces.IWorldLoadListener;
import fi.dy.masa.malitest.data.DataManager;

public class WorldLoadListener implements IWorldLoadListener
{
    @Override
    public void onWorldLoadPre(@Nullable ClientWorld worldBefore, @Nullable ClientWorld worldAfter, MinecraftClient mc)
    {
        if (worldAfter != null)
        {
            DataManager.getInstance().onWorldPre();
        }
    }

    @Override
    public void onWorldLoadPost(@Nullable ClientWorld worldBefore, @Nullable ClientWorld worldAfter, MinecraftClient mc)
    {
        DataManager.getInstance().reset(worldAfter == null);
        if (worldAfter != null)
        {
            DataManager.getInstance().onWorldJoin();
        }
    }
}