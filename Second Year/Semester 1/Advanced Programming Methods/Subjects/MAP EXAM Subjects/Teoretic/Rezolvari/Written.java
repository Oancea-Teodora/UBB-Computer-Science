import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Written {
    public static void main(String[] args) {


        //20ian2020

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15);

        System.out.println(numbers.stream()
                .filter(n -> (n % 3 == 0 || n % 2 == 0))
                .map(n -> "A" + Integer.toString(n + 1) + "B")
                .reduce("", (acc, w) -> acc + w));
        System.out.println();

        //22ian2020
        List<Integer> numbers2 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16);

        System.out.println(numbers2.stream()
                .filter(n -> n % 4 == 0)
                .map(n -> n + 1)
                .reduce(0, (acc, n) -> (acc + n) % 2));
        System.out.println();

        //23ian2020
        System.out.println(numbers.stream()
                .filter(n -> (n % 5 == 0 || n % 2 == 0))
                .map(n -> "N" + n + "R")
                .reduce("", (acc, w) -> acc + w));
        System.out.println();

        //24ian2020
        System.out.println(numbers.stream()
                .filter(n -> (n % 3 == 0 || n % 7 == 0))
                .map(n -> n - 1)
                .reduce(0, (acc, n) -> (acc + n) % 5));
        System.out.println();

        //Erasmus2020

//        List<Integer> list = numbers2.stream()
//                .filter(n -> n % 4 == 0)
//                .map(n -> n + 1)
//                .reduce(0, (acc, n) -> (acc + n) % 2);
//
//        List<Integer> list = Collections.singletonList(numbers2.stream()
//                .filter(n -> n % 4 == 0)
//                .map(n -> n + 1)
//                .reduce(0, (acc, n) -> (acc + n) % 2));
//
//        System.out.println(list);

        List<Integer> result2 = numbers2.stream()
                .filter((a) -> a % 4 == 0)
                .map((a) -> a + 1)
                .reduce((acc, n) -> (acc + n) % 2).stream()
                .collect(Collectors.toList());

        System.out.println(result2);

        System.out.println();

        //17ian2019
        List<Integer> listt = numbers2.stream()
                                .filter(n -> n % 4 == 0)
                                .collect(Collectors.toList());
        System.out.println(listt);
        System.out.println();


    }

}