import common.BaseThread;
import java.util.Random;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;
	private Random willTalk = new Random();
	

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.println("Philosopher with Thread ID " + this.getTID() + " has STARTED eating.");
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher with Thread ID " + this.getTID() + " is DONE eating.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat(): ");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try {
			System.out.println("Philosopher with Thread ID " + this.getTID() + " has STARTED thinking.");
			yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher with Thread ID " + this.getTID() + " is DONE thinking.");
		}
		catch(InterruptedException e) {
			System.err.println("phiosopher.thing(): ");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		try {
			System.out.println("Philosopher with Thread ID " + this.getTID() + " has STARTED talking.");
			yield();
			//Sleep for a random amount of time before saying something brilliant
			saySomething();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher with Thread ID " + this.getTID() + " is DONE talking.");
		}
		catch(InterruptedException e) {
			System.err.println("phiosopher.talk(): ");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}
	
	public void sleep() 
	{
		try {
			System.out.println("Philosopher with Thread ID " + this.getTID() + " has STARTED sleeping.");
			yield();
			//Sleep for a random amount of time before saying something brilliant
			sleep((long)(Math.random() * TIME_TO_WASTE));
			yield();
			System.out.println("Philosopher with Thread ID " + this.getTID() + " is DONE sleeping.");
		}
		catch(InterruptedException e) {
			System.err.println("phiosopher.talk(): ");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();
			
			DiningPhilosophers.soMonitor.goToSleep(getTID());
			
			sleep();
			
			DiningPhilosophers.soMonitor.wakeUp(getTID());

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 * Must make sure no one else is talking && philosopher who wants to talk cannot be eating
			 * 
			 * There is a 20% chance that the philosopher will talk
			 */
			if(willTalk.nextInt(5) == 4)
			{
				DiningPhilosophers.soMonitor.requestTalk(getTID()); //Request to enter C.S
				talk();
				DiningPhilosophers.soMonitor.endTalk(getTID());		//Leave C.S
			} 

			yield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
