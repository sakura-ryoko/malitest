package fi.dy.masa.malitest.network.handshake;

import java.util.Collection;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import fi.dy.masa.malitest.MaLiTest;

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

    public static final Identifier CHANNEL_ID = new Identifier("ledger", "handshake");
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
            MaLiTest.logger.info("LedgerHandshakeHandler: reset()");

            LedgerHandshakeHandler.INSTANCE.unregisterPlayReceiver();
        }
    }

    public void decodePayload(LedgerHandshakePayload payload)
    {
        MaLiTest.logger.info("LedgerHandshakeHandler#decodePayload: payload");
        int protocolVersion = payload.getProtocolVersion();
        String ledgerVersion = payload.getLedgerVersion();
        Collection<String> actionTypes = payload.getActionTypes();

        MaLiTest.logger.info("Protocol Version: {}", protocolVersion);
        MaLiTest.logger.info("Ledger version: {}", ledgerVersion);
        MaLiTest.logger.info("Number of types registered: {}", actionTypes.size());

        for (String actionType : actionTypes)
        {
            MaLiTest.logger.info("Action type: {}", actionType);
        }
    }

    public void encodePayload(NbtCompound nbt)
    {
        LedgerHandshakeHandler.INSTANCE.sendPlayPayload(new LedgerHandshakePayload(nbt));
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {
        if (payload.getId().id().equals(this.getPayloadChannel()))
        {
            LedgerHandshakeHandler.INSTANCE.decodePayload((LedgerHandshakePayload) payload);
        }
    }
}
