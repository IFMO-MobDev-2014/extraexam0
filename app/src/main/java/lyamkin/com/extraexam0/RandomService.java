package lyamkin.com.extraexam0;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import java.util.Random;


public class RandomService extends IntentService {

    public RandomService() {
        super("RandomService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final MyDatabase db = MyDatabase.getOpenedInstance(this);
            while (true) {
                long r = db.randomFunction();
                getContentResolver().notifyChange(Uri.parse(TaskContentProvider.CHANNELS_URI.toString()), null);
                Random rand = new Random();
                int time = rand.nextInt(10000);
                try {
                    Thread.sleep(time);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                db.randomFunction2(r, time);
                getContentResolver().notifyChange(Uri.parse(TaskContentProvider.CHANNELS_URI.toString()), null);
                try {
                    Thread.sleep(5000);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            //final long channelId = intent.getLongExtra(CHANNEL_ID, -1);
            //handleActionUpdateChannel(channelId);
        }
    }
}
