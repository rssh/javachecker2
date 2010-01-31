package ua.gradsoft.models.php5;

public interface PhpValueModel extends PhpExpressionModel
{

 public  PhpType         getType();

 public boolean         getBoolean();
 public int             getInt();
 public double          getFloat();
 public String          getString(PhpEvalEnvironment pee);
 public PhpArrayModel   getArray(PhpEvalEnvironment pee);
 public PhpObjectModel  getObject(PhpEvalEnvironment pee);

 public PhpValueModel   copyByValue(PhpEvalEnvironment pee);
 public PhpValueModel   copyByReference(PhpEvalEnvironment pee);

 //public Object   getAsJavaObject();

}
