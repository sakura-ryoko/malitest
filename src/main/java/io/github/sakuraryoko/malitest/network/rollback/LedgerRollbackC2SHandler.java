package io.github.sakuraryoko.malitest.network.rollback;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import io.github.sakuraryoko.malitest.MaLiTest;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

public abstract class LedgerRollbackC2SHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerRollbackC2SHandler<LedgerRollbackC2SPayload> INSTANCE = new LedgerRollbackC2SHandler<>()
    {
        @Override
        public void receive(LedgerRollbackC2SPayload payload, ClientPlayNetworking.Context context)
        {
            LedgerRollbackC2SHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerRollbackC2SHandler<LedgerRollbackC2SPayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = new Identifier("ledger", "rollback");
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
            MaLiTest.logger.info("LedgerRollbackC2SHandler: reset()");

            LedgerRollbackC2SHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerRollbackC2SPayload payload)
    {
        MaLiTest.logger.info("LedgerRollbackC2SHandler#decodePayload: payload");

        MaLiTest.logger.info("Input: {}", payload.input());
    }

    public void encodePayload(String input)
    {
        LedgerRollbackC2SHandler.INSTANCE.sendPlayPayload(new LedgerRollbackC2SPayload(input));
        MaLiTest.logger.warn("LedgerRollbackC2SHandler#encode() --> sent");
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerRollbackC2SHandler#receivePlayPayload: payload");

        LedgerRollbackC2SHandler.INSTANCE.decodePayload((LedgerRollbackC2SPayload) payload);
    }
}
