# Use a base image with Java installed
FROM openjdk:latest

# Set the working directory inside the container
WORKDIR /app

# Copy the Java source file into the container
COPY . .

# Compile the Java source file
RUN javac Solution.java

# Command to run the Java application
CMD ["java", "Solution"]
