record Range(long start, long end) {}

List<Range> ranges = new ArrayList<>();
Set<Long> freshIngredients = new HashSet<>();
List<Range> allFreshIngredients = new ArrayList<>();

void parseRange(String line) {
    String[] range = line.split("-");
    ranges.add(new Range(Long.parseLong(range[0]), Long.parseLong(range[1])));
}

boolean inAnyRange(Long ingredient) {
    return ranges.stream().anyMatch(r -> ingredient >= r.start() && ingredient <= r.end());
}

void sortRanges() {
    ranges.sort((r1, r2) -> Long.compare(r1.start(), r2.start()));
}

void tallyAllFreshIngredients() {
    Range nextRange = new Range(ranges.get(0).start(), ranges.get(0).end());

    for (int i=1; i < ranges.size(); ++i) {
        Range curr = ranges.get(i);
        if (nextRange.end() < curr.start()) {
            allFreshIngredients.add(nextRange);
            // is there a record clone pattern?
            nextRange = new Range(curr.start(), curr.end());
        } else if (curr.start() >= nextRange.start() && curr.end() <= nextRange.end()) {
            continue;
        } else if (curr.start() >= nextRange.start() && curr.end() > nextRange.end()) {
            nextRange = new Range(nextRange.start(), curr.end());
        }
    }

    allFreshIngredients.add(nextRange);
}

void validateAllFreshIngredients() {
    for (int i=1; i < allFreshIngredients.size(); ++i) {
        Range prev = allFreshIngredients.get(i-1), curr = allFreshIngredients.get(i);
        assert prev.end() < curr.start()  : "misaligned: prev:%s <-> curr:%s".formatted(prev, curr);
        assert prev.start() <= prev.end() : "invalid range: %s".formatted(prev);
        assert curr.start() <= curr.end() : "invalid range: %s".formatted(curr);
    }
}

void parseDatabase(Path p) throws NumberFormatException, IOException {
    boolean parsingRanges = true;
    for (String line : Files.readAllLines(p)) {
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
}

long countAllFreshIngredients() {
    return allFreshIngredients.stream().mapToLong(r -> (r.end() - r.start()) + 1).sum();
}

void main(String[] args) throws IOException {
    parseDatabase(Path.of(args[0]));

    IO.println("freshIngredients: %d".formatted(freshIngredients.size()));

    sortRanges();

    tallyAllFreshIngredients();

    validateAllFreshIngredients();

    IO.println("allPossibleIngredients: %d".formatted(countAllFreshIngredients()));
}
