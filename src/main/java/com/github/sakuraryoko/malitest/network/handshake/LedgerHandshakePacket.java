package com.github.sakuraryoko.malitest.network.handshake;

import java.util.List;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class LedgerHandshakePacket
{
    public Integer protocolVersion;
    public String modId;
    public String ledgerVersion;
    List<String> actions;

    public LedgerHandshakePacket(Integer protocolVersion, String ledgerVersion, List<String> actions)
    {
        this.protocolVersion = protocolVersion;
        this.ledgerVersion = ledgerVersion;
        this.actions = actions;
        this.modId = "";
    }

    public LedgerHandshakePacket(Integer protocolVersion, String ledgerVersion, String modId)
    {
        this.protocolVersion = protocolVersion;
        this.ledgerVersion = ledgerVersion;
        this.modId = modId;
        this.actions = List.of();
    }

    public Integer getProtocolVersion()
    {
        return this.protocolVersion;
    }

    public String getLedgerVersion()
    {
        return this.ledgerVersion;
    }

    public List<String> getActions()
    {
        return this.actions;
    }

    public String getModId()
    {
        return this.modId;
    }

    public NbtCompound toNbt()
    {
        NbtCompound result = new NbtCompound();

        result.putString("modid", this.modId);
        result.putString("version", this.ledgerVersion);
        result.putInt("protocol_version", this.protocolVersion);

        return result;
    }

    public static LedgerHandshakePacket fromNbt(NbtCompound input)
    {
        return new LedgerHandshakePacket(input.getInt("protocol_version"), input.getString("version"), input.getString("modid"));
    }

    public record Payload(LedgerHandshakePacket content) implements CustomPayload
    {
        public static final Id<Payload> ID = new Id<>(LedgerHandshakeHandler.CHANNEL_ID);
        public static final PacketCodec<PacketByteBuf, Payload> CODEC = CustomPayload.codecOf(Payload::write, Payload::new);

        public Payload(PacketByteBuf input)
        {
            this(new LedgerHandshakePacket(input.readInt(), input.readString(), input.readList(PacketByteBuf::readString)));
        }

        public void write(PacketByteBuf output)
        {
            output.writeNbt(content.toNbt());
        }

        @Override
        public Id<Payload> getId() { return ID; }
    }
}
