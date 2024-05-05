package fi.dy.masa.malitest.network.response;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record LedgerResponsePayload(LedgerResponse content) implements CustomPayload
{
    public static final Id<LedgerResponsePayload> TYPE = new Id<>(LedgerResponseHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerResponsePayload> CODEC = CustomPayload.codecOf(LedgerResponsePayload::write, LedgerResponsePayload::new);

    private LedgerResponsePayload(PacketByteBuf input)
    {
        this(new LedgerResponse(input.readIdentifier(), input.readInt()));
    }

    private void write(PacketByteBuf output)
    {
        output.writeIdentifier(content.type);
        output.writeInt(content.response);
    }

    @Override
    public Id<LedgerResponsePayload> getId() { return TYPE; }
}
