record Range(long start, long end) {}

List<Range> ranges = new ArrayList<>();
Set<Long> freshIngredients = new HashSet<>();

void parseRange(String line) {
    String[] range = line.split("-");
    ranges.add(new Range(Long.parseLong(range[0]), Long.parseLong(range[1])));
}

boolean inAnyRange(Long ingredient) {
    return ranges.stream().anyMatch(r -> ingredient >= r.start() && ingredient <= r.end());
}

void main(String[] args) throws IOException {
    boolean parsingRanges = true;
    for (String line : Files.readAllLines(Path.of(args[0]))) {
        if (line.isBlank()) {
            parsingRanges = false;
            continue;
        }
        if (parsingRanges) {
            parseRange(line);
        } else {
            Long ingredient = Long.parseLong(line);
            if (inAnyRange(ingredient)) freshIngredients.add(ingredient);
        }
    }

    IO.println("freshIngredients: %d".formatted(freshIngredients.size()));
}

