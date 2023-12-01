# Use a minimal base image for Bash
FROM alpine:latest

# Set the working directory inside the container
WORKDIR /app

# Copy the Bash script into the container
COPY . .

# Make the script executable
RUN chmod +x Solution.sh

# Command to run the Bash script with /bin/sh
CMD ["/bin/sh", "./Solution.sh"]
