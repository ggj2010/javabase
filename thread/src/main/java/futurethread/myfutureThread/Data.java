package futurethread.myfutureThread;

import lombok.Getter;
import lombok.Setter;

/**
 * author:gaoguangjin
 * Description:
 * Email:335424093@qq.com
 * Date 2016/1/8 17:22
 */
@Getter
@Setter
public class Data {
    //真名称
    String realName;

    Boolean isFinish=false;

    public synchronized String getRealName(){
        if(!isFinish)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return "高广金";
    }

    public synchronized void setRealName(String name){
       setIsFinish(true);
        notify();
    }



}
