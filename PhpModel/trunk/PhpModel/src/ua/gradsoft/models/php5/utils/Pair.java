
package ua.gradsoft.models.php5.utils;

import java.io.Serializable;

/**
 *Pair(x,y)
 * @author rssh
 */
public class Pair<X,Y> implements Serializable
{

    public Pair(X x, Y y)
    {
      frs=x;
      snd=y;
    }

    public X frs;
    public Y snd;
}
