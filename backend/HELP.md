Démarrez l’application. Que se passe-t-il ?
mvn spring-boot:run

***************************
APPLICATION FAILED TO START
***************************

Description:

Field sensor in fr.cnalps.squaregames.HeartbeatController required a bean of type 'fr.cnalps.squaregames.HeartbeatSensor' that could not be found.

The injection point has the following annotations:
	- @org.springframework.beans.factory.annotation.Autowired(required=true)


Action:

Consider defining a bean of type 'fr.cnalps.squaregames.HeartbeatSensor' in your configuration.