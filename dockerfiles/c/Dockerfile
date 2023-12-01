# Use a minimal base image for C programs
FROM gcc:latest

# Set the working directory inside the container
WORKDIR /app

# Copy the C source file into the container
COPY . .

# Compile the C source file
RUN gcc -o Solution Solution.c

# Command to run the C program
CMD ["./Solution"]
