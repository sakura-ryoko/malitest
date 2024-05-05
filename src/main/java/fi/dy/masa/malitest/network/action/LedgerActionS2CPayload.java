package fi.dy.masa.malitest.network.action;

import java.time.Instant;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record LedgerActionS2CPayload(BlockPos pos,
                                     String id,
                                     Identifier world,
                                     Identifier oldObjectId,
                                     Identifier objectId,
                                     String source,
                                     Instant timestamp,
                                     String extraData)
        implements CustomPayload
{
    public static final Id<LedgerActionS2CPayload> TYPE = new Id<>(LedgerActionS2CHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerActionS2CPayload> CODEC = CustomPayload.codecOf(LedgerActionS2CPayload::write, LedgerActionS2CPayload::new);

    public LedgerActionS2CPayload(PacketByteBuf buf)
    {
        this(buf.readBlockPos(), buf.readString(), buf.readIdentifier(), buf.readIdentifier(), buf.readIdentifier(), buf.readString(), buf.readInstant(), buf.readString());
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeString(id);
        buf.writeIdentifier(world);
        buf.writeIdentifier(oldObjectId);
        buf.writeIdentifier(objectId);
        buf.writeString(source);
        buf.writeInstant(timestamp);
        buf.writeString(extraData);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
