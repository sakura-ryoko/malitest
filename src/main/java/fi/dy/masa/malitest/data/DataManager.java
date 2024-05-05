package fi.dy.masa.malitest.data;

import fi.dy.masa.malilib.network.ClientPlayHandler;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import fi.dy.masa.malitest.MaLiTest;
import fi.dy.masa.malitest.network.action.LedgerActionS2CHandler;
import fi.dy.masa.malitest.network.action.LedgerActionS2CPayload;
import fi.dy.masa.malitest.network.handshake.LedgerHandshakeHandler;
import fi.dy.masa.malitest.network.handshake.LedgerHandshakePayload;
import fi.dy.masa.malitest.network.inspect.LedgerInspectC2SHandler;
import fi.dy.masa.malitest.network.inspect.LedgerInspectC2SPayload;
import fi.dy.masa.malitest.network.purge.LedgerPurgeC2SHandler;
import fi.dy.masa.malitest.network.purge.LedgerPurgeC2SPayload;
import fi.dy.masa.malitest.network.response.LedgerResponseS2CHandler;
import fi.dy.masa.malitest.network.response.LedgerResponseS2CPayload;
import fi.dy.masa.malitest.network.rollback.LedgerRollbackC2SHandler;
import fi.dy.masa.malitest.network.rollback.LedgerRollbackC2SPayload;
import fi.dy.masa.malitest.network.search.LedgerSearchC2SHandler;
import fi.dy.masa.malitest.network.search.LedgerSearchC2SPayload;

public class DataManager
{
    private static final DataManager INSTANCE = new DataManager();
    public static DataManager getInstance() { return INSTANCE; }

    private final static LedgerActionS2CHandler<LedgerActionS2CPayload> ACTION = LedgerActionS2CHandler.getInstance();
    private final static LedgerHandshakeHandler<LedgerHandshakePayload> HANDSHAKE = LedgerHandshakeHandler.getInstance();
    private final static LedgerInspectC2SHandler<LedgerInspectC2SPayload> INSPECT = LedgerInspectC2SHandler.getInstance();
    private final static LedgerPurgeC2SHandler<LedgerPurgeC2SPayload> PURGE = LedgerPurgeC2SHandler.getInstance();
    private final static LedgerResponseS2CHandler<LedgerResponseS2CPayload> RESPONSE = LedgerResponseS2CHandler.getInstance();
    private final static LedgerRollbackC2SHandler<LedgerRollbackC2SPayload> ROLLBACK = LedgerRollbackC2SHandler.getInstance();
    private final static LedgerSearchC2SHandler<LedgerSearchC2SPayload> SEARCH = LedgerSearchC2SHandler.getInstance();

    private DataManager()
    {
    }

    public void reset(boolean isLogout)
    {
        if (isLogout)
        {
            MaLiTest.logger.info("DataManager#reset() - log-out");

            // Reset Handlers
            ACTION.reset(ACTION.getPayloadChannel());
            HANDSHAKE.reset(HANDSHAKE.getPayloadChannel());
            INSPECT.reset(INSPECT.getPayloadChannel());
            PURGE.reset(PURGE.getPayloadChannel());
            RESPONSE.reset(RESPONSE.getPayloadChannel());
            ROLLBACK.reset(ROLLBACK.getPayloadChannel());
            SEARCH.reset(SEARCH.getPayloadChannel());
        }
        else
        {
            MaLiTest.logger.info("DataManager#reset() - dimension change or log-in");
        }
    }

    public void onGameInit()
    {
        MaLiTest.logger.info("DataManager#onGameInit(): execute");

        // Register Handlers
        ClientPlayHandler.getInstance().registerClientPlayHandler(ACTION);
        ClientPlayHandler.getInstance().registerClientPlayHandler(HANDSHAKE);
        ClientPlayHandler.getInstance().registerClientPlayHandler(INSPECT);
        ClientPlayHandler.getInstance().registerClientPlayHandler(PURGE);
        ClientPlayHandler.getInstance().registerClientPlayHandler(RESPONSE);
        ClientPlayHandler.getInstance().registerClientPlayHandler(ROLLBACK);
        ClientPlayHandler.getInstance().registerClientPlayHandler(SEARCH);

        // Register Payload Channels
        ACTION.registerPlayPayload(LedgerActionS2CPayload.TYPE, LedgerActionS2CPayload.CODEC, IPluginClientPlayHandler.FROM_SERVER);
        HANDSHAKE.registerPlayPayload(LedgerHandshakePayload.TYPE, LedgerHandshakePayload.CODEC, IPluginClientPlayHandler.BOTH_SERVER);
        INSPECT.registerPlayPayload(LedgerInspectC2SPayload.TYPE, LedgerInspectC2SPayload.CODEC, IPluginClientPlayHandler.TO_SERVER);
        PURGE.registerPlayPayload(LedgerPurgeC2SPayload.TYPE, LedgerPurgeC2SPayload.CODEC, IPluginClientPlayHandler.TO_SERVER);
        RESPONSE.registerPlayPayload(LedgerResponseS2CPayload.TYPE, LedgerResponseS2CPayload.CODEC, IPluginClientPlayHandler.FROM_SERVER);
        ROLLBACK.registerPlayPayload(LedgerRollbackC2SPayload.TYPE, LedgerRollbackC2SPayload.CODEC, IPluginClientPlayHandler.TO_SERVER);
        SEARCH.registerPlayPayload(LedgerSearchC2SPayload.TYPE, LedgerSearchC2SPayload.CODEC, IPluginClientPlayHandler.TO_SERVER);
    }

    public void onWorldPre()
    {
        MaLiTest.logger.info("DataManager#onGameInit(): onWorldPre");

        // Register Receivers
        ACTION.registerPlayReceiver(LedgerActionS2CPayload.TYPE, ACTION::receivePlayPayload);
        HANDSHAKE.registerPlayReceiver(LedgerHandshakePayload.TYPE, HANDSHAKE::receivePlayPayload);
        RESPONSE.registerPlayReceiver(LedgerResponseS2CPayload.TYPE, RESPONSE::receivePlayPayload);
    }

    public void onWorldJoin()
    {
        MaLiTest.logger.info("DataManager#onGameInit(): onWorldJoin");

        // Do Something
    }
}
