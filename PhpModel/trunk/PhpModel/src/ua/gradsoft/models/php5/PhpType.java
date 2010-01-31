package ua.gradsoft.models.php5;

/**
 *Types for php.
 *See http://www.php.net/manual/en/language.types.php
 * @author rssh
 */
public enum PhpType
{

    BOOLEAN(1),
    INTEGER(2),
    FLOAT(3),
    STRING(4),
    ARRAY(5),
    OBJECT(6),
    RESOURCE(7),
    NULL(8);

    PhpType(int order)
    { this.order=order; }
            
    public int getOrder()
    { return order; }

    private int order;

}
