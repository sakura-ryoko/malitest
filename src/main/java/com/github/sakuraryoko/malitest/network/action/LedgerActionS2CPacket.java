package com.github.sakuraryoko.malitest.network.action;

import java.time.Instant;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

public class LedgerActionS2CPacket
{
    public String identifier;
    public Instant timestamp;
    public BlockPos pos;
    public Identifier world;
    public Identifier objectIdentifier;
    public Identifier oldObjectIdentifier;
    public String objectState;
    public String oldObjectState;
    public String sourceName;
    public GameProfile sourceProfile;
    public String extraData;
    public Boolean rolledBack;

    public LedgerActionS2CPacket(String identifier, Instant timestamp, BlockPos pos,
                                 Identifier world, Identifier objectIdentifier, Identifier oldObjectIdentifier,
                                 String objectState, String oldObjectState, String sourceName,
                                 GameProfile sourceProfile, String extraData, Boolean rolledBack)
    {
        this.identifier = identifier;
        this.timestamp = timestamp;
        this.pos = pos;
        this.world = world;
        this.objectIdentifier = objectIdentifier;
        this.oldObjectIdentifier = oldObjectIdentifier;
        this.objectState = objectState;
        this.oldObjectState = oldObjectState;
        this.sourceName = sourceName;
        this.sourceProfile = sourceProfile;
        this.extraData = extraData;
        this.rolledBack = rolledBack;
    }

    public LedgerActionS2CPacket(BlockPos pos, String identifier,
                                 Identifier world, Identifier oldObjectIdentifier, Identifier objectIdentifier,
                                 String source, Long epochTime, Boolean rolledBack, String extraData)
    {
        this.pos = pos;
        this.identifier = identifier;
        this.world = world;
        this.oldObjectIdentifier = oldObjectIdentifier;
        this.objectIdentifier = objectIdentifier;
        this.sourceName = source;
        this.sourceProfile = new GameProfile(Util.NIL_UUID, source);
        this.timestamp = Instant.ofEpochSecond(epochTime);
        this.rolledBack = rolledBack;
        this.extraData = extraData;
        this.objectState = "";
        this.oldObjectState = "";
    }

    public record Payload(LedgerActionS2CPacket content)
            implements CustomPayload
    {
        public static final Id<Payload> ID = new Id<>(LedgerActionS2CHandler.CHANNEL_ID);
        public static final PacketCodec<PacketByteBuf, Payload> CODEC = CustomPayload.codecOf(Payload::write, Payload::new);

        public Payload(PacketByteBuf buf)
        {
            this(new LedgerActionS2CPacket(buf.readBlockPos(), buf.readString(), buf.readIdentifier(), buf.readIdentifier(), buf.readIdentifier(), buf.readString(), buf.readLong(), buf.readBoolean(), buf.readString()));
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
        public Id<Payload> getId() { return ID; }
    }
}
