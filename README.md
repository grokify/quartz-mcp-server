# Quartz MCP Server

A Model Context Protocol (MCP) Server that provides cron expression validation using the Quartz Scheduler library.

## Features

- **check_cron_validity**: Validates cron expressions using `org.quartz.CronExpression.isValidExpression`

## Dependencies

- [MCP Java SDK](https://github.com/modelcontextprotocol/java-sdk) - Model Context Protocol implementation for Java
- [Quartz Scheduler](https://www.quartz-scheduler.org/) - Job scheduling library with robust cron expression support
- Jackson - JSON processing
- SLF4J - Logging

## Building

```bash
mvn clean compile
mvn package
```

## Running

```bash
java -jar target/quartz-mcp-server-1.0.0.jar
```

Or run the shaded JAR:

```bash
java -jar target/quartz-mcp-server-1.0.0-shaded.jar
```

## Amazon Q Developer Configuration

To use this MCP server with Amazon Q Developer, add the following configuration to your MCP settings:

```json
{
    "mcpServers": {
        "quartz": {
            "command": "java",
            "args": ["-jar", "path/to/quartz-mcp-server-1.0.0.jar"]
        }
    }
}
```

Replace `path/to/quartz-mcp-server-1.0.0.jar` with the actual path to your built JAR file.

## Tool: check_cron_validity

Validates a cron expression using Quartz's `CronExpression.isValidExpression` method.

### Input

- `cron_expression` (string): The cron expression to validate (e.g., "0 0 12 * * ?")

### Output

- `cron_expression`: The input expression
- `is_valid`: Boolean indicating if the expression is valid
- `description`: Human-readable description of the result
- `summary`: The cron expression (if valid)
- `error`: Error message (if invalid)

### Example Usage

```json
{
  "tool": "check_cron_validity",
  "arguments": {
    "cron_expression": "0 0 12 * * ?"
  }
}
```

Response:
```json
{
  "cron_expression": "0 0 12 * * ?",
  "is_valid": true,
  "summary": "0 0 12 * * ?",
  "description": "Valid cron expression"
}
```

## Cron Expression Format

This server uses Quartz cron expressions which support:
- Seconds (0-59)
- Minutes (0-59) 
- Hours (0-23)
- Day of month (1-31)
- Month (1-12 or JAN-DEC)
- Day of week (1-7 or SUN-SAT, where 1=Sunday)
- Year (optional, 1970-2099)
