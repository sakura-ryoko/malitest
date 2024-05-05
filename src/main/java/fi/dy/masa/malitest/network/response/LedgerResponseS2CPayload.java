package fi.dy.masa.malitest.network.response;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class LedgerResponseS2CPayload implements CustomPayload
{
    public static final Id<LedgerResponseS2CPayload> TYPE = new Id<>(LedgerResponseS2CHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerResponseS2CPayload> CODEC = CustomPayload.codecOf(LedgerResponseS2CPayload::write, LedgerResponseS2CPayload::new);

    ResponseContent response;

    public LedgerResponseS2CPayload(PacketByteBuf buf)
    {
        this(buf.readIdentifier(), buf.readInt());
    }

    public LedgerResponseS2CPayload(Identifier type, int response)
    {
        this.response = new ResponseContent(type, response);
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeIdentifier(this.response.getType());
        buf.writeInt(this.response.getResponseCode());
    }

    public Identifier getContentType()
    {
        return this.response.getType();
    }

    public int getResponseCode()
    {
        return this.response.getResponseCode();
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
