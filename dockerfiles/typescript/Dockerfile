# Use a base image for Node.js
FROM node:14-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and tsconfig.json first to leverage Docker cache
COPY . .

# Install TypeScript dependencies
RUN npm install

# Compile TypeScript to JavaScript
RUN npm run start

# Command to run the JavaScript program
CMD ["node", "dist/Solution.js"]
