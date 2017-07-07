package datacollect.jww.com.aspectj;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 截获类名最后含有Activity、Layout的类的所有非static方法（static方法另外加一个static修饰的execution或者call即可：execution( static * *..Activity+.*(..)）
 * 监听目标方法的执行时间
 */
@Aspect
public class TraceAspect {
    private static final String TAG = "TraceAspect";
    private static Object currentObject = null;

    //截获所有类的所有方法
    private static final String POINTCUT_CALL = "execution(* *.*(..))";


    @Pointcut(POINTCUT_CALL)//切入点
    public void methodCall() {
        Log.d(TAG, "methodCall: ");
    }


    @After("methodCall()")
    public void onCallAfter(JoinPoint joinPoint) throws Throwable {
        Activity activity = null;
        //获取目标对象，截获运行时类型
        if (joinPoint.getTarget() instanceof Activity) {
            activity = ((Activity) joinPoint.getTarget());
            Log.e(TAG, "onCallAfter : " + activity.getClass().getName() + "method : " + ((MethodSignature) joinPoint.getSignature()).getName());
            for (Object object : joinPoint.getArgs()) {
                if (object instanceof View) {
                    Log.d(TAG, "view参数onCallAfter: " + ((View) object).getId());
                } else if (object instanceof Bundle) {
                    Log.d(TAG, "Bundle参数onCallAfter: " + object);
                }
            }
        }

        /**
         * 这里是监听OnClickListener类型的事件
         */
        if (joinPoint.getTarget() instanceof View.OnClickListener) {
            for (Object object : joinPoint.getArgs()) {
                if (object instanceof View) {
                    View v = (View) object;
                    Log.d(TAG, "拿到的tag的值: "+v.getTag());
                    while (!(v instanceof ContentFrameLayout)) {
                        Log.d(TAG, "每个层级的view的onCallAfter: " + v.getClass());
                        v = (View) v.getParent();
                    }
                }
            }
        }
    }



}