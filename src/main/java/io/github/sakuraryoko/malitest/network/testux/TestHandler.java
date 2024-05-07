package io.github.sakuraryoko.malitest.network.testux;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import io.github.sakuraryoko.malitest.MaLiTest;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;

public abstract class TestHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final TestHandler<TestPayload> INSTANCE = new TestHandler<>()
    {
        @Override
        public void receive(TestPayload payload, ClientPlayNetworking.Context context)
        {
            TestHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static TestHandler<TestPayload> getInstance() { return INSTANCE; }
    public static final Identifier CHANNEL_ID = new Identifier("testux", "test");
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

    public void decodePayload(TestData content)
    {
        MaLiTest.logger.info("TestHandler#decodePayload: received");

        content.dump();
    }

    public void encodePayload(TestData content)
    {
        TestHandler.INSTANCE.sendPlayPayload(new TestPayload(content));
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context context)
    {
        TestHandler.INSTANCE.decodePayload(((TestPayload) payload).content());
    }
}
