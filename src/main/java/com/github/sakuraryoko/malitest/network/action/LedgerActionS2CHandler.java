package com.github.sakuraryoko.malitest.network.action;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import com.github.sakuraryoko.malitest.MaLiTest;

public abstract class LedgerActionS2CHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerActionS2CHandler<LedgerActionS2CPacket.Payload> INSTANCE = new LedgerActionS2CHandler<>()
    {
        @Override
        public void receive(LedgerActionS2CPacket.Payload payload, ClientPlayNetworking.Context context)
        {
            LedgerActionS2CHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerActionS2CHandler<LedgerActionS2CPacket.Payload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = Identifier.of("ledger", "action");
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

    public void decodePayload(LedgerActionS2CPacket content)
    {
        MaLiTest.logger.info("LedgerActionS2CHandler#decodePayload: payload");

        MaLiTest.logger.info("Pos {}", content.pos.toShortString());
        MaLiTest.logger.info("id {}", content.identifier);
        MaLiTest.logger.info("world {}", content.world.toString());
        MaLiTest.logger.info("oldObjectIdentifier {}", content.oldObjectIdentifier.toString());
        MaLiTest.logger.info("objectIdentifier {}", content.objectIdentifier.toString());
        MaLiTest.logger.info("source {}", content.sourceName);
        MaLiTest.logger.info("timestamp {}", content.timestamp.toString());
        MaLiTest.logger.info("rolledBack {}", content.rolledBack);
        MaLiTest.logger.info("extraData {}", content.extraData);
    }

    @Override
    public void encodeWithSplitter(PacketByteBuf buf, ClientPlayNetworkHandler handler)
    {
        // NO-OP
    }

    public void encodePayload(LedgerActionS2CPacket content)
    {
        LedgerActionS2CHandler.INSTANCE.sendPlayPayload(new LedgerActionS2CPacket.Payload(content));

        MaLiTest.logger.warn("LedgerActionS2CHandler#encode() --> sent");
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        MaLiTest.logger.info("LedgerActionS2CHandler#receivePlayPayload: payload");

        LedgerActionS2CHandler.INSTANCE.decodePayload(((LedgerActionS2CPacket.Payload) payload).content());
    }
}
