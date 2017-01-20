# MovieCenter
Demo for usage the TMDB API

#Architecture
The app architecture approach base on the MVC model:

*Model - An `Content Provider` layer to hold the data and enable offline work.

*View - the Ui layer.

*Controller - `MoviesController` singelton to communicate between the two layers.

#Threads
The network work run in `IntenetService`. The results sent with `ResultReceiver` implementaion by the Controller.

#Improvments suggestion:
*Implements the network layer with `Retrofit`.

*Implement filtered queryies.

*Expand the data columns.

*Use 2 tables of SQLite, one for the basic data, on for additional imformation for a movie.
