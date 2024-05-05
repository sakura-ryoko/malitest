package fi.dy.masa.malitest.network.response;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class LedgerResponseS2CPayload implements CustomPayload
{
    public static final Id<LedgerResponseS2CPayload> TYPE = new Id<>(LedgerResponseS2CHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerResponseS2CPayload> CODEC = CustomPayload.codecOf(LedgerResponseS2CPayload::write, LedgerResponseS2CPayload::new);

    private final ResponseContent content;

    public LedgerResponseS2CPayload(PacketByteBuf buf)
    {
        this.content = new ResponseContent(buf.readIdentifier(), buf.readInt());
    }

    public LedgerResponseS2CPayload(Identifier type, int response)
    {
        this.content = new ResponseContent(type, response);
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeIdentifier(this.content.getType());
        buf.writeInt(this.content.getResponse());
    }

    public Identifier getContentType()
    {
        return this.content.getType();
    }

    public int getResponse()
    {
        return this.content.getResponse();
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
