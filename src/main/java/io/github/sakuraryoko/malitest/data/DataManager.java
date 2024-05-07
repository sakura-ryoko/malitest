package io.github.sakuraryoko.malitest.data;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import io.github.sakuraryoko.malitest.network.testux.TestData;
import fi.dy.masa.malilib.network.ClientPlayHandler;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import io.github.sakuraryoko.malitest.MaLiTest;
import io.github.sakuraryoko.malitest.Reference;
import io.github.sakuraryoko.malitest.network.action.LedgerActionS2CHandler;
import io.github.sakuraryoko.malitest.network.action.LedgerActionS2CPayload;
import io.github.sakuraryoko.malitest.network.handshake.LedgerHandshakeHandler;
import io.github.sakuraryoko.malitest.network.handshake.LedgerHandshakePayload;
import io.github.sakuraryoko.malitest.network.inspect.LedgerInspectC2SHandler;
import io.github.sakuraryoko.malitest.network.inspect.LedgerInspectC2SPayload;
import io.github.sakuraryoko.malitest.network.purge.LedgerPurgeC2SHandler;
import io.github.sakuraryoko.malitest.network.purge.LedgerPurgeC2SPayload;
import io.github.sakuraryoko.malitest.network.response.LedgerResponseHandler;
import io.github.sakuraryoko.malitest.network.response.LedgerResponsePayload;
import io.github.sakuraryoko.malitest.network.rollback.LedgerRollbackC2SHandler;
import io.github.sakuraryoko.malitest.network.rollback.LedgerRollbackC2SPayload;
import io.github.sakuraryoko.malitest.network.search.LedgerSearchC2SHandler;
import io.github.sakuraryoko.malitest.network.search.LedgerSearchC2SPayload;
import io.github.sakuraryoko.malitest.network.testux.TestHandler;
import io.github.sakuraryoko.malitest.network.testux.TestPayload;

public class DataManager
{
    private static final DataManager INSTANCE = new DataManager();
    public static DataManager getInstance() { return INSTANCE; }

    private final static LedgerActionS2CHandler<LedgerActionS2CPayload> S2C_ACTION = LedgerActionS2CHandler.getInstance();
    private final static LedgerInspectC2SHandler<LedgerInspectC2SPayload> C2S_INSPECT = LedgerInspectC2SHandler.getInstance();
    private final static LedgerPurgeC2SHandler<LedgerPurgeC2SPayload> C2S_PURGE = LedgerPurgeC2SHandler.getInstance();
    private final static LedgerRollbackC2SHandler<LedgerRollbackC2SPayload> C2S_ROLLBACK = LedgerRollbackC2SHandler.getInstance();
    private final static LedgerSearchC2SHandler<LedgerSearchC2SPayload> C2S_SEARCH = LedgerSearchC2SHandler.getInstance();
    private final static LedgerHandshakeHandler<LedgerHandshakePayload> HANDSHAKE = LedgerHandshakeHandler.getInstance();
    private final static LedgerResponseHandler<LedgerResponsePayload> RESPONSE = LedgerResponseHandler.getInstance();

    private final static TestHandler<TestPayload> TEST_HANDLER = TestHandler.getInstance();

    private DataManager()
    {
    }

