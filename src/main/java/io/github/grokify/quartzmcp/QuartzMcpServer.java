package io.github.grokify.quartzmcp;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Quartz MCP Server
 * Provides cron expression validation capabilities via Model Context Protocol
 */
public class QuartzMcpServer {
    
    private static final Logger logger = LoggerFactory.getLogger(QuartzMcpServer.class);
    
    public static void main(String[] args) {
        try {
            logger.info("Starting Quartz MCP Server...");
            
            // Create transport provider (stdio for MCP)
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            // Create and configure the MCP server
            var syncServer = McpServer.sync(transportProvider)
                .serverInfo("quartz-mcp-server", "1.0.0")
                .capabilities(ServerCapabilities.builder()
                    .resources(false, false)  // No resource support
                    .tools(true)              // Tool support enabled
                    .prompts(false)           // No prompt support
                    .logging()                // Enable logging
                    .build())
                .tools(
                    // Register the cron validation tool
                    CronValidationTool.createToolSpecification()
                )
                .build();
            
            logger.info("Registered cron validation tool: check_cron_validity");
            logger.info("Quartz MCP Server started successfully on stdio transport");
            
            // Keep the server running
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down Quartz MCP Server...");
                syncServer.close();
                logger.info("Server shutdown complete");
            }));
            
            // Wait for the server to finish - keep alive indefinitely
            Thread.currentThread().join();
            
        } catch (Exception e) {
            logger.error("Failed to start Quartz MCP Server", e);
            System.exit(1);
        }
    }
}