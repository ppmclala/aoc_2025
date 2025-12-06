final int START_POINT = 50;
final int MAX_POINT = 99;

enum Dir { RIGHT, LEFT }

static class Rotation {
    Dir dir;
    int count;

    static Rotation of(String raw) {
        Dir d = raw.charAt(0) == 'L' ? Dir.LEFT : Dir.RIGHT;
        int clicks = Integer.parseInt(raw.substring(1));

        return new Rotation(d, clicks);
    }

    public Rotation(Dir dir, int count) {
        this.dir = dir;
        this.count = count;
    }
}

class State {
    int pointer = 50;
    int zeroes = 0;
}

record RotationResult(int point, int zeroes) { }

RotationResult rotate(int pointer, Rotation rotation) {
    return switch (rotation.dir) {
        case RIGHT -> {
            int zs = rotation.count / 100;
            int c = rotation.count % 100;
            int s = 100 - pointer;
            int p;
            if (c >= s) {
                zs++;
                p = c - s;
            } else {
                p = pointer + c;
            }
            IO.println("[%2d:R%2d] result: p = %d, zs = %d".formatted(pointer, rotation.count, p, zs));

            yield new RotationResult(p, zs);
        }
        case LEFT -> {
            int zs = rotation.count / 100;
            int c = rotation.count % 100;
            int p;
            if (c == pointer) {
                p = 0;
                if (pointer != 0) zs++;
            } else if (c < pointer) {
                p = pointer - c;
            } else {
                if (pointer != 0) zs++;
                p = 100 - (c - pointer);
            }

            IO.println("[%2d:L%2d] result: p = %d, zs = %d".formatted(pointer, rotation.count, p, zs));

            yield new RotationResult(p, zs);
        }
    };
}

void main(String[] args) throws IOException {
    final State state = new State();
    Files.readAllLines(Path.of(args[0]))
        .stream()
        .map(l -> Rotation.of(l))
        .forEach(r -> {
        var next = rotate(state.pointer, r);

        assert next.point <= MAX_POINT;
        assert next.point >= 0;

        state.pointer = next.point;
        state.zeroes += next.zeroes;
    });

    IO.println("The password is: %d [point = %d]\n".formatted(state.zeroes, state.pointer));
}
