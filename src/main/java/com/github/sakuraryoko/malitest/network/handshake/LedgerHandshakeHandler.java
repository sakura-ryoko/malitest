package com.github.sakuraryoko.malitest.network.handshake;

import java.util.List;
import com.github.sakuraryoko.malitest.MaLiTest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

public abstract class LedgerHandshakeHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerHandshakeHandler<LedgerHandshakePacket.Payload> INSTANCE = new LedgerHandshakeHandler<>()
    {
        @Override
        public void receive(LedgerHandshakePacket.Payload payload, ClientPlayNetworking.Context context)
        {
            LedgerHandshakeHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerHandshakeHandler<LedgerHandshakePacket.Payload> getInstance() { return INSTANCE; }

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

    public void decodePayload(LedgerHandshakePacket content)
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

    public void encodePayload(LedgerHandshakePacket content)
    {
        LedgerHandshakeHandler.INSTANCE.sendPlayPayload(new LedgerHandshakePacket.Payload(content));
    }

    public void encodePayload(NbtCompound content)
    {
        LedgerHandshakeHandler.INSTANCE.sendPlayPayload(new LedgerHandshakePacket.Payload(LedgerHandshakePacket.fromNbt(content)));
    }

    public void encodePayload(Integer protocolVersion, String ledgerVersion, String modId)
    {
        LedgerHandshakeHandler.INSTANCE.sendPlayPayload(new LedgerHandshakePacket.Payload(new LedgerHandshakePacket(protocolVersion, ledgerVersion, modId)));
    }

    @Override
    public void encodeWithSplitter(PacketByteBuf buf, ClientPlayNetworkHandler handler)
    {
        // NO-OP
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerHandshakeC2SHandler#receivePlayPayload: payload");

        LedgerHandshakeHandler.INSTANCE.decodePayload(((LedgerHandshakePacket.Payload) payload).content());
    }
}
