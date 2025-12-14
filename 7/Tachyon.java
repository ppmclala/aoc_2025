import java.util.ArrayList;

char[][] manifold;
Set<Integer> beams = new HashSet<>();
int splits;

void dumpManifold() {
    for (int y=0; y<manifold.length; ++y) {
        char[] line = manifold[y];
        assert line != null && line.length > 0 : "expected valid line!";
        for (int x=0; x<line.length; ++x) {
            IO.print(line[x]);
        }
        IO.println();
    }
}

boolean beamAt(int x) {
    return beams.contains((Integer) x);
}

void splitBeam(int x, int y) {
    manifold[y][x-1] = '|';
    manifold[y][x+1] = '|';
    assert beamAt(x) : "expected a beam here: %d:(%d,%d)".formatted(x, x, y);
    beams.remove((Integer) x);
    beams.add(x-1);
    beams.add(x+1);
    splits++;
}

void next(int y) {
    for (int x = 0; x < manifold[y].length; x++) {
        char cell = manifold[y][x];
        switch (cell) {
            case '.':
                if (beamAt(x)) manifold[y][x] = '|';
                continue;
            case 'S':
                beams.add(x);
                break;
            case '|':
                break;
            case '^':
                if (beamAt(x)) {
                    splitBeam(x, y);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected char @ [%d][%d]: %c".formatted(y, x, cell));
        }
    }
}

// .^.^.....^.^.^.^.^.^.^...^.^.^...^.^...^.^.^.^...^.....^.^...^.^...^.^.^.^.....^...^.^...^...^.^.^.....^.^...^.^...^...^.^.^.^.^.^.^...^.^.^.
void main(String args[]) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(args[0]));
    manifold = new char[lines.size()][];
    for (int y=0; y<lines.size(); ++y) {
        String line = lines.get(y);
        assert line != null && !line.isBlank() : "expected valid line!";
        manifold[y] = new char[line.length()];
        for (int x=0; x<line.length(); ++x) {
            char cell = line.charAt(x);
            manifold[y][x] = cell;
        }
    }

    int step = 0;
    do {
        next(step++);
    } while (step < manifold.length);

    dumpManifold();
    IO.println("Beam split %d times, resulting in %d beams".formatted(splits, beams.size()));
}
