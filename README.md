# Welcome to MyGalleryApp!

MyGalleryApp is an Android application that allows users to browse and view media content such as images and videos stored on their device. The app utilizes modern Android development practices, following the MVVM architecture pattern with a clean architecture approach.

## Features

 1. Home Screen: The initial screen displays all media folders,
    including "All Images" and "All Videos" as the first and second
    items, respectively, in a grid layout.
 2. Folder View: Users can tap on any folder to view the media content inside that specific folder on a secondary screen.   
 3. Permissions: The app requests and handles storage permissions to access media content on the user's device.


## Tech Stack
-   **Language**: Kotlin
-   **Architecture**: MVVM (Model-View-ViewModel) with clean architecture.
-   **Libraries Used**:
    -    **Hilt (Dagger)**: Dependency injection framework for streamlined and efficient dependency management.
    -  **Coroutines**: Used for asynchronous and non-blocking programming to handle background tasks.
    -   **MVVM Architecture**: Follows the Model-View-ViewModel architecture pattern for separation of concerns, and maintaining a clean codebase. 
     -   **Clean Architecture**: Adopts clean architecture principles, separating layers into presentation, domain, and data layers for better modularity.
    -   **Glide** for image loading
    -   **RecyclerView** and **DiffUtil** for efficient list display
    - **ViewBinding:** Integrated for streamlined and type-safe access to UI components, reducing null pointer exceptions and enhancing code readability.
    - **Navigation Graph:** The app employs Android's Navigation Component with a clear navigation graph, managing various destinations and actions for seamless screen transitions and data passing.
## Getting Started
To run the project locally, follow these steps:
-   Clone the repository
-   Open the project in Android Studio.
-   Build and run the app on an Android device or emulator.


## Project Structure

#### 1. `GalleryRepositoryImpl` (Package: `feature_gallery.data.local.repository`)

This class manages the local data source for the gallery app. It interacts with the device's MediaStore to fetch media folders and their contents.

Improvements:

-   Code restructuring for better readability and organization.
-   Separation of concerns within methods for easier maintenance.
-   Utilization of Kotlin Flow for asynchronous data emission.

#### 2. `AlbumItemAdapter` and `MediaItemAdapter` (Package: `feature_gallery.presentation.adapter`)

These adapters handle the RecyclerView display for media folders and media items (images/videos) respectively.

Improvements:

-   Implementation of DiffUtil for efficient list updates.
-   Clear separation of responsibilities for ViewHolder and item binding.
-   Glide integration for smooth image loading.

#### 3. Fragments (`AlbumFolderFragment` and `MediaFolderFragment`) (Package: `feature_gallery.presentation.ui.fragment`)

These fragments represent the UI elements displaying the media folders and their contents.

Improvements:

-   Permission handling logic for Android 11 (R) and below.
-   Integration with ViewModel (`GalleryViewModel`) to fetch and display data.

#### 4. `GalleryViewModel` (Package: `feature_gallery.presentation.vm`)

The ViewModel orchestrates data flow between the UI and repository, responsible for fetching media folders and their respective media items.

Improvements:

-   Utilization of StateFlow for reactive UI updates.
-   Scoped coroutines for handling asynchronous operations.
#### 4. GalleryModule` (Package: `com.example.mygalleryapp.feature_gallery.di`)

The `GalleryModule` is responsible for providing dependencies using Dagger Hilt for various components within the Gallery feature.

#### Provided Dependencies:

-   **Glide Configuration:**
    
    -   Utilizes Glide for image loading throughout the app.
    -   Provides default request options such as placeholder and error images.
-   **Adapters:**
    
    -   `MediaItemAdapter`: Adapter for handling media items (images/videos) display in RecyclerViews.
    -   `AlbumItemAdapter`: Adapter for managing media folders display in RecyclerViews.
-   **Use Cases:**
    
    -   `GetMediaFolderUseCase`: Use case to retrieve media folders from the repository.
    -   `GetAllMediaFilesFromFolderUseCase`: Use case to fetch all pictures and videos from a specific folder via the repository.
-   **Repository:**
    
    -   `GalleryRepository`: Provides an instance of `GalleryRepositoryImpl` for managing local data sources, specifically interacting with the device's MediaStore.

### Note on UI Development and Device Testing

Due to time constraints, the UI might not showcase the full potential envisioned for this application. Given more time, further enhancements, refinements, testing, and visual improvements could be made to deliver a more polished and visually appealing user interface.

Additionally, it's essential to highlight that the application has been primarily tested on Realme XT running (OS 11) and Xiaomi Pad 6 (OS 13 ) devices. Regrettably, due to the unavailability of other real devices containting diverse configurations and media content, testing has been limited to these specific devices. The application functions properly on these devices, ensuring basic functionality for images and videos.

This project primarily serves as a demonstration of coding proficiency, showcasing robust backend logic, clean architecture, and efficient data handling. While the current iteration might not emphasize elaborate design or intricate user interface elements, it aims to highlight the developer's expertise in structuring and implementing backend functionalities, following industry-standard practices and clean coding principles.
