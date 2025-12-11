record Problem(List<Long> vals, char op) {}

List<Problem> problems = new ArrayList<>();

long execOp(Problem p) {
    switch (p.op()) {
        case '*':
            return p.vals().stream().mapToLong(v -> v).reduce((product, v) -> product * v).orElseThrow();
        case '+':
            return p.vals().stream().mapToLong(v -> v).sum();
        default:
            throw new UnsupportedOperationException("Unsupported op: [%c]".formatted(p.op()));
    }
}

record Pos(int x, int y) {}
static final char NONE = 'X';

void processDigits(List<Character> digits, List<Long> vals) {
    if (!digits.isEmpty()) {
        var valStr = digits
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining());
        vals.add(Long.parseLong(valStr));
    }
}

char[][] homework;

void buildHomeworkMap(List<String> lines) {
    homework = new char[lines.size()][lines.get(0).length()];

    for (int y=0; y<lines.size(); ++y) {
        homework[y] = lines.get(y).toCharArray();
    }

    Set<Integer> rowSizes =  new HashSet<>();
    for (int y=0; y<homework.length; ++y) {
        rowSizes.add(homework[y].length);
        for (int x = 0; x < homework[y].length; ++x) {
            IO.print(homework[y][x]);
        }
        IO.println();
    }

    assert rowSizes.size() == 1 : "expected uniform row sizes";
}

void main(String args[]) throws IOException {
    IO.println("Using input: %s".formatted(args[0]));
    List<String> lines = Files.readAllLines(Path.of(args[0]));

    assert !lines.isEmpty() : "expected lines to be non-empty";
    assert lines.get(0) != null : "expected lines to be populated";

    buildHomeworkMap(lines);

    int w = homework[0].length, h = homework.length;
    List<Character> digits = new ArrayList<>();
    List<Long> vals = new ArrayList<>();
    List<Long> results = new ArrayList<>();
    char op = NONE;
    int spaces = 0;

    for (int x = 0; x < w; ++x) {
        processDigits(digits, vals);
        digits.clear();
        spaces = 0;

        for (int y = 0; y < h; ++y) {
            if (homework[y][x] != ' ') {
                if (y == h - 1) {
                    op = homework[y][x]; 
                } else {
                    digits.add(homework[y][x]);
                }
            } else if (++spaces == h) {
                results.add(execOp(new Problem(vals, op)));
                op = NONE;
                vals.clear();
            }
        }
    }
    processDigits(digits, vals);
    results.add(execOp(new Problem(vals, op)));
    var sumOfAllProblems = results.stream().mapToLong(r -> r).sum();

    IO.println("Sum of all problems: %d".formatted(sumOfAllProblems));
}
