### This Application features the REST APIS to Filter NFTs using traits from a Collection

### Tech Stack : Java + SpringBoot + CosmosDB 

### Local Setup

```java
This Repo comes with a Dockerfile to build and run a container 
Install Docker (https://docs.docker.com/engine/install/)
change the cosmos keys in application.properties file (current is 30day free trail)
docker build -t metaboy-svc  .  (build the image)
docker run -p 8080:8080 metaboy-svc (start container)
heathcheck http://localhost:8080/metaboy-svc/healthCheck
```

### Free Hosting

App is also deployed to onrender cloud platform under free tier  
**Note** : as its free tier onrender will shutdown the container when app is inactive for sometime  
and restarts when a new request comes, so if you are hitting it after it is inactive your first few  
requests will timeout

host : [https://metaboy.onrender.com](https://metaboy.onrender.com)  
[https://metaboy.onrender.com/metaboy-svc/healthCheck](https://metaboy.onrender.com/metaboy-svc/healthCheck)

**APIs**

```java
GET /metaboy-svc/healthCheck  (check if app is running)
GET /metaboy-svc/collection/36fab6f7-1e51-49d9-a0be-39343abafd0f (get collection details and available filters)
POST /metaboy-svc/filterNfts (get filtered nfts)
```

### ENDPOINT

```java
POST {host}/metaboy-svc/filterNfts
```

**PAYLOAD**

<table data-layout="default" data-local-id="5a6563b1-5c74-4aa0-9f16-0c37364be92d" class="confluenceTable"><colgroup><col style="width: 170.0px;"><col style="width: 170.0px;"><col style="width: 170.0px;"><col style="width: 170.0px;"></colgroup><tbody><tr><th class="confluenceTh"><p><strong>key</strong></p></th><th class="confluenceTh"><p><strong>value</strong></p></th><th class="confluenceTh"><p></p></th><th class="confluenceTh"><p><strong>notes</strong></p></th></tr><tr><td class="confluenceTd"><p><code>collectionId</code></p></td><td class="confluenceTd"><p>collectionId of the nfts we are searching for</p></td><td class="confluenceTd"><p>mandatory</p></td><td class="confluenceTd"><p>user can select a collection to search nfts from</p></td></tr><tr><td class="confluenceTd"><p><code>filters</code></p></td><td class="confluenceTd"><p>List of Json objects containing filter information</p></td><td class="confluenceTd"><p>optional</p></td><td class="confluenceTd"><p>user can use multiple filter conditions</p></td></tr><tr><td class="confluenceTd"><p><code>filters</code>.<code>filterKey</code></p></td><td class="confluenceTd"><p>Trait type</p></td><td class="confluenceTd"><p>mandatory</p></td><td class="confluenceTd"><p></p></td></tr><tr><td class="confluenceTd"><p><code>filters</code>.<code>filterValues</code></p></td><td class="confluenceTd"><p>List of Trait values in that trait type</p></td><td class="confluenceTd"><p>mandatory</p></td><td class="confluenceTd"><p>user can select multiple trait values for a particular trait type</p></td></tr><tr><td class="confluenceTd"><p><code>filters</code>.<code>exclude</code></p></td><td class="confluenceTd"><p>whether to exclude the nfts with the filter condition</p></td><td class="confluenceTd"><p>optional (default false)</p></td><td class="confluenceTd"><p>user can filter out of a trait<br>(ex: gives ability to check all non school boys)</p></td></tr><tr><td class="confluenceTd"><p><code>fetchGmePrices</code></p></td><td class="confluenceTd"><p>whether to get live prices from gme api</p></td><td class="confluenceTd"><p>optional (default false)</p></td><td class="confluenceTd"><p>an api call is made to gme server to get prices</p></td></tr><tr><td class="confluenceTd"><p><code>sortBy</code></p></td><td class="confluenceTd"><p>which value to use the sort the results</p></td><td class="confluenceTd"><p>optional(default no sorting)</p></td><td class="confluenceTd"><p>only supports rank for now as we don’t have all the prices stored in the</p></td></tr><tr><td class="confluenceTd"><p><code>sortOrder</code></p></td><td class="confluenceTd"><p>sort high to low or low to high</p></td><td class="confluenceTd"><p>optional(asc if sortBy is passed)</p></td><td class="confluenceTd"><p>asc or desc</p></td></tr><tr><td class="confluenceTd"><p><code>pageSize</code></p></td><td class="confluenceTd"><p>number of max results to fetch in one call</p></td><td class="confluenceTd"><p>optional(default 48)</p></td><td class="confluenceTd"><p>currently max is also set to 48</p></td></tr><tr><td class="confluenceTd"><p><code>continuationToken</code></p></td><td class="confluenceTd"><p>pagination is implemented through <code>continuationToken</code><br>pass the <code>continuationToken</code> given from the previous response to next request to continue the results</p></td><td class="confluenceTd"><p>optional( default is null)</p></td><td class="confluenceTd"><p>used for pagination</p></td></tr></tbody></table>

### SAMPLE REQUESTS

**PAYLOAD** (request to get my demon brother)

```java
{
    "pageSize": 48,
    "sortBy": "rank",
    "sortOrder": "asc",
    "collectionId": "36fab6f7-1e51-49d9-a0be-39343abafd0f",
    "fetchGmePrices": false,
    "filters": [
        {
            "filterKey": "Body",
            "filterValues": [
                "Demon"
            ]
        },
        {
            "filterKey": "Hat",
            "filterValues": [
                "Dark Hoodie"
            ]
        },
        {
            "filterKey": "Background",
            "filterValues": [
                "Invasion"
            ]
        },
        {
            "filterKey": "Weapon",
            "filterValues": [
                "Zombie Hands",
                "Katana"
            ]
        }
    ]
}
```

**RESPONE**

```java
{
    "results": [
        {
            "nftId": "bd15ee96-b0f6-4776-981f-28f26f55bab7",
            "collectionId": "36fab6f7-1e51-49d9-a0be-39343abafd0f",
            "name": "MetaBoy #9341",
            "metadataUri": "ipfs://QmQvnwBNKGR4n2VxeM69xSirhkCGvdq5PE3yR1kj4AQLsH",
            "mediaUri": "ipfs://QmNngiChSt9FpfWDnMFzHRGtFwvrHCioZV6t8dw9aX8jvV",
            "gmeUrl": "https://nft.gamestop.com/token/0x1d006a27bd82e10f9194d30158d91201e9930420/0x26766bfe0e7f01c812c5e425baece875deb7c77db068ef80bc08d75d15f84f18",
            "rank": 382,
            "price": 0.0,
            "properties": {
                "Background": "Invasion",
                "Hat": "Dark Hoodie",
                "Face": "Cyclops ",
                "Body": "Demon",
                "Weapon": "Zombie Hands"
            }
        },
        {
            "nftId": "58873c32-8f54-42f6-b02a-b0d33e368a21",
            "collectionId": "36fab6f7-1e51-49d9-a0be-39343abafd0f",
            "name": "MetaBoy #2472",
            "metadataUri": "ipfs://QmThRszSSpLG4XgVc9fekbjVywX81XS92YQGx9owgMWKLH",
            "mediaUri": "ipfs://QmSS6TPLgjZQPhnKs6fVwQwezvdMYpJJaYCpBmgUhXpDSk",
            "gmeUrl": "https://nft.gamestop.com/token/0x1d006a27bd82e10f9194d30158d91201e9930420/0x4f9d200b51378c076fcff428d6706008c81409783167bd3a2d0310280cee2e8e",
            "rank": 893,
            "price": 0.0,
            "properties": {
                "Background": "Invasion",
                "Hat": "Dark Hoodie",
                "Face": "Glasses ",
                "Body": "Demon",
                "Weapon": "Katana"
            }
        }
    ],
    "continuationToken": null
}
```

**PAYLOAD** (get all non school boys and girls with demon face and `Heaven` background in rank order)

```java
{
    "pageSize": 48,
    "sortBy": "rank",
    "sortOrder": "asc",
    "collectionId": "36fab6f7-1e51-49d9-a0be-39343abafd0f",
    "fetchGmePrices": true,
    "filters": [
        {
            "filterKey": "Body",
            "filterValues": [
                "Schoolgirl",
                "Schoolboy"
            ],
            "exclude": true
        },
        {
            "filterKey": "Face",
            "filterValues": [
                "Demon"
            ]
        },
        {
            "filterKey": "Background",
            "filterValues": [
                "Heaven"
            ]
        }
    ]
}
```

**RESPONSE**

```java
{
    "results": [
        {
            "nftId": "a3359d18-0998-4ad3-bbd8-f6bb8e0893b2",
            "collectionId": "36fab6f7-1e51-49d9-a0be-39343abafd0f",
            "name": "MetaBoy #2972",
            "metadataUri": "ipfs://QmTKGDeCc9T6FUxRA8cr4VE9SWtcbPoCfqDSysJzxDD7Pd",
            "mediaUri": "ipfs://QmUsHUcY4nu8uaF1iqFFbm3jE4nj59GgbhSWs7WiNmDvLC",
            "gmeUrl": "https://nft.gamestop.com/token/0x1d006a27bd82e10f9194d30158d91201e9930420/0x49ef6c660d9eb2c06a6ecedad8f0a047548ca586a3ab08d73a695ce2d5100cb8",
            "rank": 1162,
            "price": 1.0,
            "properties": {
                "Background": "Heaven",
                "Back": "Sword Backpack",
                "Hat": "Fedora",
                "Face": "Demon",
                "Body": "Cowboy",
                "Weapon": "Axe and Shield"
            }
        },
        {
            "nftId": "5fc619d4-a07b-41f4-82ea-e4e487cf47ba",
            "collectionId": "36fab6f7-1e51-49d9-a0be-39343abafd0f",
            "name": "MetaBoy #5474",
            "metadataUri": "ipfs://QmTc1eeAWmkcyWNtMLsYMPKyRkVhKnFqMVCxtazZf2xMAx",
            "mediaUri": "ipfs://QmcFQCYzdaWcAbyfbDZvRqCceCr82bmfKr2d9mAUE6V13R",
            "gmeUrl": "https://nft.gamestop.com/token/0x1d006a27bd82e10f9194d30158d91201e9930420/0x4e39d2f12ca44937fc0947506c6e343dd2bb161d98c740a8fea86ac1fcde6bf9",
            "rank": 2596,
            "price": 0.15,
            "properties": {
                "Background": "Heaven",
                "Hat": "Cowboy Hat",
                "Neck": "Black Scarf",
                "Face": "Demon",
                "Body": "Lumberjack",
                "Weapon": "Bomb"
            }
        }
    ],
    "continuationToken": null
}
```
