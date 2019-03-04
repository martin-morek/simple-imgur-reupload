# Simple image Re-uploader
Simple Play application downloading images from provided urls and uploading them to imgur.com

## Description
This small project was a job interview assignment. It lacks a proper failure response back to the client but it was specified this way. Failure is propagated only as a log entry.  To every HTTP POST request service replies with success response with generated unique jobId, in case of failure, the jobId is used in the log entry. For each request is created new AKKA actor, what enable handle each reupload asynchronously. 

## Request example
Method: `POST`

Url: `localhost:9000/v1/images/upload`

Body: 
`{
"urls": [
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg",
        "https://example.com/image3.jpg"
    ]
}`

## Response example
`{
     "jobId": "94e13163-7015-48d6-8933-d3dafeef553d"
 }`

## Configure project
In `conf/application.conf` set `imgur.clientId` parameter to correct value. 


## Run project
`sbt run`