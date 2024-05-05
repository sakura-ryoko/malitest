package fi.dy.masa.malitest.network.handshake;

import java.util.List;
import net.minecraft.nbt.NbtCompound;

public class LedgerHandshake
{
    public Integer protocolVersion;
    public String modId;
    public String ledgerVersion;
    List<String> actions;

    public LedgerHandshake(Integer protocolVersion, String ledgerVersion, List<String> actions)
    {
        this.protocolVersion = protocolVersion;
        this.ledgerVersion = ledgerVersion;
        this.actions = actions;
        this.modId = "";
    }

    public LedgerHandshake(Integer protocolVersion, String ledgerVersion, String modId)
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

    public static LedgerHandshake fromNbt(NbtCompound input)
    {
        return new LedgerHandshake(input.getInt("protocol_version"), input.getString("version"), input.getString("modid"));
    }
}
