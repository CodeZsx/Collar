package com.codez.collar.event;

/**
 * Created by codez on 2018/2/1.
 * Description:
 */
public class ToastEvent {
	

	public String content;
	public int resId;

    /**
     * 没必要每次都生成一个对象，统一使用{@link ToastEvent#newToastEvent(String) 获取}
     * Created by codez on 2018/2/1.
     */
    @Deprecated
	public ToastEvent(String content) {
		this.content = content;
	}
    /**
     * 没必要每次都生成一个对象，统一使用{@link ToastEvent#newToastEvent(String) 获取}
     * Created by codez on 2018/2/1.
     */
    @Deprecated
	private ToastEvent(int resId){
        this.resId = resId;
    }
    private ToastEvent(){}

    private static ToastEvent toast;

	public static ToastEvent newToastEvent(String content){
        if (toast == null) {
            toast = new ToastEvent();
        }
        toast.content = content;
        return toast;
    }
    public static ToastEvent newToastEvent(int res){
        if (toast == null) {
            toast = new ToastEvent();
        }
        toast.resId = res;
        return toast;
    }
}
