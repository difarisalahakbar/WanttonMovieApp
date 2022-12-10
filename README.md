# Wantton Movie App

<p align="center">

Wantton is a Movie App to view information about Movies or Tv Series. This project has been made just for practice my coding skills. 

<img src="/SS/ss1.png" />
<img src="/SS/ss2.png" />  
</p>
 
## Features
* Movie list by now playing, popular and top rated.
* Tv Series list by on the air, popular and top rated.
* Search for Movie or Tv Series.
* Detail information about Movie or Tv Series.
* Add to favorite.

## Tech Stacks
* [Retrofit](http://square.github.io/retrofit/) + [OkHttp](http://square.github.io/okhttp/) - API and networking client.
* [Hilt](https://insert-koin.io/) - Dependency injection.
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - A collection of libraries that contains the lifecycle-aware components.
    * [Room](https://developer.android.com/training/data-storage/room) - Local persistence database.
    * [ViewModel](https://developer.android.com/reference/androidx/lifecycle/ViewModel) - UI related data holder, lifecycle aware.
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Observable data holder that notify views when underlying data changes.
    * [Data Binding](https://developer.android.com/topic/libraries/data-binding) - Declarative way to bind data to UI layout.
    * [Navigation component](https://developer.android.com/guide/navigation) - Fragment routing handler
* [Coroutine](https://developer.android.com/kotlin/coroutines) - Concurrency design pattern for asynchronous programming.
* [Flow](https://developer.android.com/kotlin/flow) - Stream of value that returns from suspend function. 
* [Glide](https://github.com/bumptech/glide) - Load image from the url with Internet connection.
* [Shimmer](https://facebook.github.io/shimmer-android/) - Showing loading with an unusual appearance.
* [Swipe Fresh Layout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout) - Refresh the layout by swiping down the screen.

## How to run the Project
Input your [The Movie DB](https://www.themoviedb.org/)'s API key to variable def API_KEY = '"YOUR_API_KEY"' in build.gradle(:app) file.
 

