/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	protected status[] philisopers;
	protected int numberOfChopsticks = 0;
	enum status{ EATING, THINKING, SLEEPING, TALKING, HUNGRY }

	/**
	 * Constructor
	 * TODO: set appropriate number of chopsticks based on the # of philosophers
	 * 
	 * Creating an array of Philosophers, whom are initially thinking. Also, add
	 * a chopstick at every Philosopher created.
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		this.numberOfChopsticks = piNumberOfPhilosophers;
		for(int i = 0; i < piNumberOfPhilosophers; i++){
			philisopers[i] = status.THINKING;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		// ...
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// ...
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// ...
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		// ...
	}
}

// EOF
