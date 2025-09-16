import java.util.Arrays;
import java.util.List;

public class Main {
    public static void P1(List<String> words) {
        // P1. Loop down the words and print each on a separate line, with two spaces in front
        // of each word. Don’t use map.
        words
                .forEach(s -> System.out.println("  " + s));
    }

    public static void P2(List<String> words) {
        // P2. Repeat the previous problem, but without the two spaces in front. This is trivial
        // if you use the same approach as in P1, so the point is to use a method reference here,
        // as opposed to an explicit lambda as in P1.
        words
                .stream()
                .forEach(System.out::println);
    }

    public static void P3(List<String> words) {
        // List<String> excitingWords = StringUtils.transformedList(words, s -> s + "!");
        // List<String> eyeWords = StringUtils.transformedList(words, s -> s.replace("i", "eye"));
        // List<String> upperCaseWords = StringUtils.transformedList(words, String::toUpperCase);

        words
                .stream()
                .map(s -> s + "!")
                .forEach(s -> System.out.print(s + " "));

        System.out.println();

        words
                .stream()
                .map(s -> {
                    if (s.equals("i")) {
                        return "eye";
                    }
                    else {
                        return s;
                    }
                })
                .forEach(s -> System.out.print(s + " "));

        System.out.println();

        words
                .stream()
                .map(String::toUpperCase)
                .forEach(s -> System.out.print(s + " "));
    }

    public static void P4(List<String> words) {
        // List<String> shortWords = StringUtils.allMatches(words, s -> s.length() < 4);
        // List<String> wordsWithB = StringUtils.allMatches(words, s -> s.contains("b"));
        // List<String> evenLengthWords = StringUtils.allMatches(words, s -> (s.length() % 2) == 0);
        words
                .stream()
                .filter(s -> s.length() < 4)
                .forEach(s -> System.out.print(s + " "));

        System.out.println();

        words
                .stream()
                .filter(s -> s.contains("b"))
                .forEach(s -> System.out.print(s + " "));

        System.out.println();

        words
                .stream()
                .filter(s -> s.length() % 2 == 0)
                .forEach(s -> System.out.print(s + " "));
    }

    public static void P5(List<String> words) {
        // P5. Turn the strings into uppercase, keep only the ones that are shorter than 4 characters,
        // of what is remaining, keep only the ones that contain “E”, and print the first result. Repeat
        // the process, except checking for a “Q” instead of an “E”. When checking for the “Q”, try to avoid
        // repeating all the code from when you checked for an “E”.
        words
                .stream()
                .filter(s -> s.length() < 4)
                .filter(s -> s.contains("E"))
                .findFirst()
                .ifPresent(System.out::println);

        words
                .stream()
                .filter(s -> s.length() < 4 && s.contains("Q"))
                .findFirst()
                .ifPresent(System.out::println);
    }

    public static void P6(List<String> words) {
        // P6. Produce a single String that is the result of concatenating the uppercase versions of
        // all of the Strings. Use a single reduce operation, without using map.
        String res = words
                .stream()
                .reduce("", (acc, s) -> acc + s.toUpperCase());
        System.out.println(res);
    }

    public static void P7(List<String> words) {
        // P7. Produce the same String as above, but this time via a map operation that turns the words
        // into uppercase, followed by a reduce operation that concatenates them.
        String res = words
                .stream()
                .map(String::toUpperCase)
                .reduce((acc, s) -> acc + s)
                .get();
        System.out.println(res);
    }

    public static void P8(List<String> words) {
        // P8. Produce a String that is all the words concatenated together, but with commas in
        // between. E.g., the result should be "hi,hello,...". Note that there is no comma at the
        // beginning, before “hi”, and also no comma at the end, after the last word. Major hint: there are two
        // versions of reduce discussed in the notes.
        String res = words
                .stream()
                .reduce((acc, s) -> acc + "," + s)
                .get();
        System.out.println(res);
    }

    public static void P9(List<String> words) {
        // P9. Find the total number of characters (i.e., sum of the lengths) of the strings in the List.
        int sum_chars = words
                .stream()
                .map(String::length)
                .reduce(0, Integer::sum);


        // OR
        sum_chars = words
                .stream()
                .mapToInt(String::length)
                .sum();

        System.out.println(sum_chars);
    }

    public static void P10(List<String> words) {
        // P10. Find the number of words that contain an “h”.
        int nr_words_with_h = words
                .stream()
                .filter(s -> s.contains("h"))
                .toList()
                .size();

        // OR
        long long_nr_words_with_h = words
                .stream()
                .filter(s -> s.contains("h"))
                .count();

        System.out.println(nr_words_with_h);
        System.out.println(long_nr_words_with_h);
    }
        public static void main(String[] args) {
        List<String> words = Arrays.asList("hi", "hEl", "how", "am", "i", "doing", "aQa");
//        P1(words);
//        P2(words);
//        P3(words);
//        P4(words);
//        P5(words);
//        P6(words);
//        P7(words);
//        P8(words);
//        P9(words);
        P10(words);
    }
}
