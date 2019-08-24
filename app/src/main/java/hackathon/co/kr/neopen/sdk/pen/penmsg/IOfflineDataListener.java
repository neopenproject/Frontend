package hackathon.co.kr.neopen.sdk.pen.penmsg;

import hackathon.co.kr.neopen.sdk.ink.structure.Stroke;
import hackathon.co.kr.neopen.sdk.metadata.structure.Symbol;

/**
 * Created by LMS on 2016-02-16.
 */
public interface IOfflineDataListener
{
    /**
     * On receive offline strokes.
     *
     * @param penAddress the pen address
     * @param strokes    the strokes
     * @param sectionId  the section id
     * @param ownerId    the owner id
     * @param noteId     the note id
     * @param symbols   the detected symbols
     */
    public void onReceiveOfflineStrokes(String penAddress, Stroke[] strokes, int sectionId, int ownerId, int noteId, Symbol[] symbols);

}
