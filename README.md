## Background
 
 The genesis for this adventure was the following exchange:
* Me: Any particular reason we are passing in the system auth token on every function of our retrofit client? A better design would be to have a singe client per route and add interceptor for adding the auth token.
* Other: We should only have one instance of the OkHttpClient per JVM.
* Me: Why?
* Other: Having multiple leads to more resource overhead
* Me: Hmm...

## Adventure Setup
- Multi module gradle project containing 2 Ktor applications. 
- Both applications call out to 2 external APIs to generate a salutation and some random advice for the caller.
- Client calls are made using Retrofit.
- One application (blue-pill) uses per client OkHttpClient the other uses a shared OkHttpClient (red-pill).
- JMeter test to hammer both services to see if there are any material differences.
- JMX to monitor telemetry

## Running service
TODO - Add run config
 