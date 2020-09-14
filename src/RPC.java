import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class RPC {
	
	private static final RPC INSTANCE = new RPC();
	public static final RPC getInstance() {
		return INSTANCE;
	}

private boolean running = true;
	  	private long created = 0;

	  	public void start() {
	  		
	  		this.created = System.currentTimeMillis();
	  		
	  		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
	  			
	  			@Override
	  			public void apply(DiscordUser user) {
	  				System.out.println("Welcome " + user.username + "#" + user.discriminator + ".");
	  				update("Starting...", "Configuring Settings");
	  				
	  			}
	  		}).build();
	  		
	  		DiscordRPC.discordInitialize("750117278971985931", handlers, true);
	  		
	  		new Thread("Discord RPC Callback") {
	  			
	  			public void run() {
	  				while(running) {
	  					DiscordRPC.discordRunCallbacks();
	  				}
	  			}
	  			
	  		}.start();
	  	}
	  	
	  	public void shutdown() {
	  		running = false;
	  		DiscordRPC.discordShutdown();
	  	}
	  	
	  	public void update(String firstLine, String secondLine) {
	  		DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
	  		b.setBigImage("largw", "very sad");
	  		b.setSmallImage("school_picturew", "bad");
	  		b.setDetails(firstLine);
	  		b.setStartTimestamps(created);
	  		
	  		DiscordRPC.discordUpdatePresence(b.build());
	  	}

  }  