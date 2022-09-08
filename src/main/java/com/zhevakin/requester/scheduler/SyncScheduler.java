package com.zhevakin.requester.scheduler;


import com.zhevakin.requester.facade.SyncFacade;
import org.springframework.stereotype.Service;

public class SyncScheduler {

    SyncFacade syncFacade;

    public SyncScheduler(SyncFacade syncFacade) {
        this.syncFacade = syncFacade;
    }

    public void sync() {
        // Код получения данных с сервера
        // ...
        // Код обновления данных на форме через функцию класса MainController или же написать обозреватель там
    }


}
