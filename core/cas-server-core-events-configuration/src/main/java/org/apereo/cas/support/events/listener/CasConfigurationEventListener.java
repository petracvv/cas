package org.apereo.cas.support.events.listener;

import org.apache.commons.lang3.ObjectUtils;
import org.apereo.cas.configuration.CasConfigurationPropertiesEnvironmentManager;
import org.apereo.cas.support.events.config.CasConfigurationModifiedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.event.EventListener;

import java.util.Collection;
import java.util.Collections;

/**
 * This is {@link CasConfigurationEventListener}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
public class CasConfigurationEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasConfigurationEventListener.class);

    @Autowired(required = false)
    private ContextRefresher contextRefresher;

    private final CasConfigurationPropertiesEnvironmentManager configurationPropertiesEnvironmentManager;

    public CasConfigurationEventListener(final CasConfigurationPropertiesEnvironmentManager configurationPropertiesEnvironmentManager) {
        this.configurationPropertiesEnvironmentManager = configurationPropertiesEnvironmentManager;
    }

    /**
     * Handle refresh event when issued to this CAS server locally.
     *
     * @param event the event
     */
    @EventListener
    public void handleRefreshEvent(final EnvironmentChangeEvent event) {
        LOGGER.debug("Received event [{}]", event);
        configurationPropertiesEnvironmentManager.rebindCasConfigurationProperties();
    }

    /**
     * Handle configuration modified event.
     *
     * @param event the event
     */
    @EventListener
    public void handleConfigurationModifiedEvent(final CasConfigurationModifiedEvent event) {
        if (this.contextRefresher == null) {
            LOGGER.warn("Unable to refresh application context, since no refresher is available");
            return;
        }

        if (event.isEligibleForContextRefresh()) {
            LOGGER.info("Received event [{}]. Refreshing CAS configuration...", event);
            Collection<String> keys = null;
            try {
                keys = this.contextRefresher.refresh();
                LOGGER.debug("Refreshed the following settings: [{}].", keys);
            } catch (final Throwable e) {
                LOGGER.trace(e.getMessage(), e);
            } finally {
                configurationPropertiesEnvironmentManager.rebindCasConfigurationProperties();
                LOGGER.info("CAS finished rebinding configuration with new settings [{}]",
                        ObjectUtils.defaultIfNull(keys, Collections.emptyList()));
            }
        }
    }
}
