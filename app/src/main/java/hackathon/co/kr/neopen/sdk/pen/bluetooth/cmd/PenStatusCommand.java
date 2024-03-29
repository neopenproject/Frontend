package hackathon.co.kr.neopen.sdk.pen.bluetooth.cmd;

import hackathon.co.kr.neopen.sdk.pen.bluetooth.comm.CommProcessor;
import hackathon.co.kr.neopen.sdk.pen.bluetooth.lib.ProtocolParser;
import hackathon.co.kr.neopen.sdk.pen.bluetooth.lib.ProtocolParser20;

/**
 * The type Pen status command.
 *
 * @author CHY
 */
public class PenStatusCommand extends Command
{
    /**
     * Instantiates a new Pen status command.
     *
     * @param key  the key
     * @param comp the comp
     */
    public PenStatusCommand(int key, CommandManager comp)
    {
        super(key, comp);
    }
    
    protected void write() 
    {
        if(comp instanceof CommProcessor )
            comp.write( ProtocolParser.buildPenStatusData() );
        else
            comp.write( ProtocolParser20.buildPenStatusData() );
    }
}
