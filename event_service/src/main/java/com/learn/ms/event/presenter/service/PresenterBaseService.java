package com.learn.ms.event.presenter.service;

import com.learn.ms.event.common.utils.IPUtils;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.exceptions.UnauthorizedResourceException;
import com.learn.ms.event.core.service.BaseService;
import java.util.Optional;

public class PresenterBaseService extends BaseService {
    public static final String CURRENT_USER_CONTEXT_HEADER = "CurrentContext";

    public Optional<String> getHeaderValue(String headerName) {
        try {
            return Optional.ofNullable(httpServletRequest.getHeader(headerName));
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }

        return Optional.empty();
    }

    public String getRemoteIPAddress() {
        try {
            String realIp = IPUtils.getClientRealIpAddress(httpServletRequest);
            if (io.micrometer.common.util.StringUtils.isNotBlank(realIp)) {
                return realIp;
            } else {
                return httpServletRequest.getRemoteAddr();
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public String getCurrentUserContextHeaderValue() {
        Optional<String> userTokenOpt = getHeaderValue(CURRENT_USER_CONTEXT_HEADER);
        if (userTokenOpt.isEmpty()) {
            throw new UnauthorizedResourceException(ResponseMessage.UNAUTHORIZED_RESOURCE_ACCESS.getResponseMessage());
        }
        return userTokenOpt.get();
    }
}
