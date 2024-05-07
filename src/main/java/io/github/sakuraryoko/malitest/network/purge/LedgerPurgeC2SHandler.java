package io.github.sakuraryoko.malitest.network.purge;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import io.github.sakuraryoko.malitest.MaLiTest;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

public abstract class LedgerPurgeC2SHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerPurgeC2SHandler<LedgerPurgeC2SPayload> INSTANCE = new LedgerPurgeC2SHandler<>()
    {
        @Override
        public void receive(LedgerPurgeC2SPayload payload, ClientPlayNetworking.Context context)
        {
            LedgerPurgeC2SHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerPurgeC2SHandler<LedgerPurgeC2SPayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = new Identifier("ledger", "purge");
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
            MaLiTest.logger.info("LedgerPurgeC2SHandler: reset()");

            LedgerPurgeC2SHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerPurgeC2SPayload payload)
    {
        MaLiTest.logger.info("LedgerPurgeC2SHandler#decodePayload: payload");

        MaLiTest.logger.info("BlockPos: {}", payload.pos().toShortString());
        MaLiTest.logger.info("pages: {}", payload.pages());
    }

    public void encodePayload(BlockPos pos, int pages)
    {
        LedgerPurgeC2SHandler.INSTANCE.sendPlayPayload(new LedgerPurgeC2SPayload(pos, pages));

        MaLiTest.logger.warn("LedgerPurgeC2SHandler#encode() --> sent");
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerPurgeC2SHandler#receivePlayPayload: payload");

        LedgerPurgeC2SHandler.INSTANCE.decodePayload((LedgerPurgeC2SPayload) payload);
    }
}
