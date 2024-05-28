package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
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
		aliasName = "${lambdas_alias_name}",
		resourceGroup = "event_group",
		runtime = DeploymentRuntime.JAVA11,
		architecture = Architecture.ARM64,
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
		authType = AuthType.NONE,
		invokeMode = InvokeMode.BUFFERED
)
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	@Override
	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent requestEvent, Context context) {
		String path = requestEvent.getRequestContext().getHttp().getPath();
		String method = requestEvent.getRequestContext().getHttp().getMethod();

		if (path != null && path.equals("/hello")) {
			APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
			response.setStatusCode(200);
			response.setBody("Hello from Lambda");

			return response;
		}

		APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
		response.setStatusCode(400);
		response.setBody(String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s",
				path, method));

		return response;
	}
}