package x;

import java.util.List;

public class X
{

 public static void fun1(String[] args)
 {
   for(int i=0; i<args.length; ++i) {
     System.out.println("args["+i+"]="+args[i]);
   }
 }

 public static void fun2(List<String> args)
 {
   for(int i=0; i<args.size(); ++i) {
     System.out.println("args["+i+"]="+args.get(i));
   }
 }

 public static void fun3(List<String> args)
 {
   for(int i=0; i<args.size(); i++) {
     System.out.println("args["+i+"]="+args.get(i));
   }
 }

 public static void fun4(String[][] ss)
 {
   for(int i=0; i<ss[0].size(); i++) {
     System.out.println("args["+i+"]="+ss[0].get(i));
   }
 }

 public static void fun5(String[] args)
 {
   int i;
   for(i=0; i<args.length; ++i) {
     System.out.println("args["+i+"]="+ss[0].get(i));
   }
 }

}
