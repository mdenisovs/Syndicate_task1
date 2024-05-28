package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.Architecture;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;

@LambdaHandler(
        lambdaName = "hello_world",
        roleName = "hello_world-role",
        isPublishVersion = true,
        methodName = "GET",
        aliasName = "${lambdas_alias_name}",
        runtime = DeploymentRuntime.JAVA11,
        architecture = Architecture.ARM64,
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
        authType = AuthType.NONE,
        invokeMode = InvokeMode.BUFFERED
)
public class HelloWorld implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        if (apiGatewayProxyRequestEvent.getHttpMethod() != null && apiGatewayProxyRequestEvent.getHttpMethod().equals("hello")) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody("Hello from Lambda");
        }

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(400)
                .withBody(String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s context: (%s) event: (%s)",
                        apiGatewayProxyRequestEvent.getPath(), apiGatewayProxyRequestEvent.getHttpMethod(), context, apiGatewayProxyRequestEvent));

    }

}