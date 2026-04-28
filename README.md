

#  [SokoData]

**Digital Registry for Local Market Vendors**

### Project Overview
SokoData is a native Android application designed to modernize the management and census of vendors in large commercial areas such as municipal markets and trade fairs. 

Moving away from error-prone physical ledgers, this solution provides market administrators with a powerful tool to accurately track vendor locations, commercial activities, and stall visuals in real-time.

### Key Features
* **Full CRUD Management:** Seamlessly Create, Read, Update, and Delete vendor profiles.
* **Visual Identification:** Integrated camera functionality to capture and link stall photos to specific vendors.
* **Dynamic Search:** Real-time filtering by name or stall number for instant on-the-ground identification.
* **Cloud Synchronization:** Reliable data persistence and image hosting powered by the **Supabase** ecosystem.
* **Responsive UX:** Built with a focus on speed and clarity for administrators working in high-traffic environments.

### Technical Stack
* **Interface:** 100% **Jetpack Compose** with **Material Design 3** (Material Theme Builder, Custom Typography).
* **Language:** Kotlin.
* **Architecture:** **MVVM** (Model-View-ViewModel) implementing **State Hoisting** for clean logic separation.
* **State Management:** Reactive UI updates using **StateFlow**.
* **Backend (BaaS):** **Supabase** (PostgreSQL database & Storage buckets).
* **Networking & Async:** **Kotlin Coroutines** and **Coil** for asynchronous image loading.


