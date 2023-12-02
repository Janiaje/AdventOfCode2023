# Use a base image for Rust
FROM rust:latest

# Set the working directory inside the container
WORKDIR /app

# Copy the Rust source code into the container
COPY . .

# Compile the Rust program
RUN rustc Solution.rs

# Command to run the compiled program
CMD ["./Solution"]
