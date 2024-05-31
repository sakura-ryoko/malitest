package io.github.sakuraryoko.malitest.network.inspect;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import io.github.sakuraryoko.malitest.MaLiTest;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

public abstract class LedgerInspectC2SHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerInspectC2SHandler<LedgerInspectC2SPayload> INSTANCE = new LedgerInspectC2SHandler<>()
    {
        @Override
        public void receive(LedgerInspectC2SPayload payload, ClientPlayNetworking.Context context)
        {
            LedgerInspectC2SHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerInspectC2SHandler<LedgerInspectC2SPayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = Identifier.of("ledger", "inspect");
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
            MaLiTest.logger.info("LedgerInspectC2SHandler: reset()");

            LedgerInspectC2SHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerInspectC2SPayload payload)
    {
        MaLiTest.logger.info("LedgerInspectC2SHandler#decodePayload: payload");

        MaLiTest.logger.info("Pos: {}", payload.pos().toShortString());
        MaLiTest.logger.info("Pages: {}", payload.pages());
    }

    public void encodePayload(BlockPos pos, Integer pages)
    {
        LedgerInspectC2SHandler.INSTANCE.sendPlayPayload(new LedgerInspectC2SPayload(pos, pages));
        MaLiTest.logger.warn("LedgerInspectC2SHandler#encode(): --> sent");
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerInspectC2SHandler#receivePlayPayload: payload");

        LedgerInspectC2SHandler.INSTANCE.decodePayload((LedgerInspectC2SPayload) payload);
    }
}
