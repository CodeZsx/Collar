package com.codez.collar.manager;

import com.codez.collar.worker.IBaseHandler;
import com.codez.collar.worker.Worker;

/**
 * Created by codez on 2018/2/1.
 * Description:
 */

public class TemplateManager {
    private static final String TAG = "GroupManager";

    private Worker worker;

    public TemplateManager() {
        worker = new Worker();
        worker.initilize(TAG+" work!");
    }

    static class Loader {
        static final TemplateManager INSTANCE = new TemplateManager();
    }

    public static TemplateManager getInstance() {
        return Loader.INSTANCE;
    }

    public void addHandler(IBaseHandler handler) {
        worker.addHandler(handler);
    }


}
