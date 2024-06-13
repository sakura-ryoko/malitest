package com.github.sakuraryoko.malitest.network.response;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class LedgerResponsePacket
{
    public Identifier type;
    public Integer response;

    public LedgerResponsePacket(Identifier type, Integer response)
    {
        this.type = type;
        this.response = response;
    }

    public Identifier getType()
    {
        return this.type;
    }

    public Integer getResponse()
    {
        return this.response;
    }

    public record Payload(LedgerResponsePacket content) implements CustomPayload
    {
        public static final Id<Payload> ID = new Id<>(LedgerResponseHandler.CHANNEL_ID);
        public static final PacketCodec<PacketByteBuf, Payload> CODEC = CustomPayload.codecOf(Payload::write, Payload::new);

        private Payload(PacketByteBuf input)
        {
            this(new LedgerResponsePacket(input.readIdentifier(), input.readInt()));
        }

        private void write(PacketByteBuf output)
        {
            output.writeIdentifier(content.type);
            output.writeInt(content.response);
        }

        @Override
        public Id<Payload> getId() { return ID; }
    }
}
