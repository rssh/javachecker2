<?php
interface a
{
    const b = 'Interface constant';
}

// Prints: Interface constant
echo a::b;


// This will however not work because its not allowed to 
// override constants. This is the same concept as with 
// class constants
class b implements a
{
    const b = 'Class constant';
}
?>

