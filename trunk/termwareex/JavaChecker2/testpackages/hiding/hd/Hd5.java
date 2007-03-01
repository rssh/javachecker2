package hd;

class Hd5Entry {
  int value;
  Hd5Entry next;
}


public final class Hd5
{

 Hd5Entry  entries;


 boolean m0(Hd5Entry x)
 {
   for(Hd5Entry e=entries; e!=null; e=e.next) {
      if (e==x) return true;
   }
   return false;
 }

 boolean m1(int x)
 {
   for(Hd5Entry e=entries; e!=null; e=e.next) {
      if (e.value==x) return true;
   }
   return false;
 }


}