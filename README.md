# Gameland

## Overview
**Gameland** is a digital game store application that allows users to browse, search, and manage video game products. The application in its current state is designed to demonstrate the integration of Android applications with Firebase by following the CRUD (Create, Read, Update, Delete) principles for data management. 

## Features

### User Authentication
  - Utilizes Firebase Authentication for user management.
  - **Registration**: New users can register with their email and password.
  - **Login**: Users can log in to fully access application features.
- **NOTE**: In the application's current state there is no separate user experience without successful authentication (a user must be registered and logged) in order to use the application.

### Firebase Integration
- **CRUD Operations**: The application's integration with the Firebase enables it to support:
  - **Create**: Ability to add new products to the store including product details such as: names, descriptions, categories, prices, and images.
  - **Read**:   Display products from Firebase Realtime Database to users.
  - **Update**: Modify details of already existing products.
  - **Delete**: Remove products from the database.

### Firebase Realtime Database
  - All data regarding products and user-specific data like cart items are stored and managed in Firebase Realtime Database, allowing real-time updates and synchronization across devices.

### Firebase Storage
  - Uses Firebase's storage solutions to store and retrieve product images, ensuring fast and scalable access to media files.

### Product Search Functionality
  - Integrated search feature to quickly find products by name or category.

    
