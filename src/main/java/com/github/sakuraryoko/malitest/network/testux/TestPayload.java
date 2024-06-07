package com.github.sakuraryoko.malitest.network.testux;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record TestPayload(TestData content) implements CustomPayload
{
    public static final Id<TestPayload> TYPE = new Id<>(TestHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, TestPayload> CODEC = CustomPayload.codecOf(TestPayload::write, TestPayload::new);

    public TestPayload(PacketByteBuf input)
    {
        this(TestData.fromPacket(input));
    }

    private void write(PacketByteBuf output)
    {
        content.toPacket(output);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
