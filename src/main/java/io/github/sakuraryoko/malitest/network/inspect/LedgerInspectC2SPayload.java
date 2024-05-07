package io.github.sakuraryoko.malitest.network.inspect;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record LedgerInspectC2SPayload(BlockPos pos, Integer pages)
        implements CustomPayload
{
    public static final Id<LedgerInspectC2SPayload> TYPE = new Id<>(LedgerInspectC2SHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerInspectC2SPayload> CODEC = CustomPayload.codecOf(LedgerInspectC2SPayload::write, LedgerInspectC2SPayload::new);

    public LedgerInspectC2SPayload(PacketByteBuf input)
    {
        this(input.readBlockPos(), input.readInt());
    }

    private void write(PacketByteBuf output)
    {
        output.writeBlockPos(pos);
        output.writeInt(pages);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
