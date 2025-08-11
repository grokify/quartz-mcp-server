# Quartz MCP Server

[![Build Status][build-status-svg]][build-status-url]
[![SAST Security Analysis][sast-status-svg]][sast-status-url]
[![Docs][docs-javadoc-svg]][docs-javadoc-url]
[![License][license-svg]][license-url]

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
java -jar target/quartz-mcp-server-0.1.1.jar
```

Or run the shaded JAR:

```bash
java -jar target/quartz-mcp-server-0.1.1-shaded.jar
```

## AI Assistant Configuration

To use this MCP server with popular AI assistants, add the following configuration to your MCP settings:

```json
{
    "mcpServers": {
        "quartz": {
            "command": "java",
            "args": ["-jar", "path/to/quartz-mcp-server-0.1.1.jar"]
        }
    }
}
```

Replace `path/to/quartz-mcp-server-0.1.1.jar` with the actual path to your built JAR file.

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

 [build-status-svg]: https://github.com/grokify/quartz-mcp-server/actions/workflows/ci.yaml/badge.svg?branch=main
 [build-status-url]: https://github.com/grokify/quartz-mcp-server/actions/workflows/ci.yaml
 [sast-status-svg]: https://github.com/grokify/quartz-mcp-server/actions/workflows/sast.yaml/badge.svg?branch=main
 [sast-status-url]: https://github.com/grokify/quartz-mcp-server/actions/workflows/sast.yaml
 [sca-status-svg]: https://github.com/grokify/quartz-mcp-server/actions/workflows/sca.yaml/badge.svg?branch=main
 [sca-status-url]: https://github.com/grokify/quartz-mcp-server/actions/workflows/sca.yaml
 [docs-javadoc-svg]: https://img.shields.io/badge/reference-Javadoc-blue.svg
 [docs-javadoc-url]: https://grokify.github.io/quartz-mcp-server/
 [license-svg]: https://img.shields.io/badge/license-MIT-blue.svg
 [license-url]: https://github.com/grokify/quartz-mcp-server/blob/main/LICENSE
