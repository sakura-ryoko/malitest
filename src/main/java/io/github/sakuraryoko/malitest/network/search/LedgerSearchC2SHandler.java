package io.github.sakuraryoko.malitest.network.search;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import io.github.sakuraryoko.malitest.MaLiTest;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

public abstract class LedgerSearchC2SHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerSearchC2SHandler<LedgerSearchC2SPayload> INSTANCE = new LedgerSearchC2SHandler<>()
    {
        @Override
        public void receive(LedgerSearchC2SPayload payload, ClientPlayNetworking.Context context)
        {
            LedgerSearchC2SHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerSearchC2SHandler<LedgerSearchC2SPayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = Identifier.of("ledger", "search");
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
            MaLiTest.logger.info("LedgerSearchC2SHandler: reset()");

            LedgerSearchC2SHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerSearchC2SPayload payload)
    {
        MaLiTest.logger.info("LedgerSearchC2SHandler#decodePayload: payload");

        MaLiTest.logger.info("Restore: {}", payload.restore());
        MaLiTest.logger.info("Args: {}", payload.args());
    }

    public void encodePayload(Boolean restore, String args)
    {
        LedgerSearchC2SHandler.INSTANCE.sendPlayPayload(new LedgerSearchC2SPayload(restore, args));
        MaLiTest.logger.warn("LedgerSearchC2SHandler#encode() --> sent");
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerSearchC2SHandler#receivePlayPayload: payload");

        LedgerSearchC2SHandler.INSTANCE.decodePayload((LedgerSearchC2SPayload) payload);
    }
}
