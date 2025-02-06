package com.learn.ms.notification.common.utils;


import com.learn.ms.notification.core.domain.enums.ResponseMessage;
import com.learn.ms.notification.core.domain.exceptions.NotificationDomainException;
import com.learn.ms.notification.core.service.BaseService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NotificationUtils extends BaseService {
    private static final String TEMPLATE_NAME = "BASE_TEMPLATE";
    private static final String TEMPLATE_EXTENSION = ".ftl";

    public static String prepareEmailNotificationContent(String templateName, Map<String, Object> templateParams) {
        try {
            templateParams.put("templateToInclude", templateName.concat(TEMPLATE_EXTENSION));
            Configuration freeMarkerConfig = getFreeMarkerConfigurationBean();
            Template template = freeMarkerConfig.getTemplate(TEMPLATE_NAME.concat(TEMPLATE_EXTENSION));
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateParams);
        } catch (Exception ex) {
            throw new NotificationDomainException(ResponseMessage.TEMPLATE_PROCESSING_ERROR.getResponseMessage());
        }
    }

    private static Configuration getFreeMarkerConfigurationBean() {
        Configuration freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_30);
        freeMarkerConfig.setTemplateLoader(new ClassTemplateLoader(NotificationUtils.class, "/templates"));
        return freeMarkerConfig;
    }

    private static int getTotalPlaceholder(String messageContent) {
        String placeHolderRegex = "\\$\\{([a-zA-Z]+)\\}";
        Pattern placeHolderPattern = Pattern.compile(placeHolderRegex);
        Matcher m = placeHolderPattern.matcher(messageContent);
        Set<String> matches = new HashSet<>();
        while (m.find()) matches.add(m.group(1));
        return matches.size();
    }

    private static void checkTemplateParams(String content, Map<String, Object> templateParams) throws NotificationDomainException {
        int totalPlaceholderMatched = getTotalPlaceholder(content);
        int totalParams = templateParams.size();

        //total count check
        if (totalParams < totalPlaceholderMatched) {
            throw new NotificationDomainException(ResponseMessage.TEMPLATE_PARAM_COUNT_MISMATCH.getResponseMessage());
        }

        //extract required parameters that content holds
        List<String> requiredParamsList = getRequiredParamsFromTemplate(content);

        //Check that all required variables are present and not null in templateParams
        for (String param : requiredParamsList) {
            if (!templateParams.containsKey(param) || templateParams.get(param) == null) {
                throw new NotificationDomainException(ResponseMessage.TEMPLATE_PARAM_MISMATCH.getResponseMessage());
            }
        }
    }

    private static List<String> getRequiredParamsFromTemplate(String templateText) throws NotificationDomainException {
        Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(templateText);
        List<String> requiredParams = new ArrayList<>();
        while (matcher.find()) {
            String paramName = matcher.group(1);
            if (paramName.trim().isEmpty()) {
                throw new NotificationDomainException(ResponseMessage.TEMPLATE_PARAM_TYPO.getResponseMessage());
            }
            requiredParams.add(paramName);
        }
        return requiredParams;
    }

    public static List<Map<String, String>> convertCsvToMap(byte[] csvData) throws IOException {
        List<Map<String, String>> resultList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.ByteArrayInputStream(csvData), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("CSV data is empty or missing a header row");
            }

            String[] headers = line.split(",");
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> rowMap = new HashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    rowMap.put(headers[i], values[i]);
                }
                resultList.add(rowMap);
            }
        }
        return resultList;
    }
}
