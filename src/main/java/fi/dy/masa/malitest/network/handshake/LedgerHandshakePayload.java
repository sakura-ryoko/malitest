package fi.dy.masa.malitest.network.handshake;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class LedgerHandshakePayload implements CustomPayload
{
    public static final Id<LedgerHandshakePayload> TYPE = new Id<>(LedgerHandshakeHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerHandshakePayload> CODEC = CustomPayload.codecOf(LedgerHandshakePayload::write, LedgerHandshakePayload::new);

    private final int protocolVersion;
    private final String ledgerVersion;
    private final Collection<String> actionTypes;
    private final NbtCompound nbt;

    public LedgerHandshakePayload(PacketByteBuf buf)
    {
        this.protocolVersion = buf.readInt();
        this.ledgerVersion = buf.readString();
        this.actionTypes = buf.readList(PacketByteBuf::readString);
        this.nbt = new NbtCompound();
    }

    public LedgerHandshakePayload(NbtCompound nbt)
    {
        this.nbt = nbt;
        this.protocolVersion = -1;
        this.ledgerVersion = "";
        this.actionTypes = new ArrayList<>();
    }

    public void write(PacketByteBuf buf)
    {
        buf.writeNbt(this.nbt);
    }

    public int getProtocolVersion()
    {
        return this.protocolVersion;
    }

    public String getLedgerVersion()
    {
        return this.ledgerVersion;
    }

    public Collection<String> getActionTypes()
    {
        return this.actionTypes;
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
