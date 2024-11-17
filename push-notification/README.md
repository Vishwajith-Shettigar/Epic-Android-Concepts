# Push notifications

## Firebase Setup
- Go to the Firebase Console.
- Create a new project or use an existing one.
- Add this Android app by registering  package name(com.example.pushnotification) and download the google-services.json file.
- Paste  google-services.json in push-notification directory( push-notification/google-services.json)
- Enable Firebase Cloud Messaging under the Firebase project settings.

## Server setup
- Create your service account key in your project settings under service tab.
- `const serviceAccount = require("./serviceAccountKey.json")`



