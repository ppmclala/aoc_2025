record Battery(int pos, char label) {}

List<Battery> selectBatteries(String bank, int start, int guard, List<Battery> batteries) {
    int maxPosition = Integer.MIN_VALUE;
    char max = Character.MIN_VALUE;
    for (int i = start; i < bank.length() - (guard-1); ++i) {
        char label = bank.charAt(i);
        if (label > max) {
            maxPosition = i;
            max = label;
        }
    }

    batteries.add(new Battery(maxPosition, max));

    if (--guard > 0) {
        return selectBatteries(bank, maxPosition + 1, guard, batteries);
    } else {
        return batteries;
    }
}

long calcJoltage(List<Battery> batteries) {
    String selectedBatteries = batteries
        .stream()
        .map(b -> String.valueOf(b.label()))
        .collect(Collectors.joining(""));
    return Long.parseLong(selectedBatteries);
}

void main(String[] args) throws IOException {
    var numBatteries = Integer.parseInt(args[1]);
    var totalJoltage = Files.readAllLines(Path.of(args[0]))
        .stream()
        .map(l -> selectBatteries(l, 0, numBatteries, new ArrayList<>()))
        .map(this::calcJoltage)
        .reduce((sum, j) -> sum + j).orElseThrow();

    IO.println("Total Joltage: %d".formatted(totalJoltage));
}
