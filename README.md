# Pricing

# Usecase
The ability to manage pricing for Netflix across various countries and plans. This REST service supports that feature.

## How to Build
1. Go to root directory of the repo
2. Run `./mvnw clean install`

## How to run
1. Copy the jar `netflix-challenge-0.1.0.jar` from `repo_root/target` to whereever you would like.
2. Run the pricing server as 

`java -jar target/netflix-challenge-0.1.0.jar`

## Validation:
1. See Design.md for resource documentation. Lets say, you execute a HTTP GET on `/country/US/plans/2S`

## Understanding Response

If the request is valid the response will be a JSON representation of a price plan resource.  Sample Response:
```
{
    "id": "9f1ddff6-88fd-45fc-98f0-dc4efbc10673",
    "planName": "2S",
    "country2Digit": "US",
    "pricePennies": 499,
    "currency": "USD",
    "effectiveStartTs": 1531460029
}
```

---

