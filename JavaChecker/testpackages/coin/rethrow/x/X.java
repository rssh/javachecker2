package x;

import java.sql.Connection;
import java.sql.SQLException;

public class X
{

  public void doSomething(Connection cn, String query)
  {
    try {
      cn.createStatement().executeUpdate(query);
    }catch(SQLException ex){
      throw new RuntimeException("sql exception occured",ex);
    }
  }

}
