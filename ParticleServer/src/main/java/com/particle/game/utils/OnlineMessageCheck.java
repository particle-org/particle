package com.particle.game.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.api.utils.IOnlineMessageCheck;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.util.http.IHttpConnectionApi;

@Singleton
public class OnlineMessageCheck implements IOnlineMessageCheck {

    private static final String PARAMS = "level=1_channel=hyt_content=";

    @Inject
    private ServerConfigService serverConfigService;

    @Inject
    private IHttpConnectionApi httpConnection;

    @Override
    public CheckResult checkMessageSync(String message) {
        int result = this.httpConnection.send(serverConfigService.getOnlineCheckApi(), PARAMS + message);

        if (result == 200) return CheckResult.PASS;
        else if (result == 201) return CheckResult.HIDE;
        else if (result == 202) return CheckResult.FORBIDDEN;
        else return CheckResult.PASS;
    }

}
