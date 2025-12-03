import java.io.IOException;

static Stream<Rotation> rotations() throws IOException {
    return Files.readAllLines(Path.of("input.txt")).stream().map(l -> Rotation.of(l));
}

static final int START_POINT = 50;
static final int MAX_POINT = 99;

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
    int zeros;
}

int rotate(int pointer, Rotation rotation) {
    int clicks = (rotation.count % 10) * (rotation.dir == Dir.LEFT ? 1 : -1);
    return pointer + clicks;
}

void main() throws IOException {
    final State state = new State();
    rotations().forEach(r -> {
        var next = rotate(state.pointer, r);
        System.out.printf("moved from %d to %d\n", state.pointer, next);
        if (next == 0) state.zeros++;
        state.pointer = next;
    });

    System.out.printf("The password is: %d\n", state.zeros);
}
