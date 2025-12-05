import java.io.IOException;
import java.util.regex.Pattern;

record IdRange(long start, long end) { }

Pattern rangeDelimiter = Pattern.compile(",");
Pattern idDelimiter = Pattern.compile("([0-9]+)-([0-9]+)");

Stream<IdRange> ranges() throws IOException {
    return rangeDelimiter
        .splitAsStream(Files.readString(Path.of("sample.txt")))
        .map(r -> {
            Matcher m = idDelimiter.matcher(r);
            m.find();
            return new IdRange(Long.parseLong(m.group(1)), Long.parseLong(m.group(2)));
        });
}

Stream<Long> expand(IdRange range) {
    return LongStream.rangeClosed(range.start(), range.end()).boxed();
}

boolean containsOnlyDuplicates(Long id) {
    String idDigits = id.toString();
    int len = idDigits.length();
    if (len % 2 != 0) return false;

    int half = len / 2;

    String first = idDigits.substring(0, half);
    String last = idDigits.substring(half, len);

    return first.equals(last);
}

void main() throws IOException {
    long total = ranges()
        .flatMap(r -> expand(r).filter(i -> containsOnlyDuplicates(i)))
        .reduce((sum, i) -> sum + i).orElseThrow();

    IO.println("Total of Invalid IDs: %d".formatted(total));
}
