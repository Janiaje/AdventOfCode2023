FROM ubuntu:latest AS builder

# Install necessary tools, including Kotlin Compiler
RUN apt-get update && \
    apt-get install -y wget unzip && \
    wget -q https://github.com/JetBrains/kotlin/releases/download/v1.9.21/kotlin-compiler-1.9.21.zip && \
    unzip kotlin-compiler-1.9.21.zip -d /usr/local && \
    rm kotlin-compiler-1.9.21.zip

FROM openjdk:latest

COPY --from=builder /usr/local/kotlinc /usr/local/kotlinc

RUN ls -lah /usr/local/kotlinc/bin

# Add Kotlin Compiler to the PATH
ENV PATH=$PATH:/usr/local/kotlinc/bin

# Set working directory
WORKDIR /app

COPY Day4.kt /app/Solution.kt
COPY input.txt /app/src/Day4/kotlin/input.txt

# Build your Kotlin code
RUN kotlinc Solution.kt -include-runtime -d Solution.jar

# Set the entrypoint to run the Kotlin application
ENTRYPOINT ["java", "-jar", "Solution.jar"]
