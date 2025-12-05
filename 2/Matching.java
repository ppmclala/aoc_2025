void main() {
    Matcher m = Pattern.compile("\\d{2}").matcher("1234567890");
    while (m.find()) {
        IO.println("%d:%d".formatted(m.start(), m.end()));
    }
}
