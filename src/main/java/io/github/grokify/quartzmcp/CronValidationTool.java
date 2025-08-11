package io.github.grokify.quartzmcp;

import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import org.quartz.CronExpression;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;
import java.text.ParseException;

/**
 * MCP Tool specification for validating cron expressions using Quartz CronExpression
 */
public class CronValidationTool {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static SyncToolSpecification createToolSpecification() {
        // Define the tool schema
        String schema = """
            {
              "type": "object",
              "properties": {
                "cron_expression": {
                  "type": "string",
                  "description": "The cron expression to validate (e.g., '0 0 12 * * ?')"
                }
              },
              "required": ["cron_expression"]
            }
            """;
        
        // Create the tool definition
        Tool tool = new Tool(
            "check_cron_validity",
            "Validates a cron expression using Quartz CronExpression.isValidExpression",
            schema
        );
        
        // Create the tool specification with implementation
        return new SyncToolSpecification(tool, (exchange, arguments) -> {
            try {
                Object cronExpressionObj = arguments.get("cron_expression");
                String cronExpression = "";
                if (cronExpressionObj instanceof JsonNode) {
                    cronExpression = ((JsonNode) cronExpressionObj).asText();
                } else if (cronExpressionObj != null) {
                    cronExpression = cronExpressionObj.toString();
                }
                
                // Validate the cron expression using Quartz
                boolean isValid = CronExpression.isValidExpression(cronExpression);
                
                StringBuilder resultText = new StringBuilder();
                resultText.append("Cron Expression: ").append(cronExpression).append("\n");
                resultText.append("Is Valid: ").append(isValid).append("\n");
                
                if (isValid) {
                    try {
                        // If valid, also provide additional details
                        CronExpression cron = new CronExpression(cronExpression);
                        resultText.append("Summary: ").append(cron.getCronExpression()).append("\n");
                        resultText.append("Description: Valid cron expression");
                    } catch (ParseException e) {
                        // This shouldn't happen if isValidExpression returned true
                        resultText.append("Description: Expression is valid but failed to parse: ").append(e.getMessage());
                    }
                } else {
                    resultText.append("Description: Invalid cron expression\n");
                    resultText.append("Error: The provided cron expression does not follow valid cron syntax");
                }
                
                return new CallToolResult(resultText.toString(), false);
                
            } catch (Exception e) {
                String errorMessage = "Failed to validate cron expression: " + e.getMessage();
                return new CallToolResult(errorMessage, true);
            }
        });
    }
}