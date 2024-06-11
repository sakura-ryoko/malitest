package com.github.sakuraryoko.malitest.network.testux;

import javax.annotation.Nullable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import com.github.sakuraryoko.malitest.MaLiTest;
import fi.dy.masa.malilib.network.IClientPayloadData;

public class TestData implements IClientPayloadData
{
    private String modName;
    private String modVersion;
    private int protocolVersion;
    private NbtCompound nbt = new NbtCompound();

    public TestData(String name, String ver, int protocol, @Nullable NbtCompound data)
    {
        this.modName = name;
        this.modVersion = ver;
        this.protocolVersion = protocol;
        if (data != null && !data.isEmpty())
        {
            this.nbt.copyFrom(data);
        }
    }

    public String getModName()
    {
        return this.modName;
    }

    public String getModVersion()
    {
        return this.modVersion;
    }

    @Nullable
    public NbtCompound getNbt()
    {
        return this.nbt;
    }

    public void replaceNbt(NbtCompound input)
    {
        this.nbt = new NbtCompound();
        this.nbt.copyFrom(input);
    }

    public void copyFrom(NbtCompound input)
    {
        this.nbt.copyFrom(input);
    }

    @Override
    public int getVersion()
    {
        return this.protocolVersion;
    }

    @Override
    public int getPacketType()
    {
        return 0;
    }

    @Override
    public int getTotalSize()
    {
        int totalSize = 8;

        if (this.nbt != null)
        {
            totalSize += this.nbt.getSizeInBytes();
        }

        return totalSize;
    }

    @Override
    public boolean isEmpty()
    {
        return this.nbt.isEmpty();
    }

    @Override
    public void toPacket(PacketByteBuf output)
    {
        output.writeString(this.modName);
        output.writeString(this.modVersion);
        output.writeInt(this.protocolVersion);
        if (!this.nbt.isEmpty())
        {
            output.writeBoolean(true);
            output.writeNbt(this.nbt);
        }
        else
        {
            output.writeBoolean(false);
        }
    }

    public static TestData fromPacket(PacketByteBuf input)
    {
        return new TestData(input.readString(), input.readString(), input.readInt(), input.readBoolean() ? input.readNbt() : null);
    }

    public void dump()
    {
        MaLiTest.logger.info("TestData --> modName: {}, modVersion {}, protocolVersion: {}", this.modName, this.modVersion, this.protocolVersion);

        if (!this.nbt.isEmpty())
        {
            MaLiTest.logger.info("NBT --> {}", this.nbt.toString());
        }
        else
        {
            MaLiTest.logger.info("NBT --> EMPTY");
        }
    }

    @Override
    public void clear()
    {
        this.modName = "";
        this.modVersion = "";
        this.protocolVersion = -1;
        this.nbt = new NbtCompound();
    }
}
