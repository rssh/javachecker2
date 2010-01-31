package ua.gradsoft.models.php5;

/**
 *Model for strings
 * @author rssh
 */
public class PhpStringModel extends PhpPrimitiveValueModel {


    public PhpStringModel()
    {
      s="";
    }

    public PhpStringModel(String s)
    {
      this.s=s;
    }


    public boolean getBoolean() {
        return (s.length()!=0 && !s.equals("0"));
    }

    public double getFloat() {
        try {
            return Float.parseFloat(s);
        }catch(NumberFormatException ex){
            return 0.0;
        }
    }

    public int getInt() {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex){
            return 0;
        }
    }

    public String getString(PhpEvalEnvironment pee) {
        return s;
    }

    public PhpType getType() {
        return PhpType.STRING;
    }

    private String s;
}
