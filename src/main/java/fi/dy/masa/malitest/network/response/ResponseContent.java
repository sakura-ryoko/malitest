package fi.dy.masa.malitest.network.response;

import net.minecraft.util.Identifier;

public class ResponseContent
{
    private final Identifier type;
    private final int response;

    public ResponseContent(Identifier type, int response)
    {
        this.type = type;
        this.response = response;
    }

    public Identifier getType() { return this.type; }
    public int getResponse() { return this.response; }
}
