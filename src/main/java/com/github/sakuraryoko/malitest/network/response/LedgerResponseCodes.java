package com.github.sakuraryoko.malitest.network.response;

public enum LedgerResponseCodes
{
    NO_PERMISSION(0),
    EXECUTING(1),
    COMPLETED(2),
    ERROR(3),
    BUSY(4);

    LedgerResponseCodes(int code)
    {}
}
