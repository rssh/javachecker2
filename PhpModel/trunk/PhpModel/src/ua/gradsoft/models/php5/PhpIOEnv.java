
package ua.gradsoft.models.php5;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 *Input/Output environment
 * @author rssh
 */
public class PhpIOEnv {


    public Writer getOutWriter()
    { return outWriter; }
    
    public void setOutWriter(Writer outWriter)
    { this.outWriter = outWriter; }
    
    public Writer getErrWriter()
    { return errWriter; }
    
    public void setErrWriter(Writer errWriter)
    { this.errWriter = errWriter; }

    /**
     * find file in includes, or null if one does not exists.
     * @param name
     * @return
     */
    public File findIncludeFile(String name)
    {
        // try without seatch
        File f = new File(name);
        if (f.exists()) return f;
        for(String dirs:includes) {
            String fullName;
            if (dirs.endsWith("/")) {
                fullName=dirs+name;
            } else {
                fullName=dirs+"/"+name;
            }
            f = new File(fullName);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    private List<String> includes;
    //private Reader       inReader = new InputStreamReader(System.in);
    private Writer       outWriter = new OutputStreamWriter(System.out);
    private Writer       errWriter = new OutputStreamWriter(System.err);

}
