package fi.dy.masa.malitest;

import fi.dy.masa.malilib.event.WorldLoadHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malitest.data.DataManager;
import fi.dy.masa.malitest.event.WorldLoadListener;

public class InitHandler implements IInitializationHandler
{
    @Override
    public void registerModHandlers()
    {
        //ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());
        DataManager.getInstance().onGameInit();

        WorldLoadListener listener = new WorldLoadListener();
        WorldLoadHandler.getInstance().registerWorldLoadPreHandler(listener);
        WorldLoadHandler.getInstance().registerWorldLoadPostHandler(listener);
    }
}
