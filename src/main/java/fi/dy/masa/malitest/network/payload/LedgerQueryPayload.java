package fi.dy.masa.malitest.network.payload;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import fi.dy.masa.malitest.network.handler.LedgerQueryHandler;

public record LedgerQueryPayload(String data) implements CustomPayload
{
    public static final Id<LedgerQueryPayload> TYPE = new Id<>(LedgerQueryHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerQueryPayload> CODEC = CustomPayload.codecOf(LedgerQueryPayload::write, LedgerQueryPayload::new);

    public LedgerQueryPayload(PacketByteBuf buf) { this(buf.readString()); }

    private void write(PacketByteBuf buf) { buf.writeString(data); }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
