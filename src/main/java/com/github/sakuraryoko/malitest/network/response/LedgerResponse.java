package com.github.sakuraryoko.malitest.network.response;

import net.minecraft.util.Identifier;

public class LedgerResponse
{
    public Identifier type;
    public Integer response;

    public LedgerResponse(Identifier type, Integer response)
    {
        this.type = type;
        this.response = response;
    }

    public Identifier getType()
    {
        return this.type;
    }

    public Integer getResponse()
    {
        return this.response;
    }
}
