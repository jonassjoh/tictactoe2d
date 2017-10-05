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
   
		for (GameState state : nextStates) {
		}


		Random random = new Random();
		return nextStates.elementAt(random.nextInt(nextStates.size()));
	}
	
	private int minimax(GameState state) {
		int player = 0;
		int PLAYER_A = 0;
		int PLAYER_B = 1;
		
		if (mu(state).size() == 0) {
			return gamma(state);
		}


		boolean player_A = (state.getNextPlayer() == Constants.CELL_X);
		
		int bestPossible = player_A ? Integer.MIN_VALUE : Integer.MAX_VALUE;

		for (GameState child : mu(state)) {
			int v = minimax(state);
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
	private int alphabeta(GameState state, int depth, int alpha, int beta) {

		if (depth == 0 || mu(state).size() == 0) {
			return gamma(state);
		}
	
		boolean player_A = (state.getNextPlayer() == Constants.CELL_X);
		int v = 0;

		if (player_A) {
			v = Integer.MIN_VALUE;
			for (GameState child : mu(state)) {
				v = max(v, alphabeta(child, depth-1, alpha, beta));
				alpha = max(alpha, v);
				if (beta <= alpha)
					break; // beta prune
			}
		}
		else {
			v = Integer.MAX_VALUE;
			for (GameState child : mu(state)) {
				v = min(v, alphabeta(child, depth-1, alpha, beta));
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

	private int gamma(GameState state) {
		// TOOD: Heuristic function
		return 1;
	}

	private int min(int a, int b) {
		return a < b ? a : b;
	}

	private int max(int a, int b) {
		return a > b ? a : b;
	}
}
