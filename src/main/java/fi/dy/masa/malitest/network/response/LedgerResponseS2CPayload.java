package fi.dy.masa.malitest.network.response;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record LedgerResponseS2CPayload(LedgerResponse reply) implements CustomPayload
{
    public static final Id<LedgerResponseS2CPayload> TYPE = new Id<>(LedgerResponseS2CHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerResponseS2CPayload> CODEC = CustomPayload.codecOf(LedgerResponseS2CPayload::write, LedgerResponseS2CPayload::new);

    private LedgerResponseS2CPayload(PacketByteBuf buf)
    {
        this(new LedgerResponse(new Identifier(buf.readString()), buf.readInt()));
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeIdentifier(reply.type);
        buf.writeInt(reply.response);
    }

    @Override
    public Id<LedgerResponseS2CPayload> getId() { return TYPE; }
}
