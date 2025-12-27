#!/bin/bash
# Library Management System - Unix Startup Script
# Version 2.0.0

echo "================================"
echo "Library Management System v2.0.0"
echo "================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Java is installed
echo -n "[1/4] Checking Java installation... "
if ! command -v java &> /dev/null; then
    echo -e "${RED}FAILED${NC}"
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java JDK 11 or higher"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo -e "${RED}FAILED${NC}"
    echo "ERROR: Java 11 or higher is required"
    exit 1
fi
echo -e "${GREEN}OK${NC}"

# Check if Maven is installed
echo -n "[2/4] Checking Maven installation... "
if ! command -v mvn &> /dev/null; then
    echo -e "${YELLOW}WARNING${NC}"
    echo "Maven not found. Please build manually if needed."
else
    echo -e "${GREEN}OK${NC}"
fi

# Check if JAR exists, build if not
echo -n "[3/4] Checking application JAR... "
if [ ! -f "target/library-management-system-2.0.0.jar" ]; then
    echo -e "${YELLOW}NOT FOUND${NC}"
    echo "Building application with Maven..."
    mvn clean package -DskipTests
    if [ $? -ne 0 ]; then
        echo -e "${RED}ERROR: Maven build failed${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}OK${NC}"
fi

# Create logs directory if it doesn't exist
mkdir -p logs

# Set Java options
JAVA_OPTS="-Xms512m -Xmx1024m"

echo -e "\n[4/4] Starting application...\n"
echo "================================"
echo "Application is starting..."
echo "Press Ctrl+C to stop"
echo "================================"
echo ""

# Run the application
java $JAVA_OPTS -jar target/library-management-system-2.0.0.jar

# Exit handler
trap "echo -e '\n${YELLOW}Application stopped${NC}'; exit 0" INT TERM
