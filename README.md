# 🎯 Habit Tracker App with Gamified Reward System

A fully-featured Android habit tracking app with gamification elements built using modern Android development practices. Turn your daily habits into a rewarding game!

## ✨ Features

### 📋 Habit Management
- **Create Custom Habits** - Add any habit you want to track with customizable point values
- **Track Daily Progress** - Mark habits as complete each day
- **Habit Descriptions** - Add optional descriptions to remind yourself why the habit matters
- **Edit & Delete** - Long-press any habit to edit or remove it

### 🎮 Gamification System
- **Points & XP** - Earn points and experience for completing habits
- **Level Up** - Gain levels as you accumulate XP (100 XP per level)
- **Streak Bonuses** - Get extra points for maintaining consecutive day streaks:
  - 3+ days: +1 bonus point
  - 7+ days: +2 bonus points  
  - 14+ days: +3 bonus points
  - 30+ days: +5 bonus points
- **Visual Feedback** - See your current level, XP progress, and available points at the top

### 🎁 Reward Shop
- **Custom Rewards** - Create personalized rewards to work towards
- **Point Cost** - Set how many points each reward costs
- **Redeem Rewards** - "Purchase" rewards using your earned points
- **Track Usage** - See how many times you've redeemed each reward

### 📊 Statistics Dashboard
- **Total Habits** - See how many habits you're tracking
- **Completion Rate** - View your overall completion percentage
- **Total Points Earned** - Track all the points you've accumulated
- **Longest Streak** - Celebrate your best consecutive completion streak

### 🎨 User Experience
- **Material Design** - Beautiful, modern Material Components UI
- **Dark/Light Theme** - Automatic theme switching based on system preferences
- **Tab Navigation** - Easy switching between Habits, Rewards, and Stats
- **Smooth Animations** - Polished transitions and feedback
- **Intuitive Dialogs** - Simple forms for adding/editing items

## 🏗️ Technical Architecture

### Tech Stack
- **Language:** Kotlin 1.9.22
- **UI:** Material Design Components, ViewBinding
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** Room Persistence Library
- **Async:** Kotlin Coroutines & LiveData
- **Build:** Gradle 8.2, Android Gradle Plugin 8.2.2

### Project Structure
```
app/src/main/
├── java/com/habittracker/app/
│   ├── data/
│   │   ├── Habit.kt              # Habit entity with streak logic
│   │   ├── Reward.kt             # Reward entity
│   │   ├── UserStats.kt          # User stats entity with leveling
│   │   ├── HabitDao.kt           # Habit database operations
│   │   ├── RewardDao.kt          # Reward database operations
│   │   ├── UserStatsDao.kt       # User stats operations
│   │   ├── AppDatabase.kt        # Room database configuration
│   │   └── HabitRepository.kt    # Data repository layer
│   ├── viewmodel/
│   │   └── HabitViewModel.kt     # ViewModel with LiveData
│   ├── adapter/
│   │   ├── HabitAdapter.kt       # RecyclerView adapter for habits
│   │   └── RewardAdapter.kt      # RecyclerView adapter for rewards
│   └── MainActivity.kt           # Main activity with tab navigation
└── res/
    ├── layout/                   # All XML layouts
    ├── values/                   # Strings, colors, themes
    └── ...
```

### Key Design Patterns
- **MVVM Architecture** - Clear separation of concerns
- **Repository Pattern** - Centralized data management
- **Observer Pattern** - LiveData for reactive UI updates
- **Singleton Pattern** - Database instance management
- **Adapter Pattern** - RecyclerView adapters for list items

## 📱 Screenshots

The app features three main tabs:

1. **Habits Tab** - View all your habits with current streaks, completion status, and point values. Tap the checkmark to complete a habit for the day.

2. **Rewards Tab** - Browse your custom rewards and redeem them using earned points. Rewards are disabled if you don't have enough points.

3. **Stats Tab** - View your overall progress with beautiful statistics cards showing total habits, completion rate, total points, and longest streak.

## 🚀 Getting the APK

### Option 1: Download Pre-built APK (Easiest)

1. Go to the [Actions tab](https://github.com/DweepParekh/HabitTrackerApp/actions)
2. Click on the most recent workflow run with a green checkmark ✅
3. Scroll down to "Artifacts" section
4. Download **HabitTrackerApp-debug.zip**
5. Extract the ZIP file
6. Install `app-debug.apk` on your Android device

### Option 2: Build from Source

#### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17
- Android SDK API 24+ (Android 7.0)

#### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/DweepParekh/HabitTrackerApp.git
   cd HabitTrackerApp
   ```

2. Open in Android Studio:
   - File → Open → Select the project folder
   - Wait for Gradle sync to complete

3. Build the APK:
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Or run from terminal:
     ```bash
     ./gradlew assembleDebug
     ```
   - APK location: `app/build/outputs/apk/debug/app-debug.apk`

4. Install on device:
   - Connect your Android device via USB (with USB debugging enabled)
   - Run → Run 'app'
   - Or manually transfer and install the APK

## 📋 Requirements

- **Minimum SDK:** Android 7.0 (API 24)
- **Target SDK:** Android 14 (API 34)
- **Device Storage:** ~5 MB
- **Permissions:** None required!

## 🎯 How to Use

### Getting Started
1. **Add Your First Habit**
   - Tap the + button on the Habits tab
   - Enter habit name (e.g., "Morning Exercise")
   - Add optional description
   - Set points per completion (default: 10)
   - Tap Save

2. **Complete Habits Daily**
   - Tap the ✓ button on any habit card
   - Watch your points and XP increase
   - Build streaks for bonus points

3. **Create Rewards**
   - Switch to Rewards tab
   - Tap the + button
   - Name your reward (e.g., "Movie Night")
   - Set point cost (e.g., 50 points)
   - Tap Save

4. **Redeem Rewards**
   - When you have enough points, tap Redeem
   - Confirm redemption
   - Enjoy your reward!

5. **Track Progress**
   - Check the Stats tab to see your progress
   - Monitor completion rates and streaks
   - Celebrate milestones

### Pro Tips
- **Maintain Streaks** - Complete habits daily to earn bonus points (up to +5 at 30+ days!)
- **Set Smart Point Values** - Harder habits should give more points
- **Create Meaningful Rewards** - Choose rewards that genuinely motivate you
- **Long-press** any habit or reward to edit or delete it
- **Check your level** at the top to see your progress

## 🔄 Automatic Streak Management

The app intelligently tracks your streaks:
- Complete a habit today → Streak +1 (if completed yesterday) or resets to 1
- Miss a day → Streak resets to 0
- Longest streak is always preserved as your personal record

## 🤝 Contributing

Feel free to:
- Report bugs by opening an issue
- Suggest features via issues
- Submit pull requests with improvements
- Fork and customize for your needs

## 📝 License

This project is open source and available under the MIT License.

## 🙏 Acknowledgments

Built with:
- [Kotlin](https://kotlinlang.org/) - Modern programming language
- [Android Jetpack](https://developer.android.com/jetpack) - Suite of libraries
- [Material Components](https://material.io/components) - Beautiful UI components
- [Room Database](https://developer.android.com/training/data-storage/room) - Robust local storage

## 📞 Support

If you encounter any issues:
1. Check the [Issues](https://github.com/DweepParekh/HabitTrackerApp/issues) page
2. Create a new issue with:
   - Description of the problem
   - Steps to reproduce
   - Device/Android version
   - Screenshots if applicable

---

**Made with ❤️ using Kotlin and Android Studio**

*Start building better habits today! 🚀*
