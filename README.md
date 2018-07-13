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
1. Based on above example:
   * Pricing server will startup with some demo data to play with. (sample here)

2. Make a test request by running this from repo_root in a terminal shell.
   `./scripts/get_plan.sh US 1S
	
   This script will get the plan details for *US* country for plan named as *1S*. You will see the results in client response.

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

