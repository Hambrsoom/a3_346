
/**
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
	protected int counterSleeping = 0;
	protected int [] priorities;
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
		philisopers = new status[piNumberOfPhilosophers];
		chopsticks  = new boolean[piNumberOfPhilosophers];
		priorities  = new int[piNumberOfPhilosophers];
		for(int i = 0; i < piNumberOfPhilosophers; i++){
			chopsticks[i]  = true;
			philisopers[i] = status.THINKING;
			priorities[i]  = (int) Math.random();
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
	public synchronized int pickUp(final int piTID)
	{
		int leftPhil;
		int leftPhilPriority;
		int highestPriorityPhil = 2;
		philisopers[piTID - 1] = status.HUNGRY;

		int rightPhil = piTID % philisopers.length;
		int rightPhilPriority = priorities[piTID % philisopers.length];

		if (piTID > 1) {
			leftPhil         = (piTID - 2) % philisopers.length;
			leftPhilPriority = priorities[(piTID - 2) % philisopers.length];
		} else {
			leftPhil = (2 - piTID) % philisopers.length;
			leftPhilPriority = priorities[(2-piTID) % philisopers.length];
		}

		int me = (piTID - 1) % philisopers.length;
		int myPriority = priorities[ (piTID - 1) % philisopers.length];

		// checking if my right neighbor has a higher priority then me
		// and he's hungry and can eat
		// (note that this will happen only if I'm the last guy in the array)
		if (rightPhilPriority > myPriority && philisopers[rightPhil] == status.HUNGRY) {
			if (chopsticks[piTID % chopsticks.length] && chopsticks[(piTID + 1) % chopsticks.length]) {
				highestPriorityPhil = rightPhil;
			} else { // if hungry but don't have chopsticks
				try {
					System.out.println("Philosopher is waiting: " + piTID );
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// if my right neighbour is not gonna eat I'll check if my left has the same
			// conditions compared to me
		} else if (leftPhilPriority > myPriority && philisopers[leftPhil] == status.HUNGRY) {
			if (chopsticks[(piTID - 1) % chopsticks.length] && chopsticks[(piTID - 2) % chopsticks.length]) {
				highestPriorityPhil = leftPhil;
			} else { // if hungry but don't have chopsticks
				try {
					this.wait();
					System.out.println("Philosopher is waiting: " + piTID);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// if both of my neighbours won't eat then I will
		} else {
			highestPriorityPhil = me;
		}
		

		//SHould we add the the hungry case ?
		highestPriorityPhil = highestPriorityPhil +1;
		while(!chopsticks[(highestPriorityPhil-1)%chopsticks.length] || !chopsticks[(highestPriorityPhil)%chopsticks.length]){
			try{
				wait();
				System.out.println("Philosopher is waiting: " + piTID);
			}catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}
		
			chopsticks[(highestPriorityPhil-1)%chopsticks.length] = false;
			chopsticks[(highestPriorityPhil)%chopsticks.length] = false;
			philisopers[highestPriorityPhil-1] = status.EATING;
			return highestPriorityPhil;
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
		
		chopsticks[(piTID- 1) %chopsticks.length] = true;
		chopsticks[(piTID)%chopsticks.length] = true;
		philisopers[piTID - 1] = status.THINKING;
		notifyAll();
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
		while(!canTalk || philisopers[piTID-1] == status.EATING || counterSleeping !=0 ){
			try{
				wait();
			} catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}
		canTalk = false;
		philisopers[piTID-1] = status.TALKING;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		canTalk = true;
		philisopers[piTID-1] = status.THINKING;
		notifyAll();
	}
	
	/**
	 * When philosopher wants to sleep
	 * 
	 */
	public synchronized void goToSleep(final int piTID) {
		while(!canTalk) {
			try{
				wait();
			} catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}
		counterSleeping = counterSleeping +1;
		philisopers[piTID-1] = status.SLEEPING;
	}
	/**
	 * When philosopher will wake up
	 * 
	 */
	public synchronized void wakeUp(final int piTID) {
		counterSleeping = counterSleeping -1;
		philisopers[piTID-1] = status.THINKING;
		if(counterSleeping == 0) {
			notifyAll();
		}
	}
}

// EOF