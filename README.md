# NasaNearEarthObjectsVisualizer

This project fetches data about Near Earth Objects (Asteroids) from the public Nasa api (https://api.nasa.gov/) and 
offers functionality to display either the largest or the closest Near Earth Object in a specified time period.The api
that is used in this project is the "Asteroids - NeoWs Api".

The project consists of a Frontend written in React and TypeScript and a Backend written in Kotlin.

## Backend

The backend caches all data that was fetched from the Nasa api so that the next calls do not need
to fetch data again if they cover the same range.

The requests that are sent to the Nasa api are handled by a thread pool, so that requests can be sent simultaneously.
With some experimenting, the thread pool was chosen to be of size 10, so 10 requests are sent and handled at the same
time. Increasing this number lead to errors from the Nasa api occasionally, so the size was chosen to be 10 and not
higher.

The Asteroids - NeoWs Api from Nasa only allows requests for a time frame of up to 8 days. So for periods that span
more than 8 days, the period is split up into intervals of maximally 8 days, and data is then fetched for each of these 
intervals separately and then stored in the cache.

### Todos and improvements to be done

- The in memory caching implemented now is very rudimentary. Using some mainstream in memory database like Redis
would be really nice
- If this was a real product, it should be tested more thoroughly
- Error handling and fault recovery (there is no retrying now, so receiving a server error from $nasa crashes the
  application)
- Allow configuring the project, i.e., which port to start on, in a configuration file or by environment variables


## Frontend

The frontend is a quite simple React app. The website is split into 2 parts, one for calculating the largest Near Earth 
Object in a specified time frame, one for calculating the closest Near Earth Object in a given time frame. Both parts
are using the same React component.

The period can be any period, though it isn't recommended to be longer than a year, since it takes
quite a long time to load all the data from the api.

### Todos and improvements to be done

- The website design, look and feel is very basic and could be improved and made to look a lot better :)
- There is no testing at all at the moment, unit tests for functions and end-to-end tests with for example Cypress
should be added
- Error handling and alerting users about errors is missing
- Allow configuring the project, i.e., which port to start on, in a configuration file or by environment variables


## How to run the project

First clone the project from github. Then run the following steps in order:

### Start the backend service

- Go to the `/backend` folder in the project directory with the terminal and run `./gradlew run`. This will start up the
  server on port 8000
- Go to the `/frontend` folder in the project directory with the terminal and first install the dependencies with
  `npm install` and then run the frontend by running `npm run start`. This will start the
frontend on port 3000.
- Open `http://localhost:3000/` in a browser and start using the Nasa Near Earth Objects Visualizer