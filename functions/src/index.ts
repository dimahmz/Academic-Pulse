/* eslint-disable linebreak-style */
/* eslint-disable max-len */
// The Cloud Functions for Firebase SDK to create Cloud Functions and triggers.
import * as v2 from "firebase-functions/v2";
import {getFirestore} from "firebase-admin/firestore";
// The Firebase Admin SDK to access Firestore.
import {initializeApp} from "firebase-admin/app";
import {
  onDocumentCreated,
  onDocumentDeleted,
} from "firebase-functions/v2/firestore";

initializeApp();

// Take the text parameter passed to this HTTP endpoint and insert it into
// Firestore under the path /messages/:documentId/original
export const addmessage = v2.https.onRequest(async (req, res) => {
  // Grab the text parameter.
  const original: any = req.query.text;
  // Push the new message into Firestore using the Firebase Admin SDK.
  const writeResult = await getFirestore()
    .collection("messages")
    .add({original: original});

  // Send back a message that we've successfully written the message
  res.json({result: `Message with ID: ${writeResult.id} is added.`});
});