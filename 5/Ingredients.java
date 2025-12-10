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

void sortRanges() {
    ranges.sort((r1, r2) -> Long.compare(r1.start(), r2.start()));
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

    sortRanges();

    // agg fresh in separate list
    // start with first -> nextRange
    // scan:
    //      case disjoint -> collect nextRange; start new nextRange
    //      case enclosed -> skip
    //      case overlap  -> update nextRange.end

    List<Range> freshIngredients = new ArrayList<>();
    Range nextRange = new Range(ranges.get(0).start(), ranges.get(0).end());

    for (int i=1; i < ranges.size(); ++i) {
        Range curr = ranges.get(i);

        if (nextRange.end() < curr.start()) {
            IO.println("DISJOINT: (%s-%s)".formatted(curr, nextRange));
            freshIngredients.add(nextRange);
            // is there a record clone pattern?
            nextRange = new Range(curr.start(), curr.end());
        } else if (curr.start() >= nextRange.start() && curr.end() <= nextRange.end()) {
            IO.println("ENCLOSED: (%s-%s)".formatted(curr, nextRange));
            continue;
        } else if (curr.start() >= nextRange.start() && curr.end() > nextRange.end()) {
            IO.println("OVERLAP: (%s-%s)".formatted(curr, nextRange));
            nextRange = new Range(nextRange.start(), curr.end());
        }
    }

    freshIngredients.add(nextRange);

    for (int i=1; i < freshIngredients.size(); ++i) {
        Range prev = freshIngredients.get(i-1), curr = freshIngredients.get(i);
        assert prev.end() < curr.start()  : "misaligned: prev:%s <-> curr:%s".formatted(prev, curr);
        assert prev.start() <= prev.end() : "invalid ranage: %s".formatted(prev);
        assert curr.start() <= curr.end() : "invalid ranage: %s".formatted(curr);
    }

    var ingredientCount = freshIngredients
        .stream()
        .mapToLong(r -> (r.end() - r.start()) + 1)
        .sum();

    IO.println("freshIngredients: %s".formatted(freshIngredients));
    IO.println("allPossibleIngredients: %d".formatted(ingredientCount));
}
