# Car Rental Management App

A comprehensive Car Rental Management System built with Kotlin and Jetpack Compose for Android. This application demonstrates proper OOP principles, MVVM architecture, Room database integration, and full CRUD operations.

## 🎓 University Assignment

This project was created as a university assignment to demonstrate advanced Android development concepts including:
- Modern Android development with Jetpack Compose
- Room Database for local data persistence
- MVVM (Model-View-ViewModel) architecture
- Repository pattern for data management
- Proper Object-Oriented Programming principles
- Comprehensive input validation
- Material Design 3 theming

## ✨ Features

### Core Functionality
- **Add Cars**: Create new car entries with full validation
- **Edit Cars**: Update existing car information
- **Delete Cars**: Remove individual cars with confirmation dialogs
- **List Cars**: Display all cars in a beautiful, scrollable list
- **View Details**: See comprehensive information for each car

### Advanced Features
- **Multi-Select Mode**: Select multiple cars for batch operations
- **Batch Edit**: Update multiple cars simultaneously (daily rate, color, rental status)
- **Batch Delete**: Delete multiple selected cars at once
- **Delete All**: Remove all cars from the database (with strong confirmation)
- **Search**: Filter cars by brand, model, or license plate
- **Sort Options**: Sort by brand, year, daily rate, or rental status
- **Filter Options**: View all cars, available only, or rented only
- **Toggle Rental Status**: Quick toggle for marking cars as rented/available
- **Statistics Dashboard**: View total cars, available count, rented count, and daily revenue

### Data Validation
All input fields are validated with helpful error messages:
- **Brand**: 2-50 characters, required
- **Model**: 1-50 characters, required
- **Year**: 1900 to current year + 1, required
- **Color**: 2-30 characters, required
- **License Plate**: 5-10 alphanumeric characters with hyphens, required
- **Daily Rate**: Positive number up to $10,000, required
- **Notes**: Optional field for additional information

## 🏗️ Architecture

### Project Structure
```
app/src/main/java/com/example/carrentall/
├── data/
│   ├── model/
│   │   └── CarItem.kt                 # Entity with validation logic
│   ├── dao/
│   │   └── CarItemDao.kt              # Data Access Object (Room)
│   ├── database/
│   │   └── AppDatabase.kt             # Room Database singleton
│   └── repository/
│       └── CarRepository.kt           # Repository pattern implementation
├── ui/
│   ├── viewmodel/
│   │   └── CarViewModel.kt            # ViewModel with StateFlows
│   ├── screens/
│   │   └── CarListScreen.kt           # Main screen composable
│   ├── components/
│   │   ├── CarListItem.kt             # Car card component
│   │   ├── CarDialog.kt               # Add/Edit dialog
│   │   ├── BatchEditDialog.kt         # Batch edit dialog
│   │   └── EmptyState.kt              # Empty state component
│   └── theme/
│       ├── Color.kt                   # Dark blue color palette
│       ├── Theme.kt                   # Material3 theme
│       └── Type.kt                    # Typography
└── MainActivity.kt                     # Entry point
```

### Design Patterns

1. **MVVM (Model-View-ViewModel)**
   - Model: `CarItem` entity with validation logic
   - View: Jetpack Compose UI components
   - ViewModel: `CarViewModel` managing UI state and business logic

2. **Repository Pattern**
   - `CarRepository` abstracts data source operations
   - Provides clean API for ViewModel
   - Handles Dispatcher.IO context switching

3. **Singleton Pattern**
   - `AppDatabase` ensures single database instance
   - Thread-safe initialization with synchronized block

4. **Observer Pattern**
   - Room's Flow for reactive data updates
   - StateFlow for UI state management
   - Automatic UI updates when data changes

## 🗄️ Database Schema

### CarItem Table
```sql
CREATE TABLE car_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    brand TEXT NOT NULL,
    model TEXT NOT NULL,
    year INTEGER NOT NULL,
    color TEXT NOT NULL,
    license_plate TEXT NOT NULL,
    daily_rate REAL NOT NULL,
    is_rented INTEGER NOT NULL DEFAULT 0,
    notes TEXT NOT NULL DEFAULT ''
);
```

## 🎨 UI/UX Design

