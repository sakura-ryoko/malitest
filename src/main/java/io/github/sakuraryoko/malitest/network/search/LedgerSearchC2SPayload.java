package io.github.sakuraryoko.malitest.network.search;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record LedgerSearchC2SPayload(Boolean restore, String args)
        implements CustomPayload
{
    public static final Id<LedgerSearchC2SPayload> TYPE = new Id<>(LedgerSearchC2SHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerSearchC2SPayload> CODEC = CustomPayload.codecOf(LedgerSearchC2SPayload::write, LedgerSearchC2SPayload::new);

    public LedgerSearchC2SPayload(PacketByteBuf input)
    {
        this(input.readBoolean(), input.readString());
    }

    private void write(PacketByteBuf output)
    {
        output.writeBoolean(restore);
        output.writeString(args);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
