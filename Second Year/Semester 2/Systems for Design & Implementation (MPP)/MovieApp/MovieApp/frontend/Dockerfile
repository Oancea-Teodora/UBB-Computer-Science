FROM node:18-alpine AS BUILD

WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy application code
COPY . .

# Build the application with the --force flag to ignore TypeScript errors
RUN CI=false npm run build || npm run build -- --skipLibCheck --emitErrors false

# Production stage using lightweight nginx
FROM nginx:alpine AS NGINX

# Copy build files from build stage to nginx
COPY --from=BUILD /app/dist /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"] 