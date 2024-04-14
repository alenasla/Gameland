# Gameland

## Overview
**Gameland** is a digital game store application that allows users to browse, search, and manage video game products. The application in **its current state** is designed to demonstrate the integration of Android applications with Firebase by following the CRUD (Create, Read, Update, Delete) principles for data management. 

## Technology Stack & Open-Source libraries
- **Android SDK**: version 34
- **[Kotlin](https://kotlinlang.org/docs/home.html)**
- **[Firebase](https://firebase.google.com/products/database)**
- **[Material Components](https://material.io/develop/android)**: Implements Material Design components, application utilizes CardView, RecyclerView components. 
- **[Glide](https://bumptech.github.io/glide/)**: Manages image loading and caching.
- 
- **Backend**: Firebase Realtime Database, Firebase Authentication, Firebase Storage
  
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
  - Used to store product and manage user-specific data, such as cart items, allowing for real-time updates and device synchronization.

### Firebase Storage
  - Used to store and retrieve product images, ensuring fast and scalable access to media files.

### Product Search Functionality
  - Integrated search feature allows to quickly find products by their name.

### Cart Functionality
  - Users can add products to their personal cart where items are saved within their user session.



    
