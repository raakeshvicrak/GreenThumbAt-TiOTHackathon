'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, response) => {
 response.send("Hello from Firebase!");
});

exports.readingviolation = functions.database.ref('/{farmname}/sensoreadings/{sensorname}').onWrite(event => {

  const plantownerId = event.params.farmname;
  const sensorname = event.params.sensorname;

  console.log('Plant Name: ', plantownerId, ' Sensor ', sensorname );

  const token = 'cOgg08-eJuM:APA91bGC66OZzQPvc0y8NPpuR48TrCc5BgvYPQT1E13xKQ1-kutbJu-S6QEYrrcMx4WUO5FJ8f6oD7s3HTf_FixDdyEgMofI9Hn6mMdEHMBXwUkA3XQFWW2cs-0leqalZpsOH6xq1Czq';

  console.log('token: ', token);
  const payload = {
    notification: {
      title: `${plantownerId} needs attention!!`,
      body: `Your plant ${plantownerId} needs attention!! ${sensorname} has breached threshold `,
  }
  };

  admin.messaging().sendToDevice(token, payload).then(function(response) {
        console.log("Successfully sent message:", response);
      })
      .catch(function(error) {
        console.log("Error sending message:", error);
  });


});
