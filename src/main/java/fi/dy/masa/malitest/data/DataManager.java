package fi.dy.masa.malitest.data;

import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.network.ClientPlayHandler;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import fi.dy.masa.malitest.MaLiTest;
import fi.dy.masa.malitest.network.handler.LedgerQueryHandler;
import fi.dy.masa.malitest.network.payload.LedgerQueryPayload;

public class DataManager
{
    private static final DataManager INSTANCE = new DataManager();
    public static DataManager getInstance() { return INSTANCE; }
    private final static LedgerQueryHandler<LedgerQueryPayload> HANDLER = LedgerQueryHandler.getInstance();

    private DataManager()
    {
    }

    public void reset(boolean isLogout)
    {
        if (isLogout)
        {
            MaLiTest.logger.info("DataManager#reset() - log-out");
            HANDLER.reset(this.getNetworkChannel());
        }
        else
        {
            MaLiTest.logger.info("DataManager#reset() - dimension change or log-in");
        }
    }

    public void onGameInit()
    {
        MaLiTest.logger.info("DataManager#onGameInit(): execute");

        ClientPlayHandler.getInstance().registerClientPlayHandler(HANDLER);
        HANDLER.registerPlayPayload(LedgerQueryPayload.TYPE, LedgerQueryPayload.CODEC, IPluginClientPlayHandler.BOTH_SERVER);
    }

    public void onWorldPre()
    {
        MaLiTest.logger.info("DataManager#onGameInit(): onWorldPre");
    }

    public void onWorldJoin()
    {
        MaLiTest.logger.info("DataManager#onGameInit(): onWorldJoin");
    }

    public Identifier getNetworkChannel() { return LedgerQueryHandler.CHANNEL_ID; }
}
