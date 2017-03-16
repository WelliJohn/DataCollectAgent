package datacollect.jww.com.datacollectagent;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * @author: JiangWeiwei
 * @time: 2017/3/14-16:19
 * @email: jiangweiwei@qccr.com
 * @desc:
 */
public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BaseActivity", "onResume: " + this.getClass().getName());
    }

}
