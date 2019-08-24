package hackathon.co.kr.neopen.sdk.metadata;

import hackathon.co.kr.neopen.sdk.metadata.structure.Symbol;

public interface IMetadataListener
{
    void onSymbolDetected(Symbol[] symbols);
}
