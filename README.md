# find-ideal-weather-any-place
A REST API developed using Spring Boot. It consumes weather forecast from https://api.openweathermap.org/ and provides the hours in which your selected place is forecasted to have your described weather. the place is selected by providing the name of the city. 
The weather is described with three parameters:
"temperature" with one of the three values "hot, cold, mild"
"wind" with one of the three values "calm, windy, stormy"
"sky" with one of the three values "clear, cloudy, overcast"

I have pushed the project with its maven wrapper so building and running should be fairly easy.

How to make an API call

http://localhost:8080/api/attr?city={city}&temperature={temperature}&wind={wind}&sky={sky}

Parameters
city(required):		      city names
temperature(optional):	Your ideal temperature.     	hot, cold, mild
wind(optional):   	    Your ideal wind situation.  	calm, windy, stormy
sky(optional): 		      Your ideal sky. 			        clear, cloudy, overcast

example:
http://localhost:8080/api/attr?city=London&temperature=mild&wind=calm&sky=clear


Security: Basic OAuth
user: thortful
pass: thortful
