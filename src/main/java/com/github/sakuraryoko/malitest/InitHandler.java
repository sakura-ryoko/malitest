package com.github.sakuraryoko.malitest;

import com.github.sakuraryoko.malitest.data.DataManager;
import com.github.sakuraryoko.malitest.event.WorldLoadListener;
import fi.dy.masa.malilib.event.WorldLoadHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import com.github.sakuraryoko.malitest.commands.MaLiTestCommand;

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

        MaLiTestCommand.register();
    }
}
