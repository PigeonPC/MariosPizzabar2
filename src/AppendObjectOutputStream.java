

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


//nedarvning af ObjectOutputStream med det formaal at override writeStreamHeader()
//ObjectOutputStream class er kun lavet til overwrite
//Den tilfoejer en header til de streamede data
//Hvis man tvinger den til at udvide en fil med flere gange write saa kommer der flere headers og filen bliver ulaeselig
//override af method forhindrer dette
public class AppendObjectOutputStream extends ObjectOutputStream
{
    // constructor med throws som goer try/catch obligatorisk naar man skaber nyt objekt af denne class
    public AppendObjectOutputStream(OutputStream out) throws IOException
    {
        // sender parameter videre til constructor i superclass som er ObjectOutputStream
        super(out);
    }

    @Override


    protected void writeStreamHeader() throws IOException
    {
        //call reset() i ObjectOutputStream som goer at gamle data bliver genskrevet med den nye header
        reset();
    }
}
