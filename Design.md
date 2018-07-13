# Design

## Entities:

The main entity modeled is called PricePlan. It is an immutable representation of resource for indicating price for a plan in a country. It works with another entity Customer. These entities associate to map plan's price for a customer.

## Interface
The interface is exposed as a HTTP REST interaction, where a client can issue requests to create new plans, modify existing plans or view pricing details of any customer. Here are the reosouces 

   * `\plans` : The main resource used to POST new pricing plans. It accepts JSON representation of the `PricePlan` object and if all input is valid, it creates a new plan. 
   * `\customers/{id}/plan`: You can query this resource to GET plan details for a customer.  It returns a `PricePlan` object. This could be optionally extended to get older plans for a customer with a timestamp parameter. (TO BE IMPLEMENTED)
   * `\plans\{id}`: To GET details of a specific price Plan. Returns a `PricePlan` object if successful. 
   * `country/{cid}/plans/{name}` : To GET or PUT details of a plan by country. The Content body is JSON representation of `PricePlan` object. Eg. `/country/US/plans/1S`. On applying the PUT method, the older plan is deactivated and new plan replaces it.
   




