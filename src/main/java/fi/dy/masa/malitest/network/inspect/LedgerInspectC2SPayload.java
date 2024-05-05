package fi.dy.masa.malitest.network.inspect;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record LedgerInspectC2SPayload(BlockPos pos)
        implements CustomPayload
{
    public static final Id<LedgerInspectC2SPayload> TYPE = new Id<>(LedgerInspectC2SHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerInspectC2SPayload> CODEC = CustomPayload.codecOf(LedgerInspectC2SPayload::write, LedgerInspectC2SPayload::new);

    public LedgerInspectC2SPayload(PacketByteBuf buf)
    {
        this(buf.readBlockPos());
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeBlockPos(pos);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
