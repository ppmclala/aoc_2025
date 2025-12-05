int maxDigit(String s) {
    return s.chars().max().orElseThrow();
}

long calculateJoltage(String bank) {
    int maxPosition = Integer.MIN_VALUE;
    char max = Character.MIN_VALUE, prevMax = Character.MIN_VALUE;
    for (int i = 0; i < bank.length(); ++i) {
        char batteryLabel = bank.charAt(i);
        if (batteryLabel > max) {
            prevMax = max;
            maxPosition = i;
            max = batteryLabel;
        }
    }

    int secondMax;
    if (maxPosition == bank.length() - 1) {
        secondMax = max;
        max = prevMax;
    } else {
        secondMax = maxDigit(bank.substring(maxPosition + 1));
    }

    IO.println("%d:%d:%d".formatted(max-48, secondMax, (max-48) * 10 + (secondMax-48)));
    return (max-48) * 10 + (secondMax-48);
}

void main() throws IOException {
    var totalJoltage = Files.readAllLines(Path.of("input.txt"))
        .stream()
        .map(l -> calculateJoltage(l))
        .reduce((sum, j) -> sum + j).orElseThrow();

    IO.println("Total Joltage: %d".formatted(totalJoltage));
}
