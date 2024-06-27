import { getFirestore } from "firebase-admin/firestore";

export const getAppSettings = async () => {
  try {
    const settingsRef = getFirestore().collection("app").doc("settings");

    const settingDoc = await settingsRef.get();

    const settings = settingDoc.data();

    if(!settings) return null

    return settings;
    
  } catch (e) {

    return null;

  }
};
