<?
class JavaClassDescription extends PHPJAOClassDescription
{
   public function __construct()
   {
     $this->javaClass='java.lang.Class';
     $this->typesOfFields=array(
                            'name' => 'java.lang.String',
                            'value' => 'java.util.ByteBuffer'
                          );
   }
   public function defaultConstruct()
   {
     return new JavaClass();
   }
}

class JavaClass extends PHPJAOPOJOBase
{
  static $classDescription;
  public function getClassDescription()
  { return self::$classDescription; }
  public $x;
  public $y;
}

JavaClass::$classDescription=new PHPJAOClassDescription();
PHPJAO::registerType('java.lang.Class',JavaClass::$classDescription)


?>
