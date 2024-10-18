//This file contains the models for the various object that are used in the frontend

//------------------------The Response object returned to any axios calls, we added an error field for better error handling on-page ------------------------//
export interface ApiResponse<T> {
    payload?: T;
    error?: string;
}
//------------------------End of the Response object returned to any axios calls, we added an error field for better error handling on-page ------------------------//
//------------------------The loggedIn user------------------------//
export interface LoginCredentials {
    username: string;
    password: string;
}

// new Credentials function
export const newLoginCredentials = (username: string, password: string): LoginCredentials => ({
    username,
    password,
});

export interface User {
    fullName: string;
    userId: string;
    //Vardia of the current user
    shift: string;
}

// Factory function for user data
export const newUser = (fullName: string, userId: string, shift: string): User => ({
    fullName,
    userId,
    shift,
});

//------------------------End of the loggedIn user------------------------//
//--------------The business flow of the PassportCheck procedure------------------------//
//The allowed statuses
export enum ControlStatusTypes {
    None = "None",
    PassportPhoto = "PassportPhoto",
    PassportNFC = "PassportNFC",
    Photo = "Photo",
    Finger = "Finger",
}
//--------------End of the business flow of the PassportCheck procedure------------------------//
//------------------------The data scanned from the MRZ of Passport-------------------//
export interface MRZData {
    documentNumber: string;
    dateOfBirth: string;
    dateOfExpiry: string;
}
// Factory function for nfc data
export const newMRZData = (documentNumber: string, dateOfBirth: string, dateOfExpiry:  string): MRZData => ({
    documentNumber,
    dateOfBirth,
    dateOfExpiry
});

//------------------------End of the data scanned from the MRZ of Passport-------------------//
//------------------------The data scanned from the NFC of Passport-------------------//
//The allowed gender
export enum GenderTypes {
    MALE = "MALE",
    FEMALE = "FEMALE",
}
export interface NFCData {
    faceImage: string; // Base64-encoded string
    familyName: string;
    firstName:  string;
    nationality:  string;
    dateOfBirth: string;
    gender:  GenderTypes;
    type:  string;
    issueCountry:  string;
    documentNumber:  string;
    dateOfExpiry: string
}
// Factory function for nfc data
export const newNFCData = (faceImage: string, familyName: string,firstName:  string,nationality:  string, dateOfBirth: string, gender:  GenderTypes, type:  string, issueCountry:  string,documentNumber:  string, dateOfExpiry: string): NFCData => ({
    faceImage,
    familyName,
    firstName,
    nationality,
    dateOfBirth,
    gender,
    type,
    issueCountry,
    documentNumber,
    dateOfExpiry
});

//------------------------End of the data scanned from the NFC of Passport-------------------//
//------------------------The entire current check Border object (MRZ, Passport, Image and Fingerprints) -------------------//
export interface BorderCheck {
    //Just the first step of scanning mrz data, we keep it to compare it to the NFC Scan later
    mrzData: MRZData,
    mrzChecked: boolean,
    //The data scanned via nfc from passport
    nfcData:  NFCData,
    chipChecked: boolean,
    //The face image of the checked person, in  Base64-encoded string
    faceImage: string,
    //The fingerprint file of the checked person, in  Base64-encoded string
    fingerprints: string
}

// Factory function for nfc data
export const newBorderCheck = (mrzData: MRZData, mrzChecked: boolean, nfcData:  NFCData, chipChecked:  boolean, faceImage: string, fingerprints:  string): BorderCheck => ({
    //Just the first step of scanning mrz data, we keep it to compare it to the NFC Scan later
    mrzData,
    mrzChecked,
    //The data scanned via nfc from passport
    nfcData,
    chipChecked,
    //The face image of the checked person, in  Base64-encoded string
    faceImage,
    //The fingerprint file of the checked person, in  Base64-encoded string
    fingerprints
});
//------------------------End of the the entire current Border check object-------------------//
//------------------------The search results from Police Operation-------------------//
export interface PoliceCheckResult {
    id: string;
    faceImage: string | null; // Base64-encoded string
    familyName: string;
    firstName:  string;
    nationality:  string;
    dateOfBirth: string;
    gender:  GenderTypes;
    crime: string //This is a placeholder
}
// Factory function for nfc data
export const newPoliceCheckResult = (id: string, faceImage: string | null, familyName: string, firstName:  string, nationality:  string, dateOfBirth: string, gender:  GenderTypes, crime:  string): PoliceCheckResult => ({
    id,
    faceImage,
    familyName,
    firstName,
    nationality,
    dateOfBirth,
    gender,
    crime
});

//------------------------End of the data scanned from the NFC of Passport-------------------//
