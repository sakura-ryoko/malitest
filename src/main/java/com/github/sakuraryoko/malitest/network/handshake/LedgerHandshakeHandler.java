package com.github.sakuraryoko.malitest.network.handshake;

import java.util.List;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import com.github.sakuraryoko.malitest.MaLiTest;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

public abstract class LedgerHandshakeHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerHandshakeHandler<LedgerHandshakePayload> INSTANCE = new LedgerHandshakeHandler<>()
    {
        @Override
        public void receive(LedgerHandshakePayload payload, ClientPlayNetworking.Context context)
        {
            LedgerHandshakeHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerHandshakeHandler<LedgerHandshakePayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = Identifier.of("ledger", "handshake");
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
            MaLiTest.logger.info("LedgerHandshakeC2SHandler: reset()");

            LedgerHandshakeHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerHandshake content)
    {
        MaLiTest.logger.info("LedgerHandshakeC2SHandler#decodePayload: payload");
        int protocolVersion = content.getProtocolVersion();
        String ledgerVersion = content.getLedgerVersion();
        List<String> actionTypes = content.getActions();

        MaLiTest.logger.info("Protocol Version: {}", protocolVersion);
        MaLiTest.logger.info("Ledger version: {}", ledgerVersion);
        MaLiTest.logger.info("Number of types registered: {}", actionTypes.size());

        for (String actionType : actionTypes)
        {
            MaLiTest.logger.info("Action type: {}", actionType);
        }
    }

    public void encodePayload(LedgerHandshake content)
    {
        LedgerHandshakeHandler.INSTANCE.sendPlayPayload(new LedgerHandshakePayload(content));
    }

    public void encodePayload(NbtCompound content)
    {
        LedgerHandshakeHandler.INSTANCE.sendPlayPayload(new LedgerHandshakePayload(LedgerHandshake.fromNbt(content)));
    }

    public void encodePayload(Integer protocolVersion, String ledgerVersion, String modId)
    {
        LedgerHandshakeHandler.INSTANCE.sendPlayPayload(new LedgerHandshakePayload(new LedgerHandshake(protocolVersion, ledgerVersion, modId)));
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerHandshakeC2SHandler#receivePlayPayload: payload");

        LedgerHandshakeHandler.INSTANCE.decodePayload(((LedgerHandshakePayload) payload).content());
    }
}