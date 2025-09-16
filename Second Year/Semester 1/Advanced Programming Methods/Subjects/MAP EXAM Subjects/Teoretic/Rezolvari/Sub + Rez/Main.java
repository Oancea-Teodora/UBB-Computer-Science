import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    //
    // 24.01.2020
    //

    // 1. Compare interfaces vs. abstract classes in Java.

    // I. Abstract classes can have constructors but interfaces can’t have constructors.
    // II. Abstract classes can have methods with implementation whereas interface provides absolute abstraction and can’t have any method implementations. Note that from Java 8 onwards, we can create default and static methods in interface that contains the method implementations.
    // III. A subclass can extend only one abstract class, but it can implement multiple interfaces.
    // IV. Abstract classes can extend other class and implement interfaces but interface can only extend other interfaces.
    // V. Interfaces are used to define contract for the subclasses whereas abstract class also define contract, but it can provide other methods implementations for subclasses to use.

    // 2. given the following collection
    // List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15);
    // Using Java functional style (Java streams),
    // please write a program that is doing the following operations in the following order:
    // a)eliminates all the numbers which are not multiple of  3 or  multiple of 7;
    // b)transform each remaining number into its predecessor (e.g. 3 is transformed into 2);
    // c)compute the sum modulo 5 of the remaining numbers (e.g. (2 +4) mod 5=1)

    // List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15);
    // System.out.println(numbers.stream()
    //      .filter(number -> number % 3 == 0 || number % 7 == 0) // a
    //      .map(number -> number - 1) // b
    //      .mapToInt(Integer::intValue)
    //      .sum() % 5); // c

    // 3.Given the following four classes in Java:
    // class A implements D{...}
    // class B extends A implements D {...}
    // class C extends A implements D {...}
    // interface D {...}
    // class Amain
    // {
    //      D  method1(ArrayList<...> list)
    //      {
    //          if list.isEmpty() return null;
    //          else return list.get(1);
    //      }
    //      void method2(ArrayList<...>  list, C elem)
    //      {list.add(elem);}
    //      void method3(C elem)
    //      {
    //          ArrayList<A> listA=new ArrayList<A>(); listA.add(new A());listA.add(new A());
    //          ArrayList<B> listB = new ArrayList<B>(); listB.add(new B());listB.add(new B());
    //          ArrayList<C> listC = new ArrayList<C>(); listC.add(new C()); listC.add(new C());
    //          this.method1(listA); this.method1(listB); this.method1(listC);
    //          this.method2(listA,elem); this.method2(listB,elem); this.method2(listC,elem);
    //      }
    // }
    // Compute the most specific signatures for the class Amain methods (method1 and method2)
    // such that the entire program is correct.
    // If it is not possible to find the types justify your answer.
    // Discuss line by line the correctness of the above program.

    // interface D{}
    // class A implements D{}
    // class B extends A implements D{}
    // class C extends A implements D{}
    // class Amain
    // {
    //      D  method1(ArrayList<? extends A> list)
    //      {
    //          if (list.isEmpty()) return null;
    //          else return list.get(1);
    //      }
    //      void method2(ArrayList<? super C>  list, C elem)
    //      {list.add(elem);}
    //      void method3(C elem)
    //      {
    //          ArrayList<A> listA = new ArrayList<A>(); listA.add(new A());listA.add(new A());
    //          ArrayList<B> listB = new ArrayList<B>(); listB.add(new B());listB.add(new B());
    //          ArrayList<C> listC = new ArrayList<C>(); listC.add(new C()); listC.add(new C());
    //          this.method1(listA); this.method1(listB); this.method1(listC);
    //          this.method2(listA,elem); this.method2(listB,elem); this.method2(listC,elem); // impossible to add C in listB
    //      }
    // }

    // 4. What is an Atomic Variable in Java.

    // In Java, an atomic variable refers to a variable that can be read and written atomically, meaning that the read and write operations are indivisible and cannot be interrupted by other threads. This ensures that the variable is always in a consistent state, even in a multithreaded environment.


    //
    // 22.01.2024
    //

    // 1. Given the following Java collection:
    // List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14,15);
    // Using Java functional style (Java streams), please write a Java stream program that is doing
    // the following operations in the following order:
    // a)keep only the numbers which are either multiple of 5 or multiple of 2;
    // b)transform each remaining number into a string, that consists of a prefix "N", the
    // number and the suffix "R" (e.g. 5 is transformed into "N5R")
    // c)concatenate all the strings

    // List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14,15);
    // System.out.println(numbers.stream()
    //      .filter(number -> number % 5 == 0 || number % 2 == 0)
    //      .map(number -> "N" + number + "R")
    //      .collect(Collectors.joining())); // or .reduce("", (s1, s2) -> s1 + s2);

    // 2. Given the following four classes in Java:
    // class A {...} class B extends A {...} class C extends A {...}
    // class Amain
    // {
    //      ... method1(... list) { return list.get(1);}
    //      void method2(... list, A el) { list.add(el);}
    //      void method3(A elem)
    //      {
    //          ArrayList<A> listA=new ArrayList<A>(); listA.add(new B());listA.add(new C());
    //          ArrayList<B> listB = new ArrayList<B>(); listB.add(new B());listB.add(new B());
    //          ArrayList<C> listC = new ArrayList<C>(); listC.add(new C());listC.add(new C());
    //          this.method1(listA); this.method1(listB); this.method1(listC);
    //          this.method2(listA,elem); this.method2(listB,elem); this.method2(listC,elem);
    //      }
    // }
    // Please complete the most specific wildcard types for the class Amain methods (method1 and
    // method2) such that the entire program is correct. Please justify your solution. If it is not
    // possible to find a solution please explain the reason.

    // class A {}
    // class B extends A {}
    // class C extends A {}
    // class Amain
    // {
    //      A method1(List<? extends A> list)
    //      {return list.get(1);}
    //      void method2(List<? super A> list, A el)
    //      {list.add(el);}
    //      void method3(A elem)
    //      {
    //          ArrayList<A> listA = new ArrayList<A>();
    //          listA.add(new B());
    //          listA.add(new C());
    //          ArrayList<B> listB = new ArrayList<B>();
    //          listB.add(new B());
    //          listB.add(new B());
    //          ArrayList<C> listC = new ArrayList<C>();
    //          listC.add(new C());
    //          listC.add(new C());
    //          this.method1(listA);
    //          this.method1(listB);
    //          this.method1(listC);
    //          this.method2(listA, elem);
    //          this.method2(listB, elem);
    //          this.method2(listC, elem); // cannot add an element of base class A
    //       }
    // }

    // 3. Is the following Java code correct? Please explain your answer.
    // class A
    // {
    //      protected int f1;
    //      static int s1=0;
    //      public A(int a) { this.f1=a*s1;s1=s1+1; }
    //      static int getS() { return getS1(s1); }
    //      int getS1(int x) {return (x*getS())};
    // }

    // class A
    // {
    //      protected int f1;
    //      static int s1=0;
    //      public A(int a) { this.f1=a*s1;s1=s1+1; }
    //      static int getS() { return getS1(s1); }// Non-static method 'getS1(int)' cannot be referenced from a static context
    //      int getS1(int x) {return (x*getS());}// Infinite loop
    // }



    //
    // 22.01.2024
    //

    // 1. Compare static vs non-static methods in Java.

    // a static method belongs to the class itself and a non-static (aka instance) method belongs to each object that is
    // generated from that class.

    // 2. Given the following collection
    // List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8,9,10,11,12,14,15,16);
    // Using Java functional style (Java streams),
    // please write a program that is doing the following operations in the following order:
    // a)eliminates all the numbers which are not multiple of  4;
    // b)transform each remaining number into its successor (e.g. 4 is transformed into 5);
    // c)compute the sum modulo 2 of the remaining numbers (e.g. (9 +5) mod 2=0)

    // List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8,9,10,11,12,14,15,16);
    //        System.out.println(numbers.stream()
    //                .filter(number -> number % 4 == 0)
    //                .map(number -> number + 1)
    //                .mapToInt(Integer::intValue)
    //                .sum() % 2);

    // 3. Given the following four classes in Java:
    // class A {...}   class B extends A {...}   class C extends A {...}
    // class Amain
    // {
    //      A  method1(ArrayList<...> list)
    //      {
    //          if list.isEmpty() return null;
    //          else return list.get(1);
    //      }
    //      void method2(ArrayList<...>  list) {  list.add(null);}
    //      void method3()
    //      {
    //          ArrayList<A> listA=new ArrayList<A>(); listA.add(new A());
    //          ArrayList<B> listB = new ArrayList<B>(); listB.add(new B());
    //          ArrayList<C> listC = new ArrayList<C>(); listC.add(new C());
    //          this.method1(listA); this.method1(listB); this.method1(listC);
    //          this.method2(listA); this.method2(listB); this.method2(listC);
    //      }
    // }
    // Compute the most specific signatures for the class Amain methods (method1 and method2)
    // such that the entire program is correct.
    // If it is not possible to find the types justify your answer.
    // Discuss line by line the correctness of the above program.

    // class A {}   class B extends A {}   class C extends A {}
    // class Amain
    // {
    //      A  method1(ArrayList<? extends A> list)
    //      {
    //          if (list.isEmpty()) return null;
    //          else return list.get(1); // IndexOutOfBoundException
    //      }
    //      void method2(ArrayList<? extends A>  list) {  list.add(null);}
    //      void method3()
    //      {
    //          ArrayList<A> listA=new ArrayList<A>(); listA.add(new A());
    //          ArrayList<B> listB = new ArrayList<B>(); listB.add(new B());
    //          ArrayList<C> listC = new ArrayList<C>(); listC.add(new C());
    //          this.method1(listA); this.method1(listB); this.method1(listC);
    //          this.method2(listA); this.method2(listB); this.method2(listC);
    //      }
    // }

    // 4. What is a Semaphore in Java.

    // In Java, a Semaphore is a synchronization primitive that is used to control access to a specific resource or a group of resources.
    // It provides a way to limit the number of threads that can access the resource concurrently.
    // Semaphores can be thought of as permits that are granted to control the number of threads that are allowed to perform
    // a particular operation.
    public static void main(String[] args)
    {


    }
}
