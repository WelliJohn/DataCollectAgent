package datacollect.jww.com.datacollectagent;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author: JiangWeiwei
 * @time: 2017/4/12-13:49
 * @email: wellijohn1991@gmail.com
 * @desc:
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                // TODO: 2017/4/12 会话数据的采集 By JiangWeiwei

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


}
