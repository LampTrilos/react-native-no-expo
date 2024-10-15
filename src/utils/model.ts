//This file contains the models for the various object that are used in the frontend
//------------------------The loggedIn user------------------------//
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

// export class UserClass implements User {
//     constructor(
//         public fullName: string,
//         public userId: string,
//         //Vardia of the current user
//         public shift: string,
//     ) {}
// }

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
//------------------------The data scanned from the nfc of Passport-------------------//
export interface PassportData {
    faceImage: string; // Base64-encoded string
    familyName: string;
    firstName:  string;
    nationality:  string;
    dateOfBirth: String;
    gender:  GenderTypes;
    type:  string;
    issueCountry:  string;
    documentNumber:  string;
    dateOfExpiry: String;
    chipChecked: boolean;
    mrzChecked: boolean;
}
//The allowed gender
export enum GenderTypes {
    MALE = "MALE",
    FEMALE = "FEMALE",
}
// Factory function for nfc data
export const newPassportData = (faceImage: string, familyName: string,firstName:  string,nationality:  string,dateOfBirth: String,gender:  GenderTypes,type:  string,issueCountry:  string,documentNumber:  string, dateOfExpiry: String,chipChecked: boolean,mrzChecked: boolean): PassportData => ({
    faceImage,
    familyName,
    firstName,
    nationality,
    dateOfBirth,
    gender,
    type,
    issueCountry,
    documentNumber,
    dateOfExpiry,
    chipChecked,
    mrzChecked
});


//------------------------End of the data scanned from the nfc of Passport-------------------//
