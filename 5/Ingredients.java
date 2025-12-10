record Range(int start, int end) {}

List<Range> ranges = new ArrayList<>();
Set<Integer> ingredients = new HashSet<>();

void parseRange(String line) {
    String[] range = line.split("-");
    ranges.add(new Range(Integer.parseInt(range[0]), Integer.parseInt(range[1])));
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
            ingredients.add(Integer.parseInt(line));
        }
    }

    IO.println(ranges);
    IO.println(ingredients);
}

