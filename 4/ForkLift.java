static final char PAPER = '@';

void dumpGrid(char[][] grid) {
    for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[0].length; j++) {
            IO.print(grid[i][j]);
        }
        IO.println();
    }
}

int neighbors(int x, int y, char[][] grid) {
    int n = 0, w = grid[y].length-1, h = grid.length-1;
    // (x-1, y-1)
    if (x-1 >= 0 && y-1 >= 0 && grid[x-1][y-1] == PAPER) n++;
    // (x,   y-1)
    if (y-1 >= 0             && grid[x][y-1] == PAPER) n++;
    // (x+1, y-1)
    if (x+1 <= w && y-1 >= 0 && grid[x+1][y-1] == PAPER) n++;
    // (x-1,   y)
    if (x-1 >= 0             && grid[x-1][y] == PAPER) n++;
    // me
    // (x+1,   y)
    if (x+1 <= w             && grid[x+1][y] == PAPER) n++;
    // (x-1, y+1)
    if (x-1 >= 0 && y+1 <= h && grid[x-1][y+1] == PAPER) n++;
    // (x,   y+1)
    if (y+1 <= h             && grid[x][y+1] == PAPER) n++;
    // (x+1, y+1)
    if (x+1 <= w && y+1 <= h && grid[x+1][y+1] == PAPER) n++;

    return n;
}

void main(String[] args) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(args[0]));
    assert !lines.isEmpty() : "expected some lines";

    char[][] grid = new char[lines.size()][lines.get(0).length()];
    for (int i = 0; i < lines.size(); ++i) {
        String line = lines.get(i);
        for (int j = 0; j < line.length(); j++) {
            grid[i][j] = line.charAt(j);
        }
    }

    dumpGrid(grid);
    int numRolls = 0;
    for (int y = 0; y < grid.length; ++y) {
        for (int x = 0; x < grid[y].length; ++x) {
            if (grid[x][y] == PAPER && neighbors(x, y, grid) < 4) {
                IO.println("accessible roll! (%d,%d)".formatted(x, y));
                numRolls++;
            }
        }
    }

    IO.println("Found %d reachable paper rolls!".formatted(numRolls));
}
