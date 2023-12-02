# Use a base image for GCC
FROM gcc:latest

# Set the working directory inside the container
WORKDIR /app

# Copy the Fortran source code into the container
COPY . .

# Compile and run the Fortran program
RUN gfortran -o Solution Solution.f90

# Command to run the compiled program
CMD ["./Solution"]
