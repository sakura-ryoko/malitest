package com.github.sakuraryoko.malitest.network.response;

import com.github.sakuraryoko.malitest.MaLiTest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

@Environment(EnvType.CLIENT)
public abstract class LedgerResponseHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerResponseHandler<LedgerResponsePayload> INSTANCE = new LedgerResponseHandler<>()
    {
        @Override
        public void receive(LedgerResponsePayload payload, ClientPlayNetworking.Context context)
        {
            LedgerResponseHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerResponseHandler<LedgerResponsePayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = Identifier.of("ledger", "response");
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
            MaLiTest.logger.info("LedgerResponseHandler: reset()");

            LedgerResponseHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerResponse content)
    {
        MaLiTest.logger.info("LedgerResponseHandler#decodePayload: payload");

        MaLiTest.logger.info("Type: {}", content.getType().toString());
        MaLiTest.logger.info("LedgerResponse: {}", content.getResponse());
    }

    public void encodePayload(Identifier type, int response)
    {
        LedgerResponseHandler.INSTANCE.sendPlayPayload(new LedgerResponsePayload(new LedgerResponse(type, response)));
    }

    @Override
    public void encodeWithSplitter(PacketByteBuf buf, ClientPlayNetworkHandler handler)
    {
        // NO-OP
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerResponseHandler#receivePlayPayload: payload");

        LedgerResponseHandler.INSTANCE.decodePayload(((LedgerResponsePayload) payload).content());
    }
}
