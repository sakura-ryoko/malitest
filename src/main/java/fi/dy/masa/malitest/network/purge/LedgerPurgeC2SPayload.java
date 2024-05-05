package fi.dy.masa.malitest.network.purge;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record LedgerPurgeC2SPayload(BlockPos pos, int pages)
        implements CustomPayload
{
    public static final Id<LedgerPurgeC2SPayload> TYPE = new Id<>(LedgerPurgeC2SHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerPurgeC2SPayload> CODEC = CustomPayload.codecOf(LedgerPurgeC2SPayload::write, LedgerPurgeC2SPayload::new);

    public LedgerPurgeC2SPayload(PacketByteBuf buf)
    {
        this(buf.readBlockPos(), buf.readInt());
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeInt(pages);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
