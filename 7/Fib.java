// 1 1 2 3 5 8 13 21...
int fib(int n) {
    if (n <= 1) return 1;
    return fib(n-2) + fib(n-1);
}

void main() {
    IO.println("Hello, Fibs!");
    IO.println(fib(7));
    for (int i=0; i<100; ++i) IO.println(fib(i));
}
