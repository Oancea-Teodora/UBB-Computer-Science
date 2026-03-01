FROM node:18-alpine

WORKDIR /app

# Install dependencies required for TypeORM, SQLite, and PostgreSQL
RUN apk add --no-cache python3 make g++ sqlite postgresql-client

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy tsconfig files first
COPY tsconfig*.json ./

# Copy application code
COPY . .

# Create data directory for database persistence (SQLite fallback)
RUN mkdir -p /app/data
ENV DB_PATH=/app/data/db.sqlite

# Install ts-node for running TypeScript directly
RUN npm install -g ts-node

# If database file exists, copy it to data directory
RUN if [ -f "db.sqlite" ]; then cp db.sqlite /app/data/; fi

# Expose the port the app runs on
EXPOSE 3000

# Set environment variables for production
ENV NODE_ENV=production
ENV TS_NODE_PROJECT="tsconfig.build.json"

# Run the application using ts-node with the less strict config
CMD ["npx", "ts-node", "--project", "tsconfig.build.json", "src/index.ts"] 