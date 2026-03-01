export interface CacheEntry<T> {
    timestamp: number;
    data: T;
}

export class CacheService {
    private static instance: CacheService;
    private cache: Record<string, CacheEntry<any>> = {};
    private DEFAULT_TTL = 5 * 60 * 1000; // 5 minutes

    private constructor() { }

    public static getInstance(): CacheService {
        if (!CacheService.instance) {
            CacheService.instance = new CacheService();
        }
        return CacheService.instance;
    }

    // Get a cached item or execute the function to get fresh data
    public async getOrSet<T>(
        key: string,
        dataFn: () => Promise<T>,
        ttl: number = this.DEFAULT_TTL
    ): Promise<{ data: T, fromCache: boolean, executionTime: number }> {
        const now = Date.now();
        const cached = this.cache[key];

        // Return cached data if valid
        if (cached && now - cached.timestamp < ttl) {
            console.log(`Cache hit for ${key}`);
            return {
                data: cached.data,
                fromCache: true,
                executionTime: 0 // Instant from cache
            };
        }

        // Otherwise execute function and cache result
        console.log(`Cache miss for ${key}, executing function`);
        const startTime = Date.now();
        const data = await dataFn();
        const executionTime = Date.now() - startTime;

        this.cache[key] = {
            timestamp: now,
            data
        };

        return {
            data,
            fromCache: false,
            executionTime
        };
    }

    // Clear a specific cache key
    public clear(key: string): void {
        if (this.cache[key]) {
            delete this.cache[key];
            console.log(`Cache cleared for key: ${key}`);
        }
    }

    // Clear all cache
    public clearAll(): void {
        this.cache = {};
        console.log('Complete cache cleared');
    }
}
