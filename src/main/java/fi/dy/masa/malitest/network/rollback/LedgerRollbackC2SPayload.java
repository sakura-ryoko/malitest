package fi.dy.masa.malitest.network.rollback;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record LedgerRollbackC2SPayload(String input)
        implements CustomPayload
{
    public static final Id<LedgerRollbackC2SPayload> TYPE = new Id<>(LedgerRollbackC2SHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerRollbackC2SPayload> CODEC = CustomPayload.codecOf(LedgerRollbackC2SPayload::write, LedgerRollbackC2SPayload::new);

    public LedgerRollbackC2SPayload(PacketByteBuf buf)
    {
        this(buf.readString());
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeString(input);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
