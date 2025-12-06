record IdRange(long start, long end) { }

Pattern rangeDelimiter = Pattern.compile(",");
Pattern idDelimiter = Pattern.compile("([0-9]+)-([0-9]+)");
Map<Integer, Pattern> ngramPatterns = new HashMap<>();

Pattern ngramPattern(int n) {
    return ngramPatterns.getOrDefault(n, Pattern.compile("\\d{%d}".formatted(n)));
}

Stream<Long> expand(IdRange range) {
    return LongStream.rangeClosed(range.start(), range.end()).boxed();
}

static final Integer ANY = Integer.MAX_VALUE;

boolean hasRequiredNGrams(Integer numNgrams, Map<String, Integer> ngrams) {
    if (numNgrams.equals(ANY)) return true;

    return ngrams.values().toArray()[0] == numNgrams;
}

boolean containsOnlyDuplicates(Long id, Integer numNgrams) {
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

        boolean singleNgramMatchesWholeId = totalLength == len && ngrams.size() == 1;
        if (singleNgramMatchesWholeId && hasRequiredNGrams(numNgrams, ngrams)) {
            return true;
        }
    }

    return false;
}

void main(String[] args) throws IOException {
    Integer numNgrams = (args.length >= 2 && !args[1].isBlank()) ? Integer.parseInt(args[1]) : ANY;

    long total = rangeDelimiter
        .splitAsStream(Files.readString(Path.of(args[0])))
        .map(r -> {
            Matcher m = idDelimiter.matcher(r);
            m.find();
            return new IdRange(Long.parseLong(m.group(1)), Long.parseLong(m.group(2)));
        })
        .flatMap(r -> expand(r).filter(i -> containsOnlyDuplicates(i, numNgrams)))
        .reduce((sum, i) -> sum + i).orElseThrow();

    IO.println("Total of Invalid IDs: %d".formatted(total));
}
