# Stage 1: Build application (Image with Node)
FROM node:20-alpine AS build
WORKDIR /app

# Copy dependency files and install
COPY package*.json ./
RUN npm install

# Copy all source code
COPY . .

# Run Vite build process
RUN npm run build

# Stage 2: Create final image (Nginx to serve static files)
FROM nginx:alpine

# Copy static files generated in stage 1
COPY --from=build /app/dist /usr/share/nginx/html

# Expose Nginx port 80
EXPOSE 80

# Initialize Nginx (default mode already serves files from html directory)
CMD ["nginx", "-g", "daemon off;"]
