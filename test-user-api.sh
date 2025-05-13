#!/bin/bash
# Testing the User Management API endpoints

# Base URL
BASE_URL="http://localhost:8080"

# Admin credentials
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin123"

# Super admin credentials
SUPER_ADMIN_USERNAME="superadmin"
SUPER_ADMIN_PASSWORD="superadmin123"

# Function to get authentication token
get_token() {
    local username=$1
    local password=$2
    
    local response=$(curl -s -X POST "$BASE_URL/auth/login" \
        -H "Content-Type: application/json" \
        -d "{\"username\":\"$username\",\"password\":\"$password\"}")
    
    local token=$(echo $response | jq -r '.token')
    echo $token
}

# Get admin token
admin_token=$(get_token $ADMIN_USERNAME $ADMIN_PASSWORD)
echo "Admin token: $admin_token"

# Get super admin token
super_admin_token=$(get_token $SUPER_ADMIN_USERNAME $SUPER_ADMIN_PASSWORD)
echo "Super Admin token: $super_admin_token"

# Test: Get all users (as admin)
echo -e "\nTest: Get all users (as admin)"
curl -s -X GET "$BASE_URL/users" \
    -H "Authorization: Bearer $admin_token" | jq

# Test: Get all users (as super admin)
echo -e "\nTest: Get all users (as super admin)"
curl -s -X GET "$BASE_URL/users" \
    -H "Authorization: Bearer $super_admin_token" | jq

# Test: Create a new user (as super admin)
echo -e "\nTest: Create a new user (as super admin)"
curl -s -X POST "$BASE_URL/users" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $super_admin_token" \
    -d '{
        "firstName": "Test",
        "lastName": "User",
        "email": "testuser@school.com",
        "username": "testuser",
        "password": "Password123!",
        "role": "TEACHER",
        "isActive": true
    }' | jq

# Get the newly created user ID for further tests
new_user_id=$(curl -s -X GET "$BASE_URL/users" \
    -H "Authorization: Bearer $super_admin_token" | \
    jq '.[] | select(.email=="testuser@school.com") | .id')

echo -e "\nNew user ID: $new_user_id"

# Test: Update user (as admin)
echo -e "\nTest: Update user (as admin)"
curl -s -X PUT "$BASE_URL/users/$new_user_id" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $admin_token" \
    -d '{
        "firstName": "Updated",
        "lastName": "User",
        "email": "testuser@school.com",
        "username": "testuser",
        "phoneNumber": "1234567890"
    }' | jq

# Test: Deactivate user (as admin)
echo -e "\nTest: Deactivate user (as admin)"
curl -s -X POST "$BASE_URL/users/$new_user_id/deactivate" \
    -H "Authorization: Bearer $admin_token" | jq

# Test: Activate user (as super admin)
echo -e "\nTest: Activate user (as super admin)"
curl -s -X POST "$BASE_URL/users/$new_user_id/activate" \
    -H "Authorization: Bearer $super_admin_token" | jq

# Test: Reset user password (as super admin)
echo -e "\nTest: Reset user password (as super admin)"
curl -s -X POST "$BASE_URL/users/$new_user_id/reset-password" \
    -H "Authorization: Bearer $super_admin_token" | jq

# Test: Delete user (as super admin)
echo -e "\nTest: Delete user (as super admin)"
curl -s -X DELETE "$BASE_URL/users/$new_user_id" \
    -H "Authorization: Bearer $super_admin_token"

echo -e "\nTests completed"
