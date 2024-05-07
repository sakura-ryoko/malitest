package io.github.sakuraryoko.malitest.network.action;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record LedgerActionS2CPayload(LedgerActionType content)
        implements CustomPayload
{
    public static final Id<LedgerActionS2CPayload> TYPE = new Id<>(LedgerActionS2CHandler.CHANNEL_ID);
    public static final PacketCodec<PacketByteBuf, LedgerActionS2CPayload> CODEC = CustomPayload.codecOf(LedgerActionS2CPayload::write, LedgerActionS2CPayload::new);

    public LedgerActionS2CPayload(PacketByteBuf buf)
    {
        this(new LedgerActionType(buf.readBlockPos(), buf.readString(), buf.readIdentifier(), buf.readIdentifier(), buf.readIdentifier(), buf.readString(), buf.readLong(), buf.readBoolean(), buf.readString()));
    }

    private void write(PacketByteBuf buf)
    {
        buf.writeBlockPos(content.pos);
        buf.writeString(content.identifier);
        buf.writeIdentifier(content.world);
        buf.writeIdentifier(content.oldObjectIdentifier);
        buf.writeIdentifier(content.objectIdentifier);
        buf.writeString(!content.sourceProfile.getName().isEmpty() ? content.sourceProfile.getName() : "@" + content.sourceName);
        buf.writeLong(content.timestamp.getEpochSecond());
        buf.writeBoolean(content.rolledBack);
        buf.writeString(content.extraData.isEmpty() ? "" : content.extraData);
    }

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}
