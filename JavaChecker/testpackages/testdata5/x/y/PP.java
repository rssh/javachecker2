package x.y;


public class PP<T>
{

   public Pair<T,T>  dupT(T t)
    { return new Pair<T,T>(t,t); }


   public static class PPP extends PP<Pair<Number,Integer> >
   {

      public PPP(long x, long y)
      { lp=new Pair<Long,Long>(x,y); }

      public PPP(int x, int y)
      { lp=new Pair<Long,Long>((long)x,(long)y); }


      public long sum()     
      {
       return lp.getT()+lp.getU();
      }


      private Pair<Long,Long> lp;
   }

   public static void main(String[] args)
   {

    PPP ppp = new PPP(10L,10L);

    Pair<Number,Integer> q = new Pair<Number,Integer>(5,10);

    PP<Pair<Number,Integer> > qq = new PP<Pair<Number,Integer>>();

    Pair<Pair<Number,Integer>,Pair<Number,Integer> > dq = qq.dupT(q);
 

   }


}