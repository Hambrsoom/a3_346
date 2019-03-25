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
	 * philosophers[] --> All philisophers sitting on a table
	 * chopsticks[]   --> All chopsticks on the table
	 * canTalk		  --> Boolean if any philosopher can start talking
	 * status		  --> Enumeration showing current state to philosophers (eat, think, sleep...)
	 */
	protected status[] philisopers;
	protected boolean chopsticks[];
	protected boolean canTalk = true;
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
		for(int i = 0; i < piNumberOfPhilosophers; i++){
			chopsticks[i] = true;
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
	 * 
	 * We make sure that both chopsticks are available. If one or none is missing, wait.
	 * Otherwise, pick up both and start eating.
	 */
	public synchronized void pickUp(final int piTID)
	{
		philisopers[piTID] = status.HUNGRY;
		while(!chopsticks[(piTID-1)%chopsticks.length] && !chopsticks[(piTID)%chopsticks.length]){
			try{
				this.wait();
			}catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}
		chopsticks[(piTID-1)%chopsticks.length] = false;
		chopsticks[(piTID)%chopsticks.length] = false;
		philisopers[piTID] = status.EATING;
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 * 
	 * Put down both forks, change your status to THINKING and noitfy
	 * all other members you are done.
	 */
	public synchronized void putDown(final int piTID)
	{
		chopsticks[(piTID-1)%chopsticks.length] = true;
		chopsticks[(piTID)%chopsticks.length] = true;
		philisopers[piTID] = status.THINKING;
		this.notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 * 
	 * Sleeping is not implemented since there is no specific
	 * case when a philosopher goes to sleep.
	 */
	public synchronized void requestTalk(final int piTID)
	{
		while(!canTalk && philisopers[piTID] == status.EATING){
			try{
				this.wait();
			} catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}
		canTalk = false;
		philisopers[piTID] = status.TALKING;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		canTalk = true;
		philisopers[piTID] = status.THINKING;
		this.notifyAll();
	}

}

// EOF
