package io.github.sakuraryoko.malitest.network.handshake;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record LedgerHandshakePayload(LedgerHandshake content) implements CustomPayload
{
    public static final Id<LedgerHandshakePayload> TYPE = new Id<>(LedgerHandshakeHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerHandshakePayload> CODEC = CustomPayload.codecOf(LedgerHandshakePayload::write, LedgerHandshakePayload::new);

    public LedgerHandshakePayload(PacketByteBuf input)
    {
        this(new LedgerHandshake(input.readInt(), input.readString(), input.readList(PacketByteBuf::readString)));
    }

    public void write(PacketByteBuf output)
    {
        output.writeNbt(content.toNbt());
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
