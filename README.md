# Labo2_SYM

### auteurs Gaetan Bacso, Rafael Garcia et Remy Vuagniaux

### But

Le but de ce laboratoire est de comprendre différents type de transmission. Ainsi que les différents type de données pouvant être envoyées.

### Question 4.1 Traitement des erreurs

Les classes et interfaces SymComManager et CommunicationEventListener utilisées au point 3.1
restent très (et certainement trop) simples pour être utilisables dans une vraie application : que se
passe-t-il si le serveur n’est pas joignable dans l’immédiat ou s’il retourne un code HTTP d’erreur ?

Pour savoir si le serveur est joignable, on vérifie si connexion est possible, pour cela nous avons créer une méthode isConnectedToServer() qui vérifie si la connexion lève une exception, si oui la connexion n'a pas pu se faire.

Pour le code d'erreur, on vérifie si la réponse retourne le code 200 (HTTP_OK) avec le test ```if (responseCode == HttpURLConnection.HTTP_OK)

```, si oui, il n'a pas d'erreur.

On pourrai imaginer que dans une application plus poussée, on test chaque codes d'erreur est que l'on traite l'erreur en fonction.

### Question 4.2 Authentification

Si une authentification par le serveur est requise, peut-on utiliser un protocole asynchrone ? Quelles
seraient les restrictions ? Peut-on utiliser une transmission différée ?

Cela pose un problème important surtout dans le cas du différé, car imaginons qu'un utilisateur souhaitant ce connecter à une page doivent d’abord s’authentifier. Si il y a une connexion l'utilisateur devra attendre que les requêtes soit transmise pour pouvoir accéder à la page donc se qui est contraire au concept de communication différée/asynchrone.

### Question 4.3 Threads concurrents

Lors de l'utilisation de protocoles asynchrones, c'est généralement deux threads différents qui se
préoccupent de la préparation, de l'envoi, de la réception et du traitement des données. Quels
problèmes cela peut-il poser ?

Il faut que ces threads soit coordonnées (concurrence), en d'autres termes, si la threads qui envoie (qui n'est pas bloquée dans l'attente de la réponse) envoie trop de requête, il se peut que le thread de réception en rate quelque une. Ce scénario est envisageable si un seul threads récupère toutes les réponses.

Il faut que le thread qui attend la réponse sache quelle requête il dois récupérer. Donc il faut que les threads aient des moyens de communiquer entre elles et de ce transmettre des connexions.Il faudrait donc donner un id au requête et que cet id soit transmis avec la réponse.

### Question Ecriture différée

Comparer les deux techniques (et éventuellement d'autres que vous pourriez imaginer) et discuter des avantages et inconvénients respectifs.

Connexion par transmission différée:

Si les requête sont lourde, les multiplexé serait une mauvaise idée donc la connexion différée est une meilleure idée.

Si des requêtes passe mais d'autre pas à cause de problème de connexions. Il n'as que celle qui n’ont pas été transmise qui sont gardée dans un buffer.

requêtes plus facile à sérialiser et a compresser

Multiplexage des connexions:

Pratique si les requêtes sont petite mais nombreuses.

On transmets beaucoup de requête en une fois.

Il faut un serveur applicatif qui sépare les requête.


Dans ce dernier cas, comment implémenter le protocole applicatif, quels avantages peut-on
espérer de ce multiplexage, et surtout, comment doit-on planifier les réponses du serveur
lorsque ces dernières s'avèrent nécessaires ?

Comme toute les requêtes sont "fusionnées" en une seule, il faut un moyen de les différencier entre elles. Donc il faudrait un champs ID que le serveur applicatif puisse reconnaître pour les traiter correctement.

Ce cas est valable si l'on veut envoyer toutes les requête à un seul serveur. Mais si on imagine que la requête multiplexée est envoyé à un serveur relais qui redistribuera les requêtes aux bons serveurs, il faut que dans le corps de la requête multiplexée il y ai les entête de chaque requête à envoyer.

### 4.5 Transmission d’objets

Quel inconvénient y a-t-il à utiliser une infrastructure de type REST/JSON n'offrant aucun
service de validation (DTD, XML-schéma, WSDL) par rapport à une infrastructure comme SOAP
offrant ces possibilités ? Est-ce qu’il y a en revanche des avantages que vous pouvez citer ?

Vérifier la validité du contenu d'une requête devient plus complexe car il n'a pas de mécanisme de vérification déjà implémenté. Donc on doit vérifier la structure des objets "manuellement".

L’utilisation d’un mécanisme comme Protocol Buffers 9 est-elle compatible avec une
architecture basée sur HTTP ?

Oui, Protocol Buffers 9 est compatible avec une architecture basée sur HTTP.

Veuillez discuter des éventuelles avantages ou limitations par rapport à un protocole basé sur JSON ou XML ?

JSON et XML sont "human readable". Ce que ProtoBuf n'est pas censé être fait pour.

Protocol Buffers est plus pratique pour modéliser des structure de données.

Protocol Buffers offre une vérification de la structure de données. Qui est très polivalante, par exemple, des balises comme required, optional, et repeated sont disponible.

Une application serveur traitant du Json peut être écrit en javascript.

Protocol Buffers gère plus facilement la "backward compatibility"

Protobuf un processing des données plus rapide

Par rapport à l’API GraphQL mise à disposition pour ce laboratoire. Avez-vous constaté des
points qui pourraient être améliorés pour une utilisation mobile ? Veuillez en discuter, vous
pouvez élargir votre réflexion à une problématique plus large que la manipulation effectuée.

L'api devarit fournir plus de methode pour permettre une plus grande agilité dans la façon de l'utiliser. Il y a aussi un problème majeur. Si le nom d'un champ change il faudra modifier la requête alors que si il y a des methode de type get il ne faudrait pas forcément modifier le code.

Lors d'une utilisation mobile il serait bien de pouvoir avoir les mises à jour qui aurait été effectuée dans la BDD grâce à une reqête pour pouvoir mettre a jour l'interface. Le problème est qu'il faudrait faire des requête periodiquement pour être sur de ne rien louper.

### Question 4.6 Transmission compressée

Quel gain peut-on constater en moyenne sur des fichiers texte (xml et json sont aussi du texte) en
utilisant de la compression du point 3.4 ? Vous comparerez plusieurs tailles et types de contenu.

Pour mesurer la compression, on regarde la taille de la données avant compression puis dans le header de la requête, un champs content-length nous dit quelle est la taille de notre données compressée.

Nous envoyons un lorem de :
5 paragraphes 475 mots et 3339 caractères.

Voici le résultat de notre compression:

|compression|1 lorem|5 lorem|10 lorem|
|--------|-------|---------|----------|
|non compressé|3347|16736|33471|
|compressé|1781|1922|2066|

### Conclusion

Ce laboratoire nous a permis de comprendre les différents type de transmission. Ainsi que les différents type de données pouvant être envoyées.
