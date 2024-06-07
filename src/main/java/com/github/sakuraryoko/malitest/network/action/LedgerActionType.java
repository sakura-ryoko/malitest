package com.github.sakuraryoko.malitest.network.action;

import java.time.Instant;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

public class LedgerActionType
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

    public LedgerActionType(String identifier, Instant timestamp, BlockPos pos,
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

    public LedgerActionType(BlockPos pos, String identifier,
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
}
