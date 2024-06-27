/* eslint-disable linebreak-style */
/* eslint-disable max-len */

import * as v2 from "firebase-functions/v2";
import { getFirestore } from "firebase-admin/firestore";
import { initializeApp } from "firebase-admin/app";
import {
  onDocumentCreated,
  onDocumentDeleted,
} from "firebase-functions/v2/firestore";
import * as dotenv from "dotenv";
import axios from "axios";
import { getAppSettings } from "./settings";

// Load environment variables from .env file
dotenv.config();

initializeApp();

// text function
export const textapi = v2.https.onRequest(async (req, res) => {
  res.json({ message: `hello world` });
});

// delete the publication's ID from the user document when its deleted
export const onDeletePublication = onDocumentDeleted(
  "publication/{docId}",
  async (event) => {
    const snapshot: any = event.data;
    if (!snapshot) {
      console.log("No data associated with the event");
      return;
    }

    const data = snapshot.data();
    const authors: string[] = data.authors;

    for (const author of authors) {
      const userRef = getFirestore().collection("user").doc(author);

      const userDoc = await userRef.get();

      const user = userDoc.data();

      if (!user) return;

      const filteredPublications = user.publications.filter(
        (pubId: string) => pubId !== event.params.docId
      );

      await userRef.update({ publications: filteredPublications });
    }
  }
);

// Automatically verify new publication
export const onCreatePublication = onDocumentCreated(
  "publication/{docId}",
  async (event) => {
    const snapshot: any = event.data;
    if (!snapshot) {
      return;
    }

    const pubId: string = event.params.docId;
    // missing api URL
    if (!process.env.VERIFY_PUBLICATION_API) return;

    // get the app settings
    const setting = await getAppSettings();
    if (!setting) return;

    //automatic verification is disabled
    if (!setting.autoVerifyNewPublications) return;
    
    axios
      .get(`${process.env.VERIFY_PUBLICATION_API}/${pubId}`)
      .then()
      .catch((e) => console.error(e.message));
  }
);
