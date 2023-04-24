**Basic Details About Monolithic and Microservice Application**


![500](https://user-images.githubusercontent.com/42623098/233902682-ff72c790-eacc-4644-9e21-b6e711c47178.jpg)

![501](https://user-images.githubusercontent.com/42623098/233903183-89c46e07-55a6-442b-8a98-cfb58f885134.jpg)

![1](https://user-images.githubusercontent.com/42623098/233903261-86becc5e-5f5b-47cd-aa29-1cfa2db089e7.jpg)

![2](https://user-images.githubusercontent.com/42623098/233903273-a28d67bb-7f16-4b89-8d3b-fe346b224105.jpg)

![3](https://user-images.githubusercontent.com/42623098/233903305-b97d8de7-3f7e-499b-b96a-574610bc1ea2.jpg)

![4](https://user-images.githubusercontent.com/42623098/233903330-31ad0ed4-f4cf-4d81-99f2-f530e5f6293f.jpg)

![5](https://user-images.githubusercontent.com/42623098/233903355-5e365c1c-a13e-4853-92fa-a9924325b7c8.jpg)

![6](https://user-images.githubusercontent.com/42623098/233903398-335b0a55-76f3-40f5-a2f3-e8c823c36dad.jpg)

![7](https://user-images.githubusercontent.com/42623098/233903415-9a8c4b6b-8d21-42b5-b5b2-3dc61ad729a3.jpg)

![8](https://user-images.githubusercontent.com/42623098/233903433-7cac3ad2-e3c2-4cd5-ad54-68c134a2a385.jpg)

![9](https://user-images.githubusercontent.com/42623098/233903453-e0a88185-50cc-4508-a576-f11b8c44c2e9.jpg)



In enterprise application, We may have 100 of 1000 of microservices, We may encounter lots of challenges while buiding microservices.
**Need of API Gateway**
**Challenge:** Client is calling multiple microservices to get the response from different Rest API. To call API of different microservices client need to remember the host and port number of the microservice that it want to comminicate. This is not good idea that client have to remember  the host name and port number for all the microservices.

**Solution** is API Gateway. We can introduce one central componenet between client and backend microservices.client will send request to this central component and this component will route the request to appropriate microservices. 



**Need of Config Server**
**Challenge** In real time project we may have lot of microservices and each microservice will have its own configuration file to maintain its configuration.If we have requirement to change configuration file of multiple microservices, then we have to go into each and every microservice to change its config file. This is not practice to go into each and every microservices and change it.

**Solution** is Config Server.   We can have a central place where we can keep all the configuration file of all these micoservices. And whenever there is a requirement to change configuarion we can go ahead and change in central cofig place.
We can use Git repository to keep all the microservices configuration file. If requirement to change configuration we can change into Git repository.
Ro externalise the configuration of microservices, We can use Config Server Pattern.

**Need of Circuit Braker**
**Challenge** Consider client-> API Gateway -> Microservice M1 -> Microservice M2. But due to some reason Microservice M2 is down. then M1 won't get response from M2. M1 will return error response to API Gateway and API Gatway farward the response to client.
If M2 is down then M1 will continuous call M2  so this is not a good practice. 

**Solution** is Circuit Braker. There should be a way where Microservice M1 have limit the number of calls to M2 whenever M2 is down or not available. This challenge we can implement using circuit braker pattern.
VCircuit braker pattern help Microservice M1 to return some default response back to API Gateway. And API 

**Need of Service Registery & Discovery**

**Challenge** We have different microservices. And if we have requirement to scale microservice 1.(say we have instance I1, I2 of Microservice M1). Due to some reason instance I2 of Microservice M1 is down and Microservice M2 is also down. We need mechanism to keep track of all these microservices and its instances(which are up and which are down)

**Solution** is Service Registery Discovery. Service Registery maitains all the host name and port number of all the microservices and its instances.
API Gatway will get the host name and port number of particular microservice from Service Registery.

**Need of** 
**Challenge** lets suppose client -> API Gateway ->Microservice M1 ->Microservice M2. This is complete call hirarchy. We need log information of complete call strat to end.
**Solution**  is distributed tracing. This pattern helps us to indentify thye complete call hirarchy from start to end.

**Few Other Challenges**
**Implementing load balancing**

**Impleting centerlized Security in API Gateway**

Spring Cloud provides tools to implement all these commenly used patterns in microservices.

![M1](https://user-images.githubusercontent.com/42623098/233918293-e12e1fcc-be62-4f6c-afe0-d9d5cf972f17.jpg)

![M2](https://user-images.githubusercontent.com/42623098/233918783-69731c95-90d7-4851-bacb-13cd741fbede.jpg)



![M3](https://user-images.githubusercontent.com/42623098/233918606-3eda6fb0-f19b-47c8-993b-db5fa60688a3.jpg)



