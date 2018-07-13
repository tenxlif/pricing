# Design

## UseCase:
The main objective is to implement an interface where an admin can create and update plans for a specific country.  At the same time, the admin should also be able to view the plan for a specific customer.


## Entities:

The main entity modeled is called `PricePlan`. It is an immutable representation of resource to store details for a plan in a country. It works with another entity `Customer`. These entities associate to map plan's price to a customer.

## Interface and Resources:
The interface is exposed as a HTTP REST interaction, where a client can issue requests to create new plans, modify existing plans or view pricing details of any customer. Here are the reosouces 

   * `/plans` : The main resource used to POST new pricing plans. It accepts JSON representation of the `PricePlan` object and if all input is valid, it creates a new plan. This plan is returned as part of a HTTP 302 Redirect from the original POST request.
   * `/customers/{id}/plan`: You can query this resource to GET plan details for a customer.  It returns a `PricePlan` object. This could be optionally extended to get older plans for a customer with a timestamp parameter. (Older plan search is yet to be implemented)
   * `/plans\{id}`: To GET details of a specific price Plan. Returns a `PricePlan` object if successful. 
   * `country/{cid}/plans/{name}` : To GET or PUT details of a plan by country. The Content body is JSON representation of `PricePlan` object. Eg. `/country/US/plans/1S`. On applying the PUT method, the older plan is deactivated and new plan replaces it.
   
## Representation:

   1 PricePlan object is represented as a json as follows (both ways: when sending it as part of request Body or being part of response Body)
   ```
    {
    "id": "9f1ddff6-88fd-45fc-98f0-dc4efbc10673",
    "planName": "2S",
    "country2Digit": "DE",
    "pricePennies": 799,
    "currency": "EUR",
    "effectiveStartTs": 1531462708
}
   ```
   
## High Level Design

Main design decisions were driven by discussions after clarifying requirements on complexity of this change. Complexity coming from pricing promotions, partnership deals are excluded. So is billing complexities like pro-rata billing, notifying the users about the upcoming changes, multiple plan per customer etc. These are all important aspects of the pricing sytem and its satellite systems which should be dealt with more details in a real world use case.

### Price stays with Plan details and not with Customer Record
As a result, the design choices were limited to (A) whether to represent price of a plan per customer OR (B) just reference the plan name in the customer profile and then update plan metadata in another record where details of that plan are stored. I chose option B as this will make price changes uniform and swift and updates will be singular. Option A would have required me to update each customer's row for that country and be ripe with problems due to inconsistent states.

### Price changes can be scheduled
One can also schedule the price changes in future or back-date it in the past. As a result, one can have many plan records for a single country and PlanType, depending on time. This makes sense also for other reasons, as it is probably better to keep every record of price change for historical analysis and compliance reasons. Each pricePlan is keyed by PlanName and country. Once narrowed down by those keys, many price plans exist for that combination. In the current local implementation, a Stack is used to implement plan lineage. In a more traditional data store, this could be implemented using another field like a LastUpdatedTs to keep chronological order of updates. 

The other inference is that the most recently updated plan is the active plan. (need further discussion on this)

### Ordering price records by time of update
The key point to note is that the order is the order in which updates were "received" by the system and not necessarily the order of time when a plan was active. This helps in searches also, as realistically, admins and clients will be only interested in last few updates even though the updates were for different time windows. 

### Entire Price Record is transferred across the HTTP interface - both ways
This is another key design decision as it keeps things simple for the client. (very valuable). This way, for example: the client can pick PricePlan as a response from one request and use that right away to make another change on that Plan. There is no need to extract fields, construct myriad objects for different requests. It makes interaction more simple for the clients to reason about as they handle the same structure and semantics of the object across many messages.

There is a drawback of that approach in that in addition to prices, other aspects of the plan can also be modified inadvertently/maliciously by the client. But, if this begins to be an issue, it is much better controlled on the server side in the PricingManager implementations to restrict changes as opposed to change the resource representation exchanged with the client.

## Scaling:
Realistically, the amount of plan changes are not going to be in the thousands per month. Also, the service does not need to scale to number of users based on the design choice above. The number of operations on this service could be a few per day realisitically and the service scales for that.

The state of the pricing datasets (while mocked in this demo) will need to be managed for a distributed setup with eventual consistency patterns with rest of the dependencies or integrations. The state comprise of 3 tables: Customer table that will capture customer, country and their choice of plan. The PricePlan table will capture all details of a plan by country and plan name as search keys in that table. Though a surrogate Primary Key will be needed to create unique records with same country and plan name. 

The state will be modified in a distributed env thereby creating locks for update when a plan is getting updated. This will avoid concurrent updates, if they so happen. The only times a lock needs to be used are:

1. Modifying details of an existing plan
2. Creating a new plan
3. Modifying a plan for a customer


### Deployment:
Shared state support will also enable running this service on many server nodes thereby increasing the availability of the service. 

In the beginning, 3 nodes should be sufficient to serve this interface. 1 DB node with addl backup nodes will be needed for shared state.


## Additional Internal interfaces:
The request handling is layered to keep out HTTP semantics from actual state change impl on the server side. At the same time, models and processors are abstracted as well with interfaces so as to be able to slide in one impl and take out another. This also decouples the way STATE is stored Vs. how it is used to serve requests.

Local interfaces were created to separate models from processors from controllers. One key element of the design there is to follow marker interfaces in the models but we also define exceptions throwin by model POJOs, in those interface. This keeps the entire interface intact and folks dont need to figure out which exceptions to handle between different layers.


## TODO:
Many aspects are not complete. (including in the implemenation)
1. Authentication and security needs to be handled.
2. Error handling needs to be made more robust.
3. Usecase related checks needs to be added.
4. Retroactive subscription management needs to be exposed. 
5. ...






