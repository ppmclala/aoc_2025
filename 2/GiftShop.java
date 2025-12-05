import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

record IdRange(long start, long end) { }

Pattern rangeDelimiter = Pattern.compile(",");
Pattern idDelimiter = Pattern.compile("([0-9]+)-([0-9]+)");
Map<Integer, Pattern> ngramPatterns = new HashMap<>();

Pattern ngramPattern(int n) {
    return ngramPatterns.getOrDefault(n, Pattern.compile("\\d{%d}".formatted(n)));
}

Stream<IdRange> ranges() throws IOException {
    return rangeDelimiter
    .splitAsStream(Files.readString(Path.of("input.txt")))
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
    int half = len / 2;

    for(int n = 1; n <= half; n++) {
        Matcher ngramMatcher = ngramPattern(n).matcher(idDigits);
        Map<String, Integer> ngrams = new HashMap<>();

        while (ngramMatcher.find()) {
            ngrams.merge(ngramMatcher.group(), 1, Integer::sum);
        }

        int totalLength = ngrams
            .entrySet()
            .stream()
            .map(e -> e.getKey().length() * e.getValue())
            .reduce((sum, ng) -> sum + ng).orElseThrow();

        if (totalLength == len && ngrams.size() == 1 && (Integer) ngrams.values().toArray()[0] == 2) {
            return true;
        }
    }

    return false;
}

void main() throws IOException {
    long total = ranges()
        .flatMap(r -> expand(r).filter(i -> containsOnlyDuplicates(i)))
        .reduce((sum, i) -> sum + i).orElseThrow();

    IO.println("Total of Invalid IDs: %d".formatted(total));
}
