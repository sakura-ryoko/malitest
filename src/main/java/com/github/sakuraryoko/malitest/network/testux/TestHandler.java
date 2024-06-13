package com.github.sakuraryoko.malitest.network.testux;

import com.github.sakuraryoko.malitest.MaLiTest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

public abstract class TestHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final TestHandler<TestPacket.Payload> INSTANCE = new TestHandler<>()
    {
        @Override
        public void receive(TestPacket.Payload payload, ClientPlayNetworking.Context context)
        {
            TestHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static TestHandler<TestPacket.Payload> getInstance() { return INSTANCE; }
    public static final Identifier CHANNEL_ID = Identifier.of("testux", "test");
    public static final int PROTOCOL_VERSION = 1;

    private boolean registered = false;

    @Override
    public Identifier getPayloadChannel()
    {
        return CHANNEL_ID;
    }

    @Override
    public boolean isPlayRegistered(Identifier identifier)
    {
        if (identifier.equals(CHANNEL_ID))
        {
            return this.registered;
        }

        return false;
    }

    @Override
    public void setPlayRegistered(Identifier identifier)
    {
        if (identifier.equals(CHANNEL_ID))
        {
            this.registered = true;
        }
    }

    @Override
    public void reset(Identifier identifier)
    {
        if (identifier.equals(CHANNEL_ID))
        {
            MaLiTest.logger.info("TestHandler: reset()");

            TestHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(TestPacket content)
    {
        MaLiTest.logger.info("TestHandler#decodePayload: received");

        content.dump();
    }

    @Override
    public void encodeWithSplitter(PacketByteBuf buf, ClientPlayNetworkHandler handler)
    {
        // NO-OP
    }

    public void encodePayload(TestPacket content)
    {
        TestHandler.INSTANCE.sendPlayPayload(new TestPacket.Payload(content));
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context context)
    {
        TestHandler.INSTANCE.decodePayload(((TestPacket.Payload) payload).content());
    }
}
