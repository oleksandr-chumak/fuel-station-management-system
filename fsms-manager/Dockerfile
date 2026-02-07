# Stage 1: Build the Angular application
FROM node:20 AS build

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application
COPY . .

# Build the application
RUN npm run build -- --configuration production

# Stage 2: Serve the application with Nginx
FROM nginx:alpine

# Remove default Nginx config
RUN rm -rf /etc/nginx/conf.d/*

# Copy custom Nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the build output to replace the default nginx contents
COPY --from=build /app/dist/webgui/browser/ /usr/share/nginx/html/

# Copy and make entrypoint script executable
COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh

# Expose port 80
EXPOSE 80

# Start with our entrypoint script
ENTRYPOINT ["/docker-entrypoint.sh"]