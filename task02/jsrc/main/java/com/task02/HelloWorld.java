package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
		lambdaName = "hello_world",
		roleName = "hello_world-role",
		runtime= DeploymentRuntime.JAVA11
)
@LambdaUrlConfig(
		authType = AuthType.NONE,
		invokeMode = InvokeMode.BUFFERED
)
public class HelloWorld implements RequestHandler<Object, Map<String, Object>> {

	public Map<String, Object> handleRequest(Object request, Context context) {
		context.getLogger().log("Invocation started: " + getTimeStamp());
		context.getLogger().log("Request: " + request);
		context.getLogger().log("Context: " + context);
		String methodName = ((Map<String, String>)request).get("rawPath");
		context.getLogger().log("method: " + methodName);

		Map<String, Object> resultMap = new HashMap<>();

		if (methodName.equals("/hello")) {
			resultMap.put("statusCode", 200);
			resultMap.put("body", "{\"statusCode\":200, \"message\": \"Hello from Lambda\"}");
		} else {
			Map<String, Object> requestContext = (((Map<String, Map<String, Object>>)request).get("requestContext"));
			Map<String, Object> http = (Map<String, Object>) requestContext.get("http");
			String message = "Bad request syntax or unsupported method. Request path: " + http.get("path") + ". HTTP method: " + http.get("method");
			context.getLogger().log("requestContext: " + requestContext);
			resultMap.put("statusCode", 400);
			resultMap.put("body",  "{\"statusCode\":400, \"message\": \"" + message + "\"}");
		}

		context.getLogger().log("Invocation completed: " + getTimeStamp());
		return resultMap;
	}

	private String getTimeStamp() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
	}
}