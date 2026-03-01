import { Request, Response, NextFunction } from "express";
import { AuthRequest } from "./auth";
import { LoggingService } from "../services/LoggingService";
import { ActionType, EntityType } from "../entity/Log";

// Helper function to determine action type from request method
const getActionTypeFromMethod = (method: string): ActionType => {
    switch (method.toUpperCase()) {
        case 'GET': return 'READ';
        case 'POST': return 'CREATE';
        case 'PUT':
        case 'PATCH': return 'UPDATE';
        case 'DELETE': return 'DELETE';
        default: return 'READ';
    }
};

// Helper function to determine entity type from URL
const getEntityTypeFromPath = (path: string): EntityType => {
    if (path.includes('/movies')) return 'Movie';
    if (path.includes('/directors')) return 'Director';
    if (path.includes('/users')) return 'User';
    return 'Other';
};

// Helper function to extract entity ID from URL if present
const getEntityIdFromPath = (path: string): number | undefined => {
    const matches = path.match(/\/(\d+)(?:\/|$)/);
    return matches ? parseInt(matches[1], 10) : undefined;
};

export const loggingMiddleware = async (req: AuthRequest, res: Response, next: NextFunction) => {
    const startTime = Date.now();
    const originalSend = res.send;

    // Save original URL before any modifications by other middleware
    const originalUrl = req.originalUrl || req.url;
    const method = req.method;

    // Extract entity info
    const entityType = getEntityTypeFromPath(originalUrl);
    const entityId = getEntityIdFromPath(originalUrl);
    const actionType = getActionTypeFromMethod(method);

    // Capture response
    res.send = function (body) {
        const responseTime = Date.now() - startTime;
        const statusCode = res.statusCode;
        const user = req.user;

        // Skip logging for certain endpoints like health checks
        if (!originalUrl.includes('/health') && !originalUrl.includes('/debug')) {
            try {
                const loggingService = LoggingService.getInstance();

                // Create details object with relevant info
                const details = JSON.stringify({
                    method,
                    path: originalUrl,
                    statusCode,
                    responseTime,
                    userAgent: req.headers['user-agent']
                });

                // Log the action asynchronously (don't block the response)
                loggingService.logAction(
                    actionType,
                    entityType,
                    user || null,
                    entityId,
                    details
                ).catch(err => {
                    console.error('Failed to log action:', err);
                });
            } catch (error) {
                console.error('Error in logging middleware:', error);
            }
        }

        // Call original send method
        return originalSend.call(this, body);
    };

    next();
}; 