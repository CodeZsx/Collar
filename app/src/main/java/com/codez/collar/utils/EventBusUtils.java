package com.codez.collar.utils;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by codez on 2018/2/1.
 * Description:
 */
public class EventBusUtils {
	/**
	 * is need send
	 * 
	 * @param event
	 * @return true : has subscriber for event ,false: did not subscribe to the event.
	 */
	public static boolean isNeedSend(Object event) {
		if (event != null) {
			return EventBus.getDefault().hasSubscriberForEvent(event.getClass());
		} else {
			return false;
		}
	}

	/**
	 * sendEvent
	 * 
	 * @param event
	 * @return true:succ,false:did not subscribe to the event.
	 */
	public static boolean sendEvent(Object event) {
		if (isNeedSend(event)) {
			EventBus.getDefault().post(event);
			return true;
		}
		return false;
	}
	public static void register(Object object){
        EventBus.getDefault().register(object);
    }
    public static void unregister(Object object){
        EventBus.getDefault().unregister(object);
    }
	/**
	 * @param event
	 * @param time send 
	 */
	public static void sendEventErrorDelay(Object event, long time) {
		if (!isNeedSend(event)) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		EventBus.getDefault().post(event);
	}
}
