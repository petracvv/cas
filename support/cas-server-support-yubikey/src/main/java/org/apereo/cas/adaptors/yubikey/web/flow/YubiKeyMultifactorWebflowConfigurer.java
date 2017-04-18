package org.apereo.cas.adaptors.yubikey.web.flow;

import org.apereo.cas.web.flow.AbstractCasMultifactorWebflowConfigurer;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;

/**
 * This is {@link YubiKeyMultifactorWebflowConfigurer}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
public class YubiKeyMultifactorWebflowConfigurer extends AbstractCasMultifactorWebflowConfigurer {

    /** Webflow event id. */
    public static final String MFA_YUBIKEY_EVENT_ID = "mfa-yubikey";
    
    private FlowDefinitionRegistry yubikeyFlowRegistry;

    @Override
    protected void doInitialize() throws Exception {
        registerMultifactorProviderAuthenticationWebflow(getLoginFlow(), MFA_YUBIKEY_EVENT_ID, this.yubikeyFlowRegistry);
    }

    public void setYubikeyFlowRegistry(final FlowDefinitionRegistry yubikeyFlowRegistry) {
        this.yubikeyFlowRegistry = yubikeyFlowRegistry;
    }
}