### Theme
- **Primary Color**: Dark Blue (#1565C0)
- **Secondary Color**: Accent Orange (#FF9800)
- **Background**: Light Gray (#ECEFF1) / Dark Gray (#263238) for dark mode
- **Modern Material Design 3** with smooth animations

### Key UI Components
- **Material3 Cards**: Elevated cards with rounded corners for each car
- **Floating Action Button**: Quick access to add new cars
- **Top App Bar**: Navigation and action buttons
- **Chips**: Visual rental status indicators
- **Dialogs**: Modal dialogs for add, edit, and confirmations
- **Snackbars**: Feedback for user actions
- **Search Bar**: Integrated search functionality
- **Statistics Card**: Dashboard showing key metrics

## 📱 Sample Data

The app pre-populates with 5 sample cars on first launch:
1. Toyota Camry 2023 - $45.99/day (Available)
2. Ford Mustang 2022 - $89.99/day (Rented)
3. Honda Civic 2024 - $39.99/day (Available)
4. Tesla Model 3 2023 - $129.99/day (Available)
5. Chevrolet Suburban 2023 - $95.00/day (Rented)

## 🔧 Technologies Used

- **Kotlin**: Modern programming language for Android
- **Jetpack Compose**: Declarative UI framework
- **Room Database**: Local database with SQLite
- **Coroutines & Flow**: Asynchronous programming
- **Material Design 3**: Modern UI components
- **ViewModel & StateFlow**: State management
- **KSP (Kotlin Symbol Processing)**: Annotation processing for Room

## 📦 Dependencies

```toml
[versions]
kotlin = "2.0.21"
room = "2.6.1"
compose = "2024.09.00"
lifecycle = "2.7.0"

[libraries]
androidx-room-runtime
androidx-room-ktx
androidx-room-compiler (KSP)
androidx-lifecycle-viewmodel-compose
androidx-compose-material3
androidx-compose-material-icons-extended
kotlinx-coroutines-android
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24 (Android 7.0) or higher
- Kotlin 2.0.21 or later

### Installation
1. Clone or download the project
2. Open in Android Studio
3. Wait for Gradle sync to complete
4. Run the app on an emulator or physical device

### Building the Project
```bash
./gradlew assembleDebug
```

### Running Tests
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 📖 Usage Guide

### Adding a Car
1. Tap the floating "+" button
2. Fill in all required fields (marked with *)
3. Optionally toggle rental status and add notes
4. Tap "Add Car" to save

### Editing a Car
1. Tap "Edit" button on any car card
2. Modify the desired fields
3. Tap "Update Car" to save changes

### Deleting a Car
1. Tap "Delete" button on any car card
2. Confirm deletion in the dialog

### Multi-Select Operations
1. Tap the checklist icon in the top app bar
2. Select desired cars by tapping their checkboxes
3. Use top bar buttons to:
   - Edit selected cars (batch edit)
   - Delete selected cars (batch delete)
   - Select/Deselect all
4. Tap the X to exit multi-select mode

### Filtering & Sorting
1. Tap the filter icon to show all, available, or rented cars
2. Tap the sort icon to sort by brand, year, daily rate, or rental status

### Searching
1. Type in the search bar at the top
2. Search by brand, model, or license plate
3. Tap X to clear search

## 🎯 OOP Principles Demonstrated

1. **Encapsulation**: Data and validation logic encapsulated in `CarItem` data class
2. **Abstraction**: Repository and DAO abstract database operations
3. **Single Responsibility**: Each class has one clear purpose
4. **Separation of Concerns**: MVVM architecture separates UI, business logic, and data
5. **Dependency Injection**: ViewModel receives repository dependency
6. **Immutability**: Data classes with val properties, StateFlow for reactive state

## 🎓 Learning Outcomes

This project demonstrates proficiency in:
- Modern Android development practices
- Database design and SQL operations
- Reactive programming with Flows
- State management in Compose
- Form validation and error handling
- Material Design guidelines
- Clean architecture principles
- Testing and debugging

## 📝 License

This project is created for educational purposes as part of a university assignment.

## 👨‍💻 Author

Created as a university assignment for Android development course.

---

**Note**: This is a fully functional application with proper error handling, validation, and user feedback. All CRUD operations are implemented with both single and batch operations support.

