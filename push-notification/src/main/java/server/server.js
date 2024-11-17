const express = require("express");
const bodyParser = require("body-parser");
const admin = require("firebase-admin");

// Please create your service account key in your project settings under service tab.
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

const app = express();

const firestore = admin.firestore();

app.use(bodyParser.json());

app.post("/send-notification", async (req, res) => {
    // Assumimg we have got userIds, title, and body to send notification.
    const {
        userIds,
        title,
        body
    } = req.body;

    try {
        const allTokens = [];

        for (const userId of userIds) {

        console.log(userId);
            const userDoc = await firestore.collection("users").doc(userId).get();
            console.log(userDoc.data());
            if (userDoc.exists && userDoc.data().fcmToken) {
                const token = userDoc.data().fcmToken;
                allTokens.push(token);
            }
        }

        // create a message, that includes token, title, and body.
        const message = {
            data: {
                title: title,
                body: body,
            },
            token: "",
        };

        const notificationPromises = allTokens.map(async (token) => {
            message.token = token
         await admin.messaging().send(message);
        })

        await Promise.all(notificationPromises);

        // Firebase Admin SDK sends message to specific token.
        res.status(200).send("Notification sent");
    } catch (error) {
        console.error("Error sending notification:", error);
        res.status(500).send(error);
    }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
