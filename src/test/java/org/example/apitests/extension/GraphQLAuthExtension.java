package org.example.apitests.extension;

import org.junit.jupiter.api.extension.*;
import org.example.apitests.testutil.AuthUtil;

public class GraphQLAuthExtension implements BeforeEachCallback, ParameterResolver {
    private String token;

    @Override
    public void beforeEach(ExtensionContext context) {
        token = AuthUtil.getAccessToken("testUsername", "password");
    }

    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        return pc.getParameter().getType().equals(String.class)
                && pc.getParameter().getName().equals("bearerToken");
    }

    @Override
    public Object resolveParameter(ParameterContext pc, ExtensionContext ec) {
        return token;
    }
}
