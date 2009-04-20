package x;

import java.sql.Connection;
import java.sql.SQLException;

public class X
{

  public void doQueryAndClose(Connection cn, String query)
  {
    try {
      cn.createStatement().executeUpdate(query);
    }finally{
       try {
         cn.close();
       }catch(SQLException e){
         /* ignore */
       }
    }

  }

}
