package fi.dy.masa.malitest.network.response;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import fi.dy.masa.malitest.MaLiTest;

@Environment(EnvType.CLIENT)
public abstract class LedgerResponseS2CHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerResponseS2CHandler<LedgerResponseS2CPayload> INSTANCE = new LedgerResponseS2CHandler<>()
    {
        @Override
        public void receive(LedgerResponseS2CPayload payload, ClientPlayNetworking.Context context)
        {
            LedgerResponseS2CHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerResponseS2CHandler<LedgerResponseS2CPayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = new Identifier("ledger", "response");
    private boolean registered = false;

    @Override
    public Identifier getPayloadChannel()
    {
        return CHANNEL_ID;
    }

    @Override
    public boolean isPlayRegistered(Identifier channel)
    {
        return this.registered;
    }

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
            MaLiTest.logger.info("LedgerResponseS2CHandler: reset()");

            LedgerResponseS2CHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerResponseS2CPayload payload)
    {
        MaLiTest.logger.info("LedgerResponseS2CHandler#decodePayload: payload");

        MaLiTest.logger.info("Type: {}", payload.getContentType().toString());
        MaLiTest.logger.info("Response: {}", payload.getResponse());
    }

    public void encodePayload(Identifier type, int response)
    {
        LedgerResponseS2CHandler.INSTANCE.sendPlayPayload(new LedgerResponseS2CPayload(type, response));
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerResponseS2CHandler#receivePlayPayload: payload");

        LedgerResponseS2CHandler.INSTANCE.decodePayload((LedgerResponseS2CPayload) payload);
    }
}
