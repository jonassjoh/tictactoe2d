import java.util.*;

public class Player {

	/**
	 * Performs a move
	 *
	 * @param gameState
	 *            the current state of the board
	 * @param deadline
	 *            time before which we must have returned
	 * @return the next state the board is in after our move
	 */
	public GameState play(final GameState gameState, final Deadline deadline) {
		Vector<GameState> nextStates = new Vector<GameState>();
		gameState.findPossibleMoves(nextStates);

		if (nextStates.size() == 0) {
			// Must play "pass" move if there are no other moves possible.
			return new GameState(gameState, new Move());
		}

		/**
		 * Here you should write your algorithms to get the best next move, i.e.
		 * the best next state. This skeleton returns a random move instead.
		 */

		int bestAlpha = Integer.MIN_VALUE;
		GameState bestState = null;

		for (GameState state : nextStates) {
			int alpha = alphabeta(state, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, gameState.getNextPlayer(), gameState);

			if (bestAlpha < alpha) {
				bestAlpha = alpha;
				bestState = state;
				gamma(state, gameState.getNextPlayer(), gameState);
			}
		}

		return bestState;
	}

/*
	private int minimax(GameState state, int player) {

		if (mu(state).size() == 0) {
			return gamma(state, player);
		}


		boolean player_A = (player == Constants.CELL_X);

		int bestPossible = player_A ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		for (GameState child : mu(state)) {
			int v = minimax(state, player_A ? Constants.CELL_X : Constants.CELL_O);
			bestPossible = player_A ? max(bestPossible, v) : min(bestPossible, v);
		}

		return bestPossible;
	}
*/

	/**
	  * @param state
	  *		The current state we are analyzing
	  * @param alpha
	  *		The current best value achievable by A
	  * @param beta
	  *		The current best value achievable by B
	  * @return
	  *		The minimax value of the state
	  */
	private int alphabeta(GameState state, int depth, int alpha, int beta, int player, GameState prevState) {

		if (depth == 0 || mu(state).size() == 0) {
			return gamma(state, player, prevState);
		}

		boolean player_A = (player == Constants.CELL_X);
		int v = 0;

		if (player_A) {
			v = Integer.MIN_VALUE;
			for (GameState child : mu(state)) {
				v = max(v, alphabeta(child, depth-1, alpha, beta, opponent(player), state));
				alpha = max(alpha, v);
				if (beta <= alpha)
					break; // beta prune
			}
		}
		else {
			v = Integer.MAX_VALUE;
			for (GameState child : mu(state)) {
				v = min(v, alphabeta(child, depth-1, alpha, beta, opponent(player), state));
				beta = min(beta, v);
				if (beta <= alpha)
					break; // alpha prune
			}
		}
		return v;
	}

	private Vector<GameState> mu(GameState state) {
		Vector<GameState> nextStates = new Vector<GameState>();
		state.findPossibleMoves(nextStates);
		return nextStates;
	}

	private int getPlayedCell(GameState curr, GameState prev) {
		for (int cell=0; cell < 16; cell++) {
			if (prev.at(cell) != curr.at(cell)) return cell;
		}
		return -1;
	}

	private int gamma(GameState state, int player, GameState prevState) {

		if (state.isOWin()) return Integer.MIN_VALUE;
		if (state.isXWin()) return Integer.MAX_VALUE;
		if (state.isEOG()) return 0;

		int myMarks = 0;

		int cell = getPlayedCell(state, prevState);

		myMarks += gamma_rows(state, player, cell);
		myMarks += gamma_cols(state, player, cell);
		myMarks += gamma_cross(state, player, cell);
		myMarks += gamma_cross_opposite(state, player, cell);

		return player == Constants.CELL_X ? myMarks : myMarks * -1;
	}

	private int mathit(int points) {
		int res = 1;
		while (points > 0) {
			res *= 10;
			points--;
		}
		return res;
	}

	private int gamma_rows(GameState state, int player, int cell) {
		int col = state.cellToCol(cell);

		int points = 0;
		for (int row = 0; row < 4; row++) {
			if (state.at(row, col) == opponent(player))
				return 0;
			if (state.at(row, col) == player)
				points++;
		}
		return mathit(points);
	}

	private int gamma_cols(GameState state, int player, int cell) {
		int row = state.cellToRow(cell);

		int points = 0;
		for (int col = 0; col < 4; col++) {
			if (state.at(row, col) == opponent(player))
				return 0;
			if (state.at(row, col) == player)
				points++;
		}
		return mathit(points);
	}

	private boolean inCross_1(GameState state, int cell) {
		switch (cell) {
			case 0:
				return true;
			case 5:
				return true;
			case 10:
				return true;
			case 15:
				return true;
			default:
				return false;
		}
	}

	private boolean inCross_2(GameState state, int cell) {
		switch (cell) {
			case 3:
				return true;
			case 6:
				return true;
			case 9:
				return true;
			case 12:
				return true;
			default:
				return false;
		}
	}

	private int gamma_cross(GameState state, int player, int cell) {

		if (!inCross_1(state, cell)) return 0;

		int points = 0;
		for (int i = 0; i < 4; i++) {
			if (state.at(i, i) == opponent(player))
				return 0;
			if (state.at(i, i) == player) {
				points++;
			}
		}
		return mathit(points);
	}

	private int gamma_cross_opposite(GameState state, int player, int cell) {

		if (!inCross_2(state, cell)) return 0;

		int points = 0;
		for (int i = 0; i < 4; i++) {
			if (state.at(i, 3-i) == opponent(player))
				return 0;
			if (state.at(i, 3-i) == player) {
				points++;
			}
		}
		return mathit(points);
	}

	private int opponent(int player) {
		return player == Constants.CELL_X ? Constants.CELL_O : Constants.CELL_X;
	}

	private int min(int a, int b) {
		return a < b ? a : b;
	}

	private int max(int a, int b) {
		return a > b ? a : b;
	}
}
