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
			int alpha = alphabeta(state, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, gameState.getNextPlayer());
			//int alpha = alphabeta(state, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, opponent(gameState.getNextPlayer()));

			if (bestAlpha < alpha) {
				bestAlpha = alpha;
				bestState = state;
			}
		}

		return bestState;
		//Random random = new Random();
		//return nextStates.elementAt(random.nextInt(nextStates.size()));
	}

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
	private int alphabeta(GameState state, int depth, int alpha, int beta, int player) {

		if (depth == 0 || mu(state).size() == 0) {
			return gamma(state, player);
		}

		boolean player_A = (player == Constants.CELL_X);
		int v = 0;

		if (player_A) {
			v = Integer.MIN_VALUE;
			for (GameState child : mu(state)) {
				v = max(v, alphabeta(child, depth-1, alpha, beta, opponent(player)));
				alpha = max(alpha, v);
				if (beta <= alpha)
					break; // beta prune
			}
		}
		else {
			v = Integer.MAX_VALUE;
			for (GameState child : mu(state)) {
				v = min(v, alphabeta(child, depth-1, alpha, beta, opponent(player)));
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

	private int gamma(GameState state, int player) {
		int myMarks = 0;

		myMarks += gamma_rows(state, player);
		myMarks += gamma_cols(state, player);
		myMarks += gamma_cross(state, player);
		myMarks += gamma_cross_opposite(state, player);

		return myMarks;
	}

	private int mathit(int points) {
		int res = 10;
		while (points > 0) {
			res *= 10;
			points--;
		}
		return res;
	}

	private int gamma_rows(GameState state, int player) {
		int res = 0;
		for (int row = 0; row < 4; row++) {
			int points = 0;
			for (int col = 0; col < 4; col++) {
				if (state.at(row, col) == opponent(player))
					return 0;
				if (state.at(row, col) == player) {
					points++;
				}
			}
			res += mathit(points);
		}
		return res;
	}

	private int gamma_cols(GameState state, int player) {
		int res = 0;
		for (int col = 0; col < 4; col++) {
			int points = 0;
			for (int row = 0; row < 4; row++) {
				if (state.at(row, col) == opponent(player))
					return 0;
				if (state.at(row, col) == player) {
					points++;
				}
			}
			res += mathit(points);
		}
		return res;
	}

	private int gamma_cross(GameState state, int player) {
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

	private int gamma_cross_opposite(GameState state, int player) {
		int points = 0;
		for (int i = 0; i < 4; i++) {
			if (state.at(i, 4-i) == opponent(player))
				return 0;
			if (state.at(i, 4-i) == player) {
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
