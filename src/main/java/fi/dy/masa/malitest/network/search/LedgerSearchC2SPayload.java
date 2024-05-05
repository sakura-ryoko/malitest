package fi.dy.masa.malitest.network.search;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record LedgerSearchC2SPayload(String query)
        implements CustomPayload
{
    public static final Id<LedgerSearchC2SPayload> TYPE = new Id<>(LedgerSearchC2SHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerSearchC2SPayload> CODEC = CustomPayload.codecOf(LedgerSearchC2SPayload::write, LedgerSearchC2SPayload::new);

    public LedgerSearchC2SPayload(PacketByteBuf buf)
    {
        this(buf.readString());
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeString(query);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
