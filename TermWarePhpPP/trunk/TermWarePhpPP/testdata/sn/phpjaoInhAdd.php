<?

class Y
{
  public function __construct()
  { $this->arr = array('a'=>1); }

  public $arr;
}

class X extends Y
{
  public function __construct()
  {
    parent::__construct();
    $this->arr = $this->arr + array('b'=>2);
  }
}

#$x = new X();
#var_dump($x->arr);

?>
