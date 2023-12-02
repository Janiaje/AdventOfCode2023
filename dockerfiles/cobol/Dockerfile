# Use a base image for COBOL
FROM debian:latest

# Set the working directory inside the container
WORKDIR /app

# Install the GnuCOBOL compiler
RUN apt-get update && \
    apt-get install -y gnucobol4

# Copy the COBOL source code into the container
COPY . .

# Compile and run the COBOL program
RUN cobc -x -o Solution Solution.cbl

# Command to run the compiled program
CMD ["./Solution"]
