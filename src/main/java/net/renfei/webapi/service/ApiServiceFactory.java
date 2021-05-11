package net.renfei.webapi.service;

import net.renfei.webapi.service.impl.NewPlatformApiServiceImpl;
import net.renfei.webapi.service.impl.OldPlatformApiServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author renfei
 */
@Service
public class ApiServiceFactory {
    private final NewPlatformApiServiceImpl newPlatformApiService;
    private final OldPlatformApiServiceImpl oldPlatformApiService;

    public ApiServiceFactory(NewPlatformApiServiceImpl newPlatformApiService,
                             OldPlatformApiServiceImpl oldPlatformApiService) {
        this.newPlatformApiService = newPlatformApiService;
        this.oldPlatformApiService = oldPlatformApiService;
    }

    public ApiService getApiService() {
        return getApiService(PlatformEnum.OLD);
    }

    public ApiService getApiService(PlatformEnum platformEnum) {
        switch (platformEnum) {
            case NEW:
                return newPlatformApiService;
            case OLD:
                return oldPlatformApiService;
            default:
                return null;
        }
    }
}
