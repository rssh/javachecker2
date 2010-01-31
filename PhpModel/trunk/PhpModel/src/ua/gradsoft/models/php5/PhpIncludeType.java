package ua.gradsoft.models.php5;

/**
 *Type of include
 * @author rssh
 */
public enum PhpIncludeType {

    INCLUDE(false,false),
    INCLUDE_ONCE(false,true),
    REQUIRE(true,false),
    REQUIRE_ONCE(true,true)
    ;

    PhpIncludeType(boolean isRequire, boolean isOnce)
    {
      this.isRequire=isRequire;
      this.isOnce=isOnce;
    }

    public boolean isRequire()
    { return isRequire; }

    public boolean isOnce()
    { return isOnce; }

    private boolean isRequire;
    private boolean isOnce;
}
