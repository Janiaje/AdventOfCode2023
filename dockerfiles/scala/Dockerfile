# Use a base image for Scala
FROM hseeberger/scala-sbt:17.0.2_1.6.2_2.13.8

# Set the working directory inside the container
WORKDIR /app

# Copy the Scala source code into the container
COPY . .

# Compile and run the Scala program
RUN sbt "runMain Solution"

# Command to run the compiled program
CMD ["sbt", "runMain Solution"]
