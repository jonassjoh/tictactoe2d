public class Tictactoe2d {

	private Kattio io = new Kattio(System.in, System.out);

	public static void main(String... args) {
		new Tictactoe2d();
	}

	public Tictactoe2d() {
		print("Hello, world\n");
	}

	private void print(String msg) {
		io.print(msg);
		io.flush();
	}
}
