#!/bin/bash

# Script to test Workflow Service locally

echo "========================================="
echo "TechQuarter Workflow Service - Local Test"
echo "========================================="
echo ""

# Build the application
echo "1. Building application..."
mvn clean install -DskipTests -q
if [ $? -eq 0 ]; then
    echo "   ✅ Build successful"
else
    echo "   ❌ Build failed"
    exit 1
fi

echo ""
echo "2. Starting application on port 8080..."
java -jar target/workflow-service-0.0.1-SNAPSHOT.jar &
APP_PID=$!
sleep 8

echo "   ✅ Application started (PID: $APP_PID)"
echo ""

# Test endpoints
echo "========================================="
echo "Testing Endpoints"
echo "========================================="
echo ""

# Test 1: Create Employee
echo "1. Creating Employee (POST /employees)..."
EMPLOYEE_RESPONSE=$(curl -s -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP_TEST_001",
    "name": "Test Employee",
    "email": "test@company.com",
    "costCenter": "CC-TEST"
  }')

echo "$EMPLOYEE_RESPONSE" | jq . 2>/dev/null || echo "$EMPLOYEE_RESPONSE"
echo ""

# Test 2: Get Employee
echo "2. Getting Employee (GET /employees/EMP_TEST_001)..."
GET_RESPONSE=$(curl -s -X GET http://localhost:8080/employees/EMP_TEST_001 \
  -H "Content-Type: application/json")

echo "$GET_RESPONSE" | jq . 2>/dev/null || echo "$GET_RESPONSE"
echo ""

# Test 3: Create Booking
echo "3. Creating Booking (POST /bookings)..."
BOOKING_RESPONSE=$(curl -s -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP_TEST_001",
    "resourceType": "FLIGHT",
    "destination": "London",
    "departureDate": "2024-12-15T10:00:00",
    "returnDate": "2024-12-20T18:00:00",
    "travelerCount": 2,
    "costCenterRef": "CC-TEST",
    "tripPurpose": "Business Conference"
  }')

echo "$BOOKING_RESPONSE" | jq . 2>/dev/null || echo "$BOOKING_RESPONSE"
echo ""

# Test 4: List All Bookings
echo "4. Listing All Bookings (GET /bookings)..."
BOOKINGS_LIST=$(curl -s -X GET http://localhost:8080/bookings \
  -H "Content-Type: application/json")

echo "$BOOKINGS_LIST" | jq . 2>/dev/null || echo "$BOOKINGS_LIST"
echo ""

echo "========================================="
echo "Tests completed!"
echo "========================================="
echo ""
echo "To stop the application:"
echo "kill $APP_PID"
echo ""
