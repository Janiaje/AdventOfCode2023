# Use a base image for Perl
FROM perl:latest

# Set the working directory inside the container
WORKDIR /app

# Copy the Perl script into the container
COPY . .

# Make the script executable
RUN chmod +x Solution.pl

# Command to run the Perl script
CMD ["./Solution.pl"]