    public void reset(boolean isLogout)
    {
        if (isLogout)
        {
            MaLiTest.logger.info("DataManager#reset() - log-out");

            // Reset Handlers
            S2C_ACTION.reset(S2C_ACTION.getPayloadChannel());
            C2S_INSPECT.reset(C2S_INSPECT.getPayloadChannel());
            C2S_PURGE.reset(C2S_PURGE.getPayloadChannel());
            C2S_ROLLBACK.reset(C2S_ROLLBACK.getPayloadChannel());
            C2S_SEARCH.reset(C2S_SEARCH.getPayloadChannel());
            HANDSHAKE.reset(HANDSHAKE.getPayloadChannel());
            RESPONSE.reset(RESPONSE.getPayloadChannel());

            TEST_HANDLER.reset(TEST_HANDLER.getPayloadChannel());
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
        ClientPlayHandler.getInstance().registerClientPlayHandler(S2C_ACTION);
        ClientPlayHandler.getInstance().registerClientPlayHandler(C2S_INSPECT);
        ClientPlayHandler.getInstance().registerClientPlayHandler(C2S_PURGE);
        ClientPlayHandler.getInstance().registerClientPlayHandler(C2S_ROLLBACK);
        ClientPlayHandler.getInstance().registerClientPlayHandler(C2S_SEARCH);
        ClientPlayHandler.getInstance().registerClientPlayHandler(HANDSHAKE);
        ClientPlayHandler.getInstance().registerClientPlayHandler(RESPONSE);

        ClientPlayHandler.getInstance().registerClientPlayHandler(TEST_HANDLER);

        // Register Payload Channels
        S2C_ACTION.registerPlayPayload(LedgerActionS2CPayload.TYPE, LedgerActionS2CPayload.CODEC, IPluginClientPlayHandler.FROM_SERVER);
        C2S_INSPECT.registerPlayPayload(LedgerInspectC2SPayload.TYPE, LedgerInspectC2SPayload.CODEC, IPluginClientPlayHandler.TO_SERVER);
        C2S_SEARCH.registerPlayPayload(LedgerSearchC2SPayload.TYPE, LedgerSearchC2SPayload.CODEC, IPluginClientPlayHandler.TO_SERVER);
        C2S_PURGE.registerPlayPayload(LedgerPurgeC2SPayload.TYPE, LedgerPurgeC2SPayload.CODEC, IPluginClientPlayHandler.TO_SERVER);
        C2S_ROLLBACK.registerPlayPayload(LedgerRollbackC2SPayload.TYPE, LedgerRollbackC2SPayload.CODEC, IPluginClientPlayHandler.TO_SERVER);
        HANDSHAKE.registerPlayPayload(LedgerHandshakePayload.TYPE, LedgerHandshakePayload.CODEC, IPluginClientPlayHandler.BOTH_SERVER);
        RESPONSE.registerPlayPayload(LedgerResponsePayload.TYPE, LedgerResponsePayload.CODEC, IPluginClientPlayHandler.BOTH_SERVER);

        TEST_HANDLER.registerPlayPayload(TestPayload.TYPE, TestPayload.CODEC, IPluginClientPlayHandler.BOTH_SERVER);

        PlayerBlockBreakEvents.AFTER.register(this::onInspectBlock);
    }

    public void onWorldPre()
    {
        MaLiTest.logger.info("DataManager#onGameInit(): onWorldPre");

        // Register Receivers
        S2C_ACTION.registerPlayReceiver(LedgerActionS2CPayload.TYPE, S2C_ACTION::receivePlayPayload);
        //C2S_HANDSHAKE.registerPlayReceiver(LedgerHandshakeC2SPayload.TYPE, C2S_HANDSHAKE::receivePlayPayload);
        //C2S_INSPECT.registerPlayReceiver(LedgerInspectC2SPayload.TYPE, C2S_INSPECT::receivePlayPayload);
        //C2S_SEARCH.registerPlayReceiver(LedgerSearchC2SPayload.TYPE, C2S_SEARCH::receivePlayPayload);
        //C2S_PURGE.registerPlayReceiver(LedgerPurgeC2SPayload.TYPE, C2S_PURGE::receivePlayPayload);
        //C2S_ROLLBACK.registerPlayReceiver(LedgerRollbackC2SPayload.TYPE, C2S_ROLLBACK::receivePlayPayload);
        HANDSHAKE.registerPlayReceiver(LedgerHandshakePayload.TYPE, HANDSHAKE::receivePlayPayload);
        RESPONSE.registerPlayReceiver(LedgerResponsePayload.TYPE, RESPONSE::receivePlayPayload);

        TEST_HANDLER.registerPlayReceiver(TestPayload.TYPE, TEST_HANDLER::receivePlayPayload);
    }

    public void onWorldJoin()
    {
        MaLiTest.logger.info("DataManager#onGameInit(): onWorldJoin");

        // Do Something, like register client with Ledger via 'Handshake'.
        NbtCompound data = new NbtCompound();
        data.putString("test", "hello");

        TEST_HANDLER.encodePayload(new TestData(Reference.MOD_ID, Reference.MOD_VERSION, TestHandler.PROTOCOL_VERSION, data));
    }

    public void onInspectBlock(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity)
    {
        C2S_INSPECT.encodePayload(blockPos, 1);
    }

    public void onInspectBlock(BlockPos blockPos)
    {
        C2S_INSPECT.encodePayload(blockPos, 1);
    }

    public void sendSearchQuery(String query)
    {
        C2S_SEARCH.encodePayload(false, query);
    }
}
