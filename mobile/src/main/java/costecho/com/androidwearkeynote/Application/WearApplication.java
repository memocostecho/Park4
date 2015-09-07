package costecho.com.androidwearkeynote.Application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by yasminegutierrez on 9/6/15.
 */

public class WearApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "Ade7c9PGGgahUTC0gkXf97RW5AlTPNXKsQHtM5aW", "KZpcGZPpTGAxxj9yMxVXwuHQ6RXQmyL5XVzbUVNr");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}



