package fi.dy.masa.malitest.network.action;

import java.time.Instant;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import fi.dy.masa.malitest.MaLiTest;

public abstract class LedgerActionS2CHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerActionS2CHandler<LedgerActionS2CPayload> INSTANCE = new LedgerActionS2CHandler<>()
    {
        @Override
        public void receive(LedgerActionS2CPayload payload, ClientPlayNetworking.Context context)
        {
            LedgerActionS2CHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerActionS2CHandler<LedgerActionS2CPayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = new Identifier("ledger", "action");
    private boolean registered = false;

    @Override
    public Identifier getPayloadChannel() { return CHANNEL_ID; }

    @Override
    public boolean isPlayRegistered(Identifier channel) { return this.registered; }

    @Override
    public void setPlayRegistered(Identifier channel)
    {
        if (channel.equals(CHANNEL_ID))
        {
            this.registered = true;
        }
    }

    @Override
    public void reset(Identifier channel)
    {
        if (channel.equals(CHANNEL_ID))
        {
            MaLiTest.logger.info("LedgerActionS2CHandler: reset()");

            LedgerActionS2CHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerActionS2CPayload payload)
    {
        MaLiTest.logger.info("LedgerActionS2CHandler#decodePayload: payload");

        MaLiTest.logger.info("Pos {}", payload.pos().toShortString());
        MaLiTest.logger.info("id {}", payload.id());
        MaLiTest.logger.info("world {}", payload.world().toString());
        MaLiTest.logger.info("oldObjectId {}", payload.oldObjectId().toString());
        MaLiTest.logger.info("objectId {}", payload.objectId().toString());
        MaLiTest.logger.info("source {}", payload.source());
        MaLiTest.logger.info("timestamp {}", payload.timestamp().toString());
        MaLiTest.logger.info("extraData {}", payload.extraData());
    }

    public void encodePayload(BlockPos pos,
                              String id,
                              Identifier world,
                              Identifier oldObjectId,
                              Identifier objectId,
                              String source,
                              Instant timestamp,
                              String extraData)
    {
        LedgerActionS2CHandler.INSTANCE.sendPlayPayload(new LedgerActionS2CPayload(pos, id, world, oldObjectId, objectId, source, timestamp, extraData));
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerActionS2CHandler#receivePlayPayload: payload");

        LedgerActionS2CHandler.INSTANCE.decodePayload((LedgerActionS2CPayload) payload);
    }
}
