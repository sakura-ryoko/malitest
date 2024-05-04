package fi.dy.masa.malitest.network.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import fi.dy.masa.malitest.network.payload.LedgerQueryPayload;

@Environment(EnvType.CLIENT)
public abstract class LedgerQueryHandler<T extends CustomPayload> implements IPluginClientPlayHandler<T>
{
    private static final LedgerQueryHandler<LedgerQueryPayload> INSTANCE = new LedgerQueryHandler<>()
    {
        @Override
        public void receive(LedgerQueryPayload payload, ClientPlayNetworking.Context context)
        {
            LedgerQueryHandler.INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static LedgerQueryHandler<LedgerQueryPayload> getInstance() { return INSTANCE; }

    public static final Identifier CHANNEL_ID = new Identifier("ledger", "query");
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
            this.registered = false;
        }
    }

    @Override
    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx)
    {

    }
}
