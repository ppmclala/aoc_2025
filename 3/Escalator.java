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
        secondMax = bank.substring(maxPosition + 1).chars().max().orElseThrow();
    }

    return (max-48) * 10 + (secondMax-48);
}

void main() throws IOException {
    var totalJoltage = Files.readAllLines(Path.of("input.txt"))
        .stream()
        .map(l -> calculateJoltage(l))
        .reduce((sum, j) -> sum + j).orElseThrow();

    IO.println("Total Joltage: %d".formatted(totalJoltage));
}
